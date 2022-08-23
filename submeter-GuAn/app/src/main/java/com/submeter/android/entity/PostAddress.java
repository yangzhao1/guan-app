package com.submeter.android.entity;

import android.text.TextUtils;

/**
 * Created by zhao.bo on 2015/8/12.
 */
public class PostAddress {
    public static final int TICKET_ADDRESS = 1;

    public static final int INVOICE_ADDRESS = 2;

    private String id;

    private String name;

    private String phone;

    private String province;

    private String city;

    private String district;

    private String detailAddress;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getAddress() {
        StringBuilder addressBuilder = new StringBuilder();
        if(!TextUtils.isEmpty(province)) {
            addressBuilder.append(province);
        }

        if(!TextUtils.isEmpty(city)) {
            addressBuilder.append(city);
        }

        if(!TextUtils.isEmpty(district)) {
            addressBuilder.append(district);
        }

        if(!TextUtils.isEmpty(detailAddress)) {
            addressBuilder.append(detailAddress);
        }

        return addressBuilder.toString();
    }

    public String getBriefAddress() {
        StringBuilder addressBuilder = new StringBuilder();
        if(!TextUtils.isEmpty(province)) {
            addressBuilder.append(province);
        }

        if(!TextUtils.isEmpty(city)) {
            addressBuilder.append(city);
        }

        if(!TextUtils.isEmpty(district)) {
            addressBuilder.append(district);
        }

        return addressBuilder.toString();
    }
}
