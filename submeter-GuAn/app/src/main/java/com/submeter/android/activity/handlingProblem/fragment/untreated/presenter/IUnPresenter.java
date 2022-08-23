package com.submeter.android.activity.handlingProblem.fragment.untreated.presenter;

import com.submeter.android.view.OnRefreshListener;

/**
 * Created by 2 on 2018/12/1.
 */

public interface IUnPresenter extends OnRefreshListener{

    public void loadData(String isHandle,String searchC,int type,int isTodate);

    public void gotoCompanyInfo(String markerID);

    void onSearch(String isHandle,String search,int type,int isTodate,String startTime,String endTime);

    boolean isSearch();
}
