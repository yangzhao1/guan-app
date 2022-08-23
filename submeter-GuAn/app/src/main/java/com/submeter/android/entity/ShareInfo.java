package com.submeter.android.entity;

/**
 * Created by zhao.bo on 2015/8/12.
 */
public class ShareInfo {
    private String title;

    private String subTitle;

    private String icon = "";

    private String id;

    private String shareType;

    private String wxH5PageUrl = "";

    private String h5PageUrl = "";

    private String miniProgramImage = "";

    private String orderNumber;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getWxH5PageUrl() {
        return wxH5PageUrl;
    }

    public void setWxH5PageUrl(String wxH5PageUrl) {
        this.wxH5PageUrl = wxH5PageUrl;
    }

    public String getH5PageUrl() {
        return h5PageUrl;
    }

    public void setH5PageUrl(String h5PageUrl) {
        this.h5PageUrl = h5PageUrl;
    }

    public String getMiniProgramImage() {
        return miniProgramImage;
    }

    public void setMiniProgramImage(String miniProgramImage) {
        this.miniProgramImage = miniProgramImage;
    }
}
