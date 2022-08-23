package com.submeter.android.activity.companyGetOutInfo.model;

import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.view.OnRefreshListener;

/**
 * Created by  on 2018/12/1.
 */

public interface IVioModel extends OnRefreshListener{
    void loadData(IDataSourceListener listener, String id);
}
