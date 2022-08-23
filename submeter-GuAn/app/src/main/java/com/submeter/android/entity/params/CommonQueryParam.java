package com.submeter.android.entity.params;

/**
 * 封装请求参数基类
 * @author thm
 * @date 2018/12/4
 */
public class CommonQueryParam {

    /**
     * 综合排序 升序-aes 降序-desc
     */
    private String integratedSort;
    /**
     * 销量排序 升序-aes 降序-desc 默认升序
     */
    private String salesSort;

    /**
     * 价格排序 升序-aes 降序-desc 默认升序
     */
    private String priceSort;

    public String getIntegratedSort() {
        return integratedSort;
    }

    public void setIntegratedSort(String integratedSort) {
        this.integratedSort = integratedSort;
    }

    public String getSalesSort() {
        return salesSort;
    }

    public void setSalesSort(String salesSort) {
        this.salesSort = salesSort;
    }

    public String getPriceSort() {
        return priceSort;
    }

    public void setPriceSort(String priceSort) {
        this.priceSort = priceSort;
    }
}
