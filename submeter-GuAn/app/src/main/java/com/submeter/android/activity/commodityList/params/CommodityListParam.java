package com.submeter.android.activity.commodityList.params;

import com.submeter.android.entity.params.CommonQueryParam;

/**
 * 封装请求参数
 * @author thm
 * @date 2018/12/4
 */
public class CommodityListParam extends CommonQueryParam {
    /**
     * 搜索标题的关键字
     */
    private String key;
    /**
     * 商铺id
     */
    private String storeId;
    /**
     * 品牌id
     */
    private String brandId;
    /**
     * 前台分类
     */
    private String productFrontCategoryId;

    /**
     * 最低价
     */
    private String lowMum;

    /**
     * 最高价
     */
    private String topMum;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getProductFrontCategoryId() {
        return productFrontCategoryId;
    }

    public void setProductFrontCategoryId(String productFrontCategoryId) {
        this.productFrontCategoryId = productFrontCategoryId;
    }

    public String getLowMum() {
        return lowMum;
    }

    public void setLowMum(String lowMum) {
        this.lowMum = lowMum;
    }

    public String getTopMum() {
        return topMum;
    }

    public void setTopMum(String topMum) {
        this.topMum = topMum;
    }
}
