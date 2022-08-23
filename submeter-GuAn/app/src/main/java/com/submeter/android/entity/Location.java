package com.submeter.android.entity;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhao.bo on 2015/8/12.
 */
public class Location {

    private double latitude = -1;

    private double longitude = -1;

    private String district;

    private String street;

    private String province;

    private String city;

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getProvince() {
        return province;
    }

    public String getDetailStreet() {
        StringBuilder stringBuilder = new StringBuilder();
        if(!TextUtils.isEmpty(city)) {
            stringBuilder.append(city);
        }

        if(!TextUtils.isEmpty(district)) {
            stringBuilder.append(district);
        }

        if(!TextUtils.isEmpty(street)) {
            stringBuilder.append(street);
        }

        return stringBuilder.toString();
    }

    public String toStdring() {
        String position = "";
        try {
            JSONObject locationObj = new JSONObject();
            /*if(-1 != cityCode) {
                locationObj.put("cityCode", String.valueOf(cityCode));
            }*/
            if(-1 != latitude || -1 != longitude) {
                locationObj.put("latitude", latitude);
                locationObj.put("longitude", longitude);
            }

            if(null != district) {
                locationObj.put("district", district);
            }

            if(null != street) {
                locationObj.put("street", street);
            }

            if(null != province) {
                locationObj.put("province", province);
            }

            if(null != city) {
                locationObj.put("city", city);
            }

            position = locationObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return position;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
