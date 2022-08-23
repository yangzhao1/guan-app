package com.submeter.android.activity.handlingProblem.fragment.untreated.model;

import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.view.OnRefreshListener;

/**
 * Created by  on 2018/12/1.
 */

public interface IUnModel extends OnRefreshListener{
    /*加载商品分类页面数据*/
    public void loadData(IDataSourceListener listener, String isHandle,int pageNum,
                         String SearchC,int type,int isTodate);

    public void gotoCompanyInfo(String markerId);

    void onSearch(String isHandle,String searchC,int type,int isTodate,String startTime,String endTime);

    boolean isSearch();
}
