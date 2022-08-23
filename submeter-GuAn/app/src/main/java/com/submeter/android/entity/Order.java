package com.submeter.android.entity;

import java.util.ArrayList;

/**
 * Created by zhao.bo on 2015/8/12.
 */
public class Order {
    public static int STATUS_UNPAY = 0; //未支付

    public static int STATUS_NORMAL = 1;    //支付成功

    public static int STATUS_VERIFIED = 2;  //已验证

    public static int STATUS_CANCEL = 3;    //订单已取消

    private int quantity;

    private int status;

    private long createAt;

    private long lockMaxTime;

    private String coverImage = "";

    private String activityId;

    private String placeName;

    private String orderNumber;

    private String userName;

    private String userPhoneNumber;

    private String activityOptionName;

    private String activityOptionDescription;

    //add for show

    private long performanceBeginAt;

    private ArrayList<OrderCode> orderCodes;

    public String getOrderNumber() {
        return orderNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getActivityOptionName() {
        return activityOptionName;
    }

    public int getStatus() {
        return status;
    }

    public long getPerformanceBeginAt() {
        return performanceBeginAt;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public long getCreateAt() {
        return createAt;
    }

    public long getLockMaxTime() {
        return lockMaxTime;
    }

    public String getActivityOptionDescription() {
        return activityOptionDescription;
    }

    public ArrayList<OrderCode> getOrderCodes() {
        return orderCodes;
    }
}
