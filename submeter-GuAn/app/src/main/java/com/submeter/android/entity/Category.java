package com.submeter.android.entity;

import java.util.ArrayList;

/**
 * Created by zhao.bo on 2015/8/12.
 */
public class Category {
    /*大分类id*/
    private String id;

    /*分类名称*/
    private String name;

    /*分类的图标*/
    private String image;

    /*子分类*/
    private ArrayList<Category> childList;

    /*分类下的广告*/
    private ArrayList<Banner> adList;

    /*分类下的品牌*/
    private ArrayList<Brand> brandList;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<Category> getChildList() {
        return childList;
    }

    public ArrayList<Banner> getAdList() {
        return adList;
    }

    public ArrayList<Brand> getBrandList() {
        return brandList;
    }
}
