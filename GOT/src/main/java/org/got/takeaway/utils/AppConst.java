package org.got.takeaway.utils;

public interface AppConst {
    int DIVISOR = 3;
    String USERNAME = "username";

    String SOCKJS_URL = "/takeaway-websockets";
    String APP_DEST_PREFIX = "/app";
    String START_REQUEST_URL = "/start";
    String SCORE_REQUEST_URL = "/score";
    String PLAY_URL = "/play";
    String BROKER_NAME = "/queue";
    String UPDATE_QUEUE_URL = BROKER_NAME + "/update";
}
