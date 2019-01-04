package com.example.vleh.hw5.common;

public class Constants {

    // these are used by both server and client
    public static final String MSG_DELIMITER = "##";
    public static final int MSG_TYPE_INDEX = 0;
    public static final int MSG_BODY_INDEX = 1;


    // these are used by the client
    public static final int MSG_WORDSTATE_INDEX = 1;
    public static final int MSG_ATTEMPTS_INDEX = 2;
    public static final int MSG_SCORE_INDEX = 3;

    // these are used for extras passed from activity to another
    public static final String EXTRA_HOSTNAME = "HOSTNAME";
    public static final String EXTRA_PORT = "PORT";

    // these are used for throwing errors in AsyncTasks
    public static final String ASYNC_ERROR = "ERROR";

    // Firebase related
    public static final String FIREBASE_TOKEN = "FIREBASE_TOKEN";
}