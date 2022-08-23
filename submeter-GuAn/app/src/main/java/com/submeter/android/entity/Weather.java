package com.submeter.android.entity;

/**
 * Created by yangzhao on 2019/4/8.
 */

public class Weather {


    /* 城市 */
    private String city;
    /* 天气类型 */
    private String weatherType;
    /* 温度 */
    private String temp;
    /* 日期 */
    private String date;
    /* PM */
    private String pm;
    /* 企业数量 */
    private String companyNums;
    /* 预警状态 */
    private String alert;


    public void setCity(String city) {
        this.city = city;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public void setCompanyNums(String companyNums) {
        this.companyNums = companyNums;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getCity() {
        return city;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public String getTemp() {
        return temp;
    }

    public String getDate() {
        return date;
    }

    public String getPm() {
        return pm;
    }

    public String getCompanyNums() {
        return companyNums;
    }

    public String getAlert() {
        return alert;
    }
}
