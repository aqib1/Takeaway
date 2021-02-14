function getStompClient(sockJSPath) {
   var socket = new SockJS(sockJSPath);
   stompClient = Stomp.over(socket);
   // not supporting auto-reconnect
   stompClient.reconnect_delay = 0;
   return stompClient;
}

function send(path) {
    stompClient.send(path, {});
}

function send(path, message) {
    stompClient.send(path, {}, JSON.stringify(message));
}

function closeStompClient() {
    if(stompClient !== null) {
        stompClient.disconnect();
    }
}