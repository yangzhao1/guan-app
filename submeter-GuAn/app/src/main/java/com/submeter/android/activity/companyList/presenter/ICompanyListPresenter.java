package com.submeter.android.activity.companyList.presenter;

import com.submeter.android.view.OnRefreshListener;

/**
 * Created by yangzhao on 2019/4/13.
 */

public interface ICompanyListPresenter extends OnRefreshListener {

    void loadData(String searchContent,int state,String org);

    void onSearch(String search,String sort,
                  int state,String org);

    void getAreaList();

    boolean isSearch();
}
