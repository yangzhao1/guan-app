package com.submeter.android.activity.commodityDetail.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.Commodity;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.CommodityAction;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public class CommodityDetailModel implements ICommodityDetailModel, INetworkResponseListener {

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener<Commodity> listener;

    public CommodityDetailModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(int id, IDataSourceListener<Commodity> dataSourceListener) {
        this.listener = dataSourceListener;
        CommodityAction.getCommodity(id, SubmeterApp.getInstance().getUserToken(), networkResponseListener);
    }

    @Override
    public void onResponse(String result) {
        Commodity commodity = null;
        if(!networkResponseListener.handleInnerError(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String dataString = jsonObject.optString("data");
                commodity = new Gson().fromJson(dataString, Commodity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(listener != null) {
            listener.onLoadFinish(commodity);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(listener != null) {
            listener.onLoadFail(networkResponseListener.getErrorCode(), networkResponseListener.getErrorMessage());
        }
    }
}
