package com.submeter.android.activity.commodityList.model;

import com.submeter.android.activity.commodityList.params.CommodityListParam;
import com.submeter.android.dataSource.CommodityListDataSource;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * @author thm
 * @date 2018/12/13
 */
public class CommodityListModel implements ICommodityListModel {

    private CommodityListDataSource commodityListDataSource;

    @Override
    public void loadData(CommodityListParam params, IDataSourceListener listener) {
        commodityListDataSource = new CommodityListDataSource(listener);
        commodityListDataSource.initData(params);
    }

    @Override
    public void refreshData() {
        commodityListDataSource.onRefresh();
    }

    @Override
    public void loadMore() {
        commodityListDataSource.onLoadMore();
    }

    @Override
    public boolean isRefresh(){
        return commodityListDataSource.isRefresh();
    }

}
