package com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.model;

import com.submeter.android.entity.MessageData;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by yangzhao on 2019/4/11.
 */

public interface IDisposeMsgModel {
    void loadData(IDataSourceListener<MessageData> listener,int pageNum,int pageSize,String search);

    void refreshData();

    void loadMoreData();

    boolean isRefresh();
}
