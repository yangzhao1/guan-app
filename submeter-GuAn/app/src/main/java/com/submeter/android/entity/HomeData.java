package com.submeter.android.entity;

import java.util.List;

/**
 * Created by yang on 2019/4/8.
 */

public class HomeData {

    /**
     * 实时天气情况
     */
    private Weather weather;
    /**
     * 企业数量
     */
    private int entNum=0;
    /**
     * 首页轮播公告
     */
    private List<Notice> noticeDtoList;

    public int getCompanyNum() {
        return entNum;
    }

    public void setCompanyNum(int entNum) {
        this.entNum = entNum;
    }

    public Weather getWeather() {
        return weather;
    }

    public List<Notice> getNotices() {
        return noticeDtoList;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public void setNotices(List<Notice> noticeDtoList) {
        this.noticeDtoList = noticeDtoList;
    }
}
