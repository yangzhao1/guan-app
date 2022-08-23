package com.submeter.android.eventbus;

/**
 * Created by 赵勃 on 2018/11/25.
 * EventBus event
 */

public class MessageEvent {
    //close welcome page
    public static final int CLOSE_WELCOME = 1;

    //show the banner from splash
    public static final int SHOW_SPLASH_BANNER = 2;

    //sign in
    public static final int SIGN_IN_SUCCESS = 3;

    //register
    public static final int REGISTER_SUCCESS = 4;

    //signout
    public static final int SIGNOUT_SUCCESS = 5;

    //show main category
    public static final int SHOW_MAIN_CATEGORY = 6;

    //show main category
    public static final int SHOW_MAIN_OUTINFO = 7;
    public static final int TOKEN_EXPIRED = 8;

    /*
    * event type
     */
    private int code;

    /*
    * a entity for event
     */
    private Object data;

    public MessageEvent(int code) {
        this(code, null);
    }

    public MessageEvent(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
