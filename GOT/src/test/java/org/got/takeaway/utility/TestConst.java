package org.got.takeaway.utility;

public interface TestConst {
    String USERNAME = "username";
    String PLAYER_1 = "Aqib";
    String PLAYER_2 = "John";
    String SESSION_ID = "sessionId";
    String APP_DEST_PREFIX = "/app";
    String START_REQUEST_URL = APP_DEST_PREFIX + "/start";
    String SCORE_REQUEST_URL = APP_DEST_PREFIX + "/score";
    String BROKER_NAME = "/queue";
    String UPDATE_QUEUE_URL = BROKER_NAME + "/update";
    String PLAY_URL = APP_DEST_PREFIX + "/play";
    String UPDATE_ERROR_URL = BROKER_NAME + "/error";
    String USER_UPDATE_ERROR_URL = "/user" + UPDATE_ERROR_URL;
    String USER_UPDATE_QUEUE_URL = "/user" + UPDATE_QUEUE_URL;
    String SOCKJS_URL = "ws://localhost:%d/takeaway-websockets";
}
