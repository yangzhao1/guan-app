package com.submeter.android.activity.commodityList.presenter;


import com.submeter.android.activity.commodityList.params.CommodityListParam;

/**
 * @author thm
 * @date 2018/12/13
 */
public interface ICommodityListPresenter {

    void loadData(CommodityListParam params);

    void refreshData();

    void loadMore();

    boolean isRefresh();

    void getFilterCategoryList();

    void getBannerList();

//    void gotoDetails(String id);

}
