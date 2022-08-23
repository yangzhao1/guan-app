package com.submeter.android.activity.main.fragment.home.model;

import com.submeter.android.entity.HomeData;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by  on 2018/11/28.
 */

public interface IHomeModel {
    public void loadData(IDataSourceListener<HomeData> listener);
}
