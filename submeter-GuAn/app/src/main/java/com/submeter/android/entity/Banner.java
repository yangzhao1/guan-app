package com.submeter.android.entity;

/**
 * Created by zhao.bo on 2018/12/1.
 */
public class Banner {

    /* 广告id */
    private int id;

    /*广告图片*/
    private String path;

    /*广告名称*/
    private String title;

    /*店铺ID*/
    private String storeId;

    /*商品id*/
    private String productId;

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getProductId() {
        return productId;
    }
}
