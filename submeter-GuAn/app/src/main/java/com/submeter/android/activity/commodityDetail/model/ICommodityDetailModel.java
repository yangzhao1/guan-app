package com.submeter.android.activity.commodityDetail.model;

import com.submeter.android.entity.Commodity;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface ICommodityDetailModel {
    public void loadData(int id, IDataSourceListener<Commodity> dataSourceListener);
}
