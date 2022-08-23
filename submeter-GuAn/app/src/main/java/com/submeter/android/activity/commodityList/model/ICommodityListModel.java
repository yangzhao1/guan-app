package com.submeter.android.activity.commodityList.model;

import com.submeter.android.activity.commodityList.params.CommodityListParam;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * @author thm
 * @date 2018/12/13
 */
public interface ICommodityListModel {

    void loadData(CommodityListParam params, IDataSourceListener listener);

    void refreshData();

    void loadMore();

    boolean isRefresh();

}
