package com.submeter.android.entity;

import java.util.List;

/**
 * Created by yangzhao on 2019/6/17.
 */

public class CityPieData {

    /**
     * msg : success
     * code : 0
     * list : [{"townName":"北京亦庄永清高新技术产业开发区","orgID":"32","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"刘街乡","orgID":"6","entTotal":1,"meterTotal":1,"violationTotal":0,"violationMonthTotal":0},{"townName":"永清县","orgID":"13","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"里澜城镇","orgID":"7","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"三圣口乡","orgID":"8","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"后奕镇","orgID":"9","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"永清镇","orgID":"24","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"韩村镇","orgID":"25","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"别古庄镇","orgID":"26","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"管家务回族乡","orgID":"27","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"曹家务乡","orgID":"28","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"龙虎庄乡","orgID":"29","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"城区街道办事处","orgID":"30","entTotal":0,"meterTotal":0,"violationTotal":0,"violationMonthTotal":0},{"townName":"河北永清经济开发区","orgID":"31","entTotal":3,"meterTotal":4,"violationTotal":2,"violationMonthTotal":1}]
     */

    private String msg;
    private int code;
    private List<DataBean> list;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getList() {
        return list;
    }

    public void setList(List<DataBean> list) {
        this.list = list;
    }

    public static class DataBean {
        /**
         * townName : 北京亦庄永清高新技术产业开发区
         * orgID : 32
         * entTotal : 0
         * meterTotal : 0
         * violationTotal : 0
         * violationMonthTotal : 0
         */

        private String townName;
        private String orgID;
        private int entTotal;
        private int meterTotal;
        private int violationTotal;
        private int violationMonthTotal;

        public String getTownName() {
            return townName;
        }

        public void setTownName(String townName) {
            this.townName = townName;
        }

        public String getOrgID() {
            return orgID;
        }

        public void setOrgID(String orgID) {
            this.orgID = orgID;
        }

        public int getEntTotal() {
            return entTotal;
        }

        public void setEntTotal(int entTotal) {
            this.entTotal = entTotal;
        }

        public int getMeterTotal() {
            return meterTotal;
        }

        public void setMeterTotal(int meterTotal) {
            this.meterTotal = meterTotal;
        }

        public int getViolationTotal() {
            return violationTotal;
        }

        public void setViolationTotal(int violationTotal) {
            this.violationTotal = violationTotal;
        }

        public int getViolationMonthTotal() {
            return violationMonthTotal;
        }

        public void setViolationMonthTotal(int violationMonthTotal) {
            this.violationMonthTotal = violationMonthTotal;
        }
    }
}
