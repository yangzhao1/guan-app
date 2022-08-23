package com.submeter.android.activity.cityPie.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.cityPie.view.ICityPieView;
import com.submeter.android.entity.CityPieData;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.HomeAction;

/**
 * Created by yangzhao on 2019/6/17.
 */

public class CityPiePresenter implements ICityPiePresenter,INetworkResponseListener{

    private ICityPieView cityPieView;
    private NetworkResponseListener listener;
    public CityPiePresenter(ICityPieView cityPieView) {
        this.cityPieView = cityPieView;
        listener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(String name) {
        cityPieView.getBaseActivity().showLoadingView();
        HomeAction.getPieArea(name, SubmeterApp.getInstance().getUserToken(),listener);
    }

    @Override
    public void onResponse(String result) {
        cityPieView.getBaseActivity().hideLoadingView();
        if (!listener.handleInnerError(result)) {
            CityPieData data = new Gson().fromJson(result,CityPieData.class);
            cityPieView.updateView(data.getList());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        cityPieView.getBaseActivity().hideLoadingView();
        cityPieView.getBaseActivity().showToast(listener.getErrorMessage());
    }
}
