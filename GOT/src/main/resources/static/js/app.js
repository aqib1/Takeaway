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

function create_UUID(){
    var dt = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (dt + Math.random()*16)%16 | 0;
        dt = Math.floor(dt/16);
        return (c=='x' ? r :(r&0x3|0x8)).toString(16);
    });
    return String(uuid);
}

  $(function() {
    $("#play").click(function () {
        start();
    });

    $("#close").click(function () {
        close();
    });
  });