package com.submeter.android.activity.companyInfo.model;

import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by  on 2018/12/1.
 */

public interface IComInfoModel{
    void loadData(IDataSourceListener listener, int id,String currentTime);
}
