package com.submeter.android.entity;

/**
 * Created by yangzhao on 2019/4/9.
 */

public class MonitorData {

    /**
     * 正常
     */
    private String normal;
    /**
     * 异常
     */
    private String abnormal;
    /**
     * 停工
     */
    private String stoppage;

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal;
    }

    public String getStoppage() {
        return stoppage;
    }

    public void setStoppage(String stoppage) {
        this.stoppage = stoppage;
    }
}
