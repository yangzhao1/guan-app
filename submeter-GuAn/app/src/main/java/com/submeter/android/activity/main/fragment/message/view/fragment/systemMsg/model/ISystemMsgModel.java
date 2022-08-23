package com.submeter.android.activity.main.fragment.message.view.fragment.systemMsg.model;

import com.submeter.android.entity.Notice;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.view.OnRefreshListener;

import java.util.List;

/**
 * Created by yangzhao on 2019/4/11.
 */

public interface ISystemMsgModel extends OnRefreshListener{
    void loadData(IDataSourceListener<List<Notice>> listener, int pageNum, int pageSize, String search);
}
