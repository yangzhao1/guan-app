package com.submeter.android.entity;

/**
 * Created by yangzhao on 2019/12/3 0003.
 */

public class PowerBean {


    /**
     * name : 2019-11-26
     * nameDesc : 时间
     * value : 604
     * valueDesc : 合并统计
     */

    private String name;
    private String nameDesc;
    private float value;
    private String valueDesc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameDesc() {
        return nameDesc;
    }

    public void setNameDesc(String nameDesc) {
        this.nameDesc = nameDesc;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getValueDesc() {
        return valueDesc;
    }

    public void setValueDesc(String valueDesc) {
        this.valueDesc = valueDesc;
    }
}
