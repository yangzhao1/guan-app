package com.submeter.android.activity.companyList.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.companyList.model.CompanyListModel;
import com.submeter.android.activity.companyList.model.ICompanyListModel;
import com.submeter.android.activity.companyList.view.ICompanyView;
import com.submeter.android.entity.AreaBean;
import com.submeter.android.entity.CompanyDataBean;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.MonitorAction;

/**
 * Created by yangzhao on 2019/4/13.
 */

public class CompanyListPersenter implements ICompanyListPresenter,IDataSourceListener<CompanyDataBean> {

    private ICompanyView companyView;
    private ICompanyListModel model;
    private NetworkResponseListener networkResponseListener;
    public CompanyListPersenter(ICompanyView companyView) {
        this.companyView = companyView;
        model = new CompanyListModel();
    }

    @Override
    public void loadData(String searchContent,int state,String org) {
        companyView.showLoadingView();
        model.loadData(this,"",1,searchContent,state,org); //0表示全部
    }

    @Override
    public void onSearch(String search,String sort,
                         int state,String org) {
        companyView.showLoadingView();
        model.onSearch(search,sort,state,org);
    }

    @Override
    public void getAreaList() {
        companyView.showLoadingView();
        networkResponseListener = new NetworkResponseListener(new INetworkResponseListener() {
            @Override
            public void onResponse(String result) {
                companyView.hideLoadingView();
                if (!networkResponseListener.handleInnerError(result)){
                    AreaBean areaBean = new Gson().fromJson(result,AreaBean.class);
                    companyView.updateAreaList(areaBean.getSysOrganizationEntities());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                companyView.hideLoadingView();
            }
        });
        MonitorAction.getArea(SubmeterApp.getInstance().getUserToken(),networkResponseListener);
    }

    @Override
    public boolean isSearch() {
        return model.isSearch();
    }

    @Override
    public void onLoadFinish(CompanyDataBean bean) {
        companyView.hideLoadingView();
        companyView.updateView(bean);
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        companyView.hideLoadingView();
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
