package com.submeter.android.entity;

/**
 * Created by zhao.bo on 2018/12/1.
 */
public class CooperativeBrand {

    /* 品牌id */
    private int id;

    /*品牌logo*/
    private String logo;

    /*品牌名称*/
    private String name;

    /*品牌介绍页面（html）*/
    private String url;

    /*店铺ID*/
    private int storeId;

    /*商家id*/
    private int businessId;

    public int getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getBusinessId() {
        return businessId;
    }
}
