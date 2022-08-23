package com.submeter.android.entity;

/**
 * Created by zhao.bo on 2018/12/1.
 */
public class Event {
    /*活动id*/
    private String id;

    /*活动名称*/
    private String title;

    /*活动图*/
    private String url;

    /*活动开始时间*/
    private long startDate;

    /*活动结束时间*/
    private long endDate;

    /*倒计时时间，单位S*/
    private long reverseDate;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getReverseDate() {
        return reverseDate;
    }
}
