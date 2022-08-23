package com.submeter.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangzhao on 2019/4/15.
 */

public class Violator implements Parcelable{

    /**
     *  id
     */
    private String id;
    /**
     *  时间
     */
    private String vioTime;
    /**
     *  违规详细
     */
    private String mark;
    /**
     *  周违规
     */
    private int isWeek;
    /**
     *  月违规
     */
    private int isMonth;
    /**
     *  是否处理
     */
    private int ishandle;
    /**
     *  状态
     */
    private String status;

    /**
     *  类型
     */
    private String type;

    protected Violator(Parcel in) {
        id = in.readString();
        vioTime = in.readString();
        mark = in.readString();
        isWeek = in.readInt();
        isMonth = in.readInt();
        ishandle = in.readInt();
        status = in.readString();
        type = in.readString();
    }

    public static final Creator<Violator> CREATOR = new Creator<Violator>() {
        @Override
        public Violator createFromParcel(Parcel in) {
            return new Violator(in);
        }

        @Override
        public Violator[] newArray(int size) {
            return new Violator[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVioTime() {
        return vioTime;
    }

    public void setVioTime(String vioTime) {
        this.vioTime = vioTime;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getIsWeek() {
        return isWeek;
    }

    public void setIsWeek(int isWeek) {
        this.isWeek = isWeek;
    }

    public int getIsMonth() {
        return isMonth;
    }

    public void setIsMonth(int isMonth) {
        this.isMonth = isMonth;
    }

    public int getIshandle() {
        return ishandle;
    }

    public void setIshandle(int ishandle) {
        this.ishandle = ishandle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(vioTime);
        dest.writeString(mark);
        dest.writeInt(isWeek);
        dest.writeInt(isMonth);
        dest.writeInt(ishandle);
        dest.writeString(status);
        dest.writeString(type);
    }
}
