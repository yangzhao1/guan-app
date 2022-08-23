package com.submeter.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangzhao on 2019/4/15.
 */

public class Company implements Parcelable{
    /**
     * id : 5
     * name : 廊坊市1
     * number : dd
     * township : 610100
     * region : 2
     * address : dd
     * productStart : dd
     * productEnd : dd
     * controlLevel : 1
     * enterpriseLevel : 2
     * industryNav : 2
     * allowEia : 1
     * lon : 39.520
     * lat : 116.699
     */

    /**
     * id
     */
    private int id;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 数量
     */
    private String number;
    /**
     * 所属乡
     */
    private String township;
    /**
     * 所属镇
     */
    private String region;
    /**
     * 地址
     */
    private String address;
    /**
     * 开始生产
     */
    private String productStart;
    /**
     * 结束生产
     */
    private String productEnd;
    /**
     * 控制等级
     */
    private String controlLevel;
    /**
     *
     */
    private String enterpriseLevel;
    /**
     *
     */
    private String industryNav;
    /**
     *
     */
    private String allowEia;
    /**
     * 经度
     */
    private String lon;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 联系人
     */
    private String person;
    /**
     * 电话
     */
    private String phone;
    /**
     * 环保信用
     */
    private int score;

    private String sumvalue;
    private String yestoday;
    private String lasthour;
    private String monthAve;

    protected Company(Parcel in) {
        id = in.readInt();
        name = in.readString();
        number = in.readString();
        township = in.readString();
        region = in.readString();
        address = in.readString();
        productStart = in.readString();
        productEnd = in.readString();
        controlLevel = in.readString();
        enterpriseLevel = in.readString();
        industryNav = in.readString();
        allowEia = in.readString();
        lon = in.readString();
        lat = in.readString();
        person = in.readString();
        phone = in.readString();
        score = in.readInt();
        sumvalue = in.readString();
        yestoday = in.readString();
        lasthour = in.readString();
        monthAve = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public void setPerson(String person) {
        this.person = person;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSumvalue() {
        return sumvalue;
    }

    public void setSumvalue(String sumvalue) {
        this.sumvalue = sumvalue;
    }

    public String getYestoday() {
        return yestoday;
    }

    public void setYestoday(String yestoday) {
        this.yestoday = yestoday;
    }

    public String getLasthour() {
        return lasthour;
    }

    public void setLasthour(String lasthour) {
        this.lasthour = lasthour;
    }

    public String getMonthAve() {
        return monthAve;
    }

    public void setMonthAve(String monthAve) {
        this.monthAve = monthAve;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductStart() {
        return productStart;
    }

    public void setProductStart(String productStart) {
        this.productStart = productStart;
    }

    public String getProductEnd() {
        return productEnd;
    }

    public void setProductEnd(String productEnd) {
        this.productEnd = productEnd;
    }

    public String getControlLevel() {
        return controlLevel;
    }

    public void setControlLevel(String controlLevel) {
        this.controlLevel = controlLevel;
    }

    public String getEnterpriseLevel() {
        return enterpriseLevel;
    }

    public void setEnterpriseLevel(String enterpriseLevel) {
        this.enterpriseLevel = enterpriseLevel;
    }

    public String getIndustryNav() {
        return industryNav;
    }

    public void setIndustryNav(String industryNav) {
        this.industryNav = industryNav;
    }

    public String getAllowEia() {
        return allowEia;
    }

    public void setAllowEia(String allowEia) {
        this.allowEia = allowEia;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPerson() {
        return person;
    }

    public String getPhone() {
        return phone;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(township);
        dest.writeString(region);
        dest.writeString(address);
        dest.writeString(productStart);
        dest.writeString(productEnd);
        dest.writeString(controlLevel);
        dest.writeString(enterpriseLevel);
        dest.writeString(industryNav);
        dest.writeString(allowEia);
        dest.writeString(lon);
        dest.writeString(lat);
        dest.writeString(person);
        dest.writeString(phone);
        dest.writeInt(score);
        dest.writeString(sumvalue);
        dest.writeString(yestoday);
        dest.writeString(lasthour);
        dest.writeString(monthAve);
    }
}
