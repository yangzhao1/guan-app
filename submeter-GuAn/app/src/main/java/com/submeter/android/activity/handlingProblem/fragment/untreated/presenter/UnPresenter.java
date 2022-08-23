package com.submeter.android.activity.handlingProblem.fragment.untreated.presenter;

import com.submeter.android.activity.handlingProblem.fragment.untreated.model.UnModel;
import com.submeter.android.activity.handlingProblem.fragment.untreated.view.IUnView;
import com.submeter.android.interfacce.IDataSourceListener;

import java.util.List;

/**
 * Created by 赵勃 on 2018/12/1.
 */

public class UnPresenter implements IUnPresenter, IDataSourceListener<List> {
    private IUnView allView;
    private UnModel model;

    public UnPresenter(IUnView allView) {
        this.allView = allView;
        model = new UnModel();
    }

    @Override
    public void loadData(String isHandle,String searchC,int type,int isTodate) {
        allView.getBaseActivity().showLoadingView();
        model.loadData(this,isHandle,1,searchC,type,isTodate);
    }

    @Override
    public void gotoCompanyInfo(String markerID) {
        model.gotoCompanyInfo(markerID);
    }

    @Override
    public void onSearch(String isHandle,String search,int type,int isTodate,
                         String startTime,String endTime) {
        allView.getBaseActivity().showLoadingView();
        model.onSearch(isHandle,search,type,isTodate,startTime,endTime);
    }

    @Override
    public boolean isSearch() {
        return model.isSearch();
    }

    @Override
    public void onLoadFinish(List list) {
        allView.getBaseActivity().hideLoadingView();
        allView.updateView(list);
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        allView.getBaseActivity().hideLoadingView();
    }

    @Override
    public void onRefresh() {
        model.onRefresh();
    }

    @Override
    public void onLoadMore() {
        model.onLoadMore();
    }

    @Override
    public boolean isRefresh() {
        return model.isRefresh();
    }
}
