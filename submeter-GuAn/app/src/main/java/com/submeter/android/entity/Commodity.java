package com.submeter.android.entity;

/**
 * Created by zhao.bo on 2015/8/12.
 */
public class Commodity {

    private int id;

    /*商品的title*/
    private String title;

    /*商品图片*/
    private String image;

    /*商城价*/
    private double price;

    /*合约价*/
    private double partnerCost;

    /*商品名称*/
    private String name;

    /*商品销量*/
    private long sales;

    /*0：撮合、1：自营、2：联营*/
    private String typeStore;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public double getPartnerCost() {
        return partnerCost;
    }

    public String getName() {
        return name;
    }

    public String getTypeStore() {
        return typeStore;
    }

    public long getSales() {
        return sales;
    }

    public void setSales(long sales) {
        this.sales = sales;
    }
}
