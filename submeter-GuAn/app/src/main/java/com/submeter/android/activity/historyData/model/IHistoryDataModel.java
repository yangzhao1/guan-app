package com.submeter.android.activity.historyData.model;

import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by  on 2018/12/1.
 */

public interface IHistoryDataModel {
    void loadData(IDataSourceListener listener, int id,String currentTime);
}
