package com.submeter.android.entity;

/**
 * Created by yangzhao on 2019/4/15.
 * 违规企业
 */

public class GetOutUn {
    /**
     *  id
     */
    private String enId;

    /**
     *  城市
     */
    private String enname;

    /**
     *  经度
     */
    private String areaX;


    /**
     *  纬度
     */
    private String areaY;

    /**
     *  数量
     */
    private String nums;

    /**
     *  联系人
     */
    private String person;

    /**
     *  联系电话
     */
    private String phone;

    private String sumvalue;
    private String yestoday;
    private String lasthour;
    private String monthAve;

    public String getSumvalue() {
        return sumvalue;
    }

    public String getYestoday() {
        return yestoday;
    }

    public String getLasthour() {
        return lasthour;
    }

    public String getMonthAve() {
        return monthAve;
    }

    public String getEnId() {
        return enId;
    }

    public String getEnname() {
        return enname;
    }

    public String getAreaX() {
        return areaX;
    }

    public String getAreaY() {
        return areaY;
    }

    public String getNums() {
        return nums;
    }

    public String getPerson() {
        return person;
    }

    public String getPhone() {
        return phone;
    }
}
