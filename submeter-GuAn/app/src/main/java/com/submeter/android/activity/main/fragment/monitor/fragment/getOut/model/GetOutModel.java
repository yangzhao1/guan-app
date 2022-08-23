package com.submeter.android.activity.main.fragment.monitor.fragment.getOut.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.CooperativeBrandCategory;
import com.submeter.android.entity.PageData;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.MonitorAction;

import java.util.List;

/**
 * Created by 赵勃 on 2018/12/1.
 */

public class GetOutModel implements IGetOutModel, INetworkResponseListener {

    private IDataSourceListener<List<CooperativeBrandCategory>> listener;

    private NetworkResponseListener responseListener;

    public GetOutModel() {
        responseListener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(IDataSourceListener<List<CooperativeBrandCategory>> listener) {
        this.listener = listener;
        MonitorAction.getBrandCategory(SubmeterApp.getInstance().getUserToken(), responseListener);
    }

    @Override
    public void onResponse(String result) {
        if (!responseListener.handleInnerError(result)) {
            PageData<CooperativeBrandCategory> pageDataList = new Gson().fromJson(result, new TypeToken<PageData<CooperativeBrandCategory>>() {}.getType());
            if(listener != null) {
                listener.onLoadFinish(pageDataList.getList());
            }
        }

        if(listener != null) {
            listener.onLoadFinish(null);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(listener != null) {
            listener.onLoadFail(responseListener.getErrorCode(), responseListener.getErrorMessage());
        }
    }
}
