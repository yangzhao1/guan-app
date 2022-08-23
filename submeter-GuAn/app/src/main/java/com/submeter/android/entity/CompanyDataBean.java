package com.submeter.android.entity;

import java.util.List;

/**
 * Created by yangzhao on 2019/12/7 0007.
 */

public class CompanyDataBean {

    private List<Company> list;
    private String productNum;
    private String stopProductNum;
    private String notStartNum;
    private String notFoundNum;

    public List<Company> getList() {
        return list;
    }

    public void setList(List<Company> list) {
        this.list = list;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getStopProductNum() {
        return stopProductNum;
    }

    public void setStopProductNum(String stopProductNum) {
        this.stopProductNum = stopProductNum;
    }

    public String getNotStartNum() {
        return notStartNum;
    }

    public void setNotStartNum(String notStartNum) {
        this.notStartNum = notStartNum;
    }

    public String getNotFoundNum() {
        return notFoundNum;
    }

    public void setNotFoundNum(String notFoundNum) {
        this.notFoundNum = notFoundNum;
    }
}
