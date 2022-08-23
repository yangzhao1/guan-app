package com.submeter.android.entity;

import java.util.ArrayList;

/**
 * Created by zhao.bo on 2018/12/1.
 */
public class CooperativeBrandCategory {

    /* 合作品牌分类id */
    private int id;

    /*合作品牌分类名称*/
    private String name;

    ArrayList<CooperativeBrand> cooperativebrandEntity;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<CooperativeBrand> getCooperativebrandEntity() {
        return cooperativebrandEntity;
    }
}
