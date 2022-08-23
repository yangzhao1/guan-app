package com.submeter.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yangzhao on 2019/4/15.
 */

public class ViolatorData implements Parcelable{

    /**
     * id
     */
    private String enId;
    /**
     * 企业名称
     */
    private String enname;
    /**
     * 经度
     */
    private String areaX;
    /**
     * 纬度
     */
    private String areaY;
    /**
     * 数量
     */
    private Object nums;
    /**
     * 联系人
     */
    private String person;
    /**
     * 电话
     */
    private String phone;
    /**
     * 地址
     */
    private String address;
    /**
     * 周违规次数
     */
    private int weekNum;
    /**
     * 月违规次数
     */
    private int monthNum;

    /**
     * 违规列表
     */
    private List<Violator> vioDtos;

    protected ViolatorData(Parcel in) {
        enId = in.readString();
        enname = in.readString();
        areaX = in.readString();
        areaY = in.readString();
        person = in.readString();
        phone = in.readString();
        address = in.readString();
        weekNum = in.readInt();
        monthNum = in.readInt();
    }

    public static final Creator<ViolatorData> CREATOR = new Creator<ViolatorData>() {
        @Override
        public ViolatorData createFromParcel(Parcel in) {
            return new ViolatorData(in);
        }

        @Override
        public ViolatorData[] newArray(int size) {
            return new ViolatorData[size];
        }
    };

    public List<Violator> getList() {
        return vioDtos;
    }

    public String getEnId() {
        return enId;
    }

    public void setEnId(String enId) {
        this.enId = enId;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getAreaX() {
        return areaX;
    }

    public void setAreaX(String areaX) {
        this.areaX = areaX;
    }

    public String getAreaY() {
        return areaY;
    }

    public void setAreaY(String areaY) {
        this.areaY = areaY;
    }

    public Object getNums() {
        return nums;
    }

    public void setNums(Object nums) {
        this.nums = nums;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    public int getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(int monthNum) {
        this.monthNum = monthNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(enId);
        dest.writeString(enname);
        dest.writeString(areaX);
        dest.writeString(areaY);
        dest.writeString(person);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeInt(weekNum);
        dest.writeInt(monthNum);
    }
}
