var stompClient = null;
function connect() {
    var socket = new SockJS('/got/takeaway');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
    stompClient.subscribe('/got/sender/state', function(d) {
        console.log(d);
    });

    stompClient.send('/got/receiver/start', {}, 'a');
  });
}

  $(function() {
    $("#connect").click(function () {
        alert("working");
        connect();
    });

  });