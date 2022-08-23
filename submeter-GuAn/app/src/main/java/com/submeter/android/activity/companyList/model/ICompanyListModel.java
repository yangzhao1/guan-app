package com.submeter.android.activity.companyList.model;

import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.view.OnRefreshListener;

/**
 * Created by  on 2018/12/1.
 */

public interface ICompanyListModel extends OnRefreshListener{
    /*加载商品分类页面数据*/
    public void loadData(IDataSourceListener listener, String isHandle,
                         int pageNum,String searchC,int state,String org);

    public void gotoCompanyInfo(String markerId);

    void onSearch(String seacrchC,String sort,
                  int state,String org);

    boolean isSearch();
}
