var stompClient = null;
var gameResponse = null;
var primaryPlayer = false;
var number = 0;
var random = 0;

function start() {
    clearError(ERROR_DISPLAY);
    $(NAME_INPUT_ID).removeClass(INVALID_CLASS);
    var playerName = $.trim($(NAME_INPUT_ID).val());
    if(playerName.length === 0) {
        $(NAME_INPUT_ID).addClass(INVALID_CLASS);
        return;
    }

    stompClient = getStompClient(SOCKJS_URL);

    stompClient.connect({
        username: playerName
    }, function(frame) {
        setConnected(true);
        console.log(frame);
        connectFallback();
    }, parseError);
}

function connectFallback() {
    send(START_REQUEST_URL);
    stompClient.subscribe(UPDATE_QUEUE_URL, function(response) {
    var res = JSON.parse(response.body);
      if(res.status === OK) {
          gameResponse = res.body;
          console.log(gameResponse);
          messageParse();
       } else {
         parseError(res.body.detailedMessage);
       }
   });

   stompClient.subscribe(UPDATE_ERROR_URL, function(response) {
        console.log(response);
   });

}

function messageParse() {
    if(gameResponse === null) {
        return;
    }
    clearError(ERROR_DISPLAY);
    hideControls();
    switch(gameResponse.status) {
        case GameStatus.WAITING: {
             messagePlayView(gameResponse.message);
             break;
        }
        case GameStatus.START: {
            primaryPlayer = gameResponse.primary;
            clearDiv(GAME_BODY_ID);
            $(OPPONENT_ID).html(gameResponse.message).show();
            startSession();
            break;
        }
       case GameStatus.PLAY: {
            play();
            break;
       }
       case GameStatus.DISCONNECT: {
            disconnected();
            break;
       }
       case GameStatus.OVER: {
            gameOver();
            break;
       }
    }
}

function play() {
    number = gameResponse.number;
    messagePlayView(gameResponse.opponent + ' sent value ' + number);
    if(isAutomatic()) {
       automaticPlay();
    } else {
         $(ADDITION_SELECTOR_DIV).show();
    }
}

function automaticPlay() {
    delay(function() {
           if(gameResponse.status === GameStatus.PLAY) {
               var curr = gameResponse.number;
               var addition = divisibleByThree(curr);
               sendValue(addition, curr);
           } else {
                random = randomNumber();
                messagePlayView('You generated the random number: ' + random);
                sendScore(random);
           }
        });
}

function divisibleByThree(value) {
    var modulo = value % 3;
    switch (modulo) {
        case 0:
            return 0;
        case 1:
            return -1;
        default:
            return 1;
    }
}

function startSession() {
    initUIForStart();
    if(isAutomatic() && isPrimaryPlayer()) {
      automaticPlay();
    }
}

function initUIForStart() {
    if(!isAutomatic() && gameResponse !== null) {
        if(isPrimaryPlayer() && gameResponse.status === GameStatus.START) {
            $(RAND_NUMB_SEC).show();
        }

        if(gameResponse.status === GameStatus.PLAY) {
            $(ADDITION_SELECTOR_DIV).show();
        }
    } else {
           hideControls();
    }
}

function hideControls() {
    $(ADDITION_SELECTOR_DIV).hide();
    $(RAND_NUMB_SEC).hide();
}

function clearDiv(id) {
    $(id).html('');
}

function messagePlayView(message) {
    $(GAME_BODY_ID).prepend('<tr><td colspan="2">'+ message + '</td></tr>');
}

function sendRandomNumber() {
    $(RANDOM_INPUT_NUMBER).removeClass(INVALID_CLASS);
    var randomNumber = parseInt($(RANDOM_INPUT_NUMBER).val());
    if(isNaN(randomNumber) || randomNumber <= 0) {
        $(RANDOM_INPUT_NUMBER).addClass(INVALID_CLASS);
        return;
    }
    sendScore(randomNumber);
    hideControls();
}

function sendScore(randomNumber) {
    messagePlayView('You entered value = ' + randomNumber);
    var request = {
        number: randomNumber
    };
    send(SCORE_REQUEST_URL, request);
    gameResponse = null;
}

function addValue() {
    var selectedValue = parseInt($(ADD_SELECT).val());
    var result = selectedValue + number;

    if(result % 3 != 0) {
        parseError(ADDITION_DIVISIBLE_ERROR);
        return;
    }
    sendValue(selectedValue, number);
    hideControls();
}

function sendValue(selectedValue, number) {
    messagePlayView('You selected ' + selectedValue + ' current value is '+ (selectedValue+number)+' [After division : '+ ((number + selectedValue) / 3) + ']' );
    var request = {
        number: number,
        moveAttr: selectedValue
    }
    send(PLAY_URL, request);
    gameResponse = null;
}

function gameOver() {
    if (gameResponse.win) {
        wonMessage();
    } else {
        lostMessage();
    }

    hideControls();
    gameMessage = null;
}

function wonMessage() {
    $(GAME_BODY_ID).prepend('<tr><td colspan="2" class="alert alert-success">You won the game :)</td></tr>');
}

function lostMessage(message) {
    $(GAME_BODY_ID).prepend('<tr><td colspan="2" class="alert alert-danger">You lost the game :(</td></tr>');
}

function isAutomatic() {
    return $(GAME_MODE_ID).val() === 'Auto';
}

function isPrimaryPlayer() {
    return primaryPlayer;
}

function disconnected() {
    messagePlayView(gameResponse.message);
    clearDiv(OPPONENT_ID);
    hideControls();
    gameResponse = null;
    primaryPlayer = true;
}

function parseError(error) {
    headerErrors = null;
    if (error.headers && error.headers.message) {
            headerErrors = error.headers.message;
       }

    if (headerErrors !== null) {
        $(ERROR_DISPLAY + ' span').html(headerErrors);
    } else {
        $(ERROR_DISPLAY + ' span').html(error);
    }

    $(ERROR_DISPLAY).show();
}

function clearError(error) {
    $(ERROR_DISPLAY).hide();
}

function setConnected(connected) {
    $(PLAY_BTN_ID).prop("disabled", connected);
    $(CLOSE_BTN_ID).prop("disabled", !connected);
    $(NAME_INPUT_ID).prop('disabled', connected);
    if (connected) {
        $(PLAY_VIEW_ID).show();
    } else {
        $(PLAY_VIEW_ID).hide();
        hideControls();
    }
    $(GAME_BODY_ID).html("");
}

function delay(fun) {
    setTimeout(fun, 1000);
}

function randomNumber() {
    return Math.floor(Math.random() * 100) + 1;
}

  $(function() {
    $(PLAY_BTN_ID).click(function () {
        start();
    });

    $(CLOSE_BTN_ID).click(function () {
        clearDiv(OPPONENT_ID);
        closeStompClient();
        setConnected(false);
    });

    $(SEND_BTN_ID).click(function () {
        sendRandomNumber();
    });

    $(GAME_MODE_ID).change(function () {
            clearError(ERROR_DISPLAY);
            initUIForStart();
            messageParse();
        }).change();

    $(ADD_SELECT).change(function() {
        clearError(ERROR_DISPLAY);
    }).change();

    $(NUMB_ADDER_BTN_ID).click(addValue);
  });