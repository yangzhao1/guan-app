package com.submeter.android.entity;

/**
 * Created by yangzhao on 2019/4/15.
 */

public class WarningMessage {
    /**
     * id
     */
    private String id;
    /**
     * grade 等级
     */
    private String grade;

    /**
     * degree 程度
     */
    private String degree;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 停止时间
     */
    private String stopTime;

    /**
     * 时长
     */
    private String timeLong;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     *  更新时间
     */
    private String updateTime;

    /**
     * 预警对象
     */
    private String warnObj;


    /**
     *  摘要
     */
    private String remark;


    /**
     * 状态
     */
    private String state;


    /**
     *  等级
     */
    private String level;

    public String getId() {
        return id;
    }

    public String getGrade() {
        return grade;
    }

    public String getDegree() {
        return degree;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public String getTimeLong() {
        return timeLong;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getWarnObj() {
        return warnObj;
    }

    public String getRemark() {
        return remark;
    }

    public String getState() {
        return state;
    }

    public String getLevel() {
        return level;
    }
}
