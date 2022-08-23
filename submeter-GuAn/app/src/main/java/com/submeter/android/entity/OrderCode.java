package com.submeter.android.entity;

/**
 * Created by zhao.bo on 2015/8/12.
 */
public class OrderCode {
    public static final int STATUS_NORMAL = 0;

    public static final int STATUS_VERIFIED = 1;

    private String code;

    private int status;

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
