var stompClient = null;

function start() {
    var playerName = $.trim($('#name').val());
    var socket = new SockJS('/takeaway-websockets');
    stompClient = Stomp.over(socket);
    // not supporting auto-reconnect
    stompClient.reconnect_delay = 0;
    stompClient.connect({
        username: playerName
    }, function(frame) {
    play();
    stompClient.subscribe('/user/queue/advise', function(d) {
       console.log(d);
    });
  });
}

function play() {
    stompClient.send('/app/start', {});
}

function close() {
    if(stompClient !== null) {
        stompClient.disconnect();
    }
}

  $(function() {
    $("#play").click(function () {
        start();
    });

    $("#close").click(function () {
        close();
    });
  });