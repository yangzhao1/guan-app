package com.submeter.android.activity.main.fragment.home.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.HomeData;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.HomeAction;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 赵勃 on 2018/11/28.
 */

public class HomeModel implements IHomeModel, INetworkResponseListener {

    //首页的数据实体
    private HomeData homeData;

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener<HomeData> loadDataListener;

    public HomeModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(IDataSourceListener<HomeData> listener) {
        this.loadDataListener = listener;
        HomeAction.getHome(SubmeterApp.getInstance().getUserToken(), networkResponseListener);
    }

    @Override
    public void onResponse(String result) {
        if (!networkResponseListener.handleInnerError(result)) {
            try {
                JSONObject resultObj = new JSONObject(result);
                JSONObject dataObj = resultObj.getJSONObject("data");
                homeData = new Gson().fromJson(dataObj.toString(), new TypeToken<HomeData>() {}.getType());
                if (homeData!=null){
                    loadDataListener.onLoadFinish(homeData);
                }else {
                    loadDataListener.onLoadFinish(null);
                }
            } catch (JSONException e) {

            }
        }else {
            loadDataListener.onLoadFinish(null);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(loadDataListener != null) {
            loadDataListener.onLoadFail(networkResponseListener.getErrorCode(), networkResponseListener.getErrorMessage());
        }
    }
}
