package com.submeter.android.activity.commodityList.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.Banner;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.CommodityListAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author thm
 * @date 2018/12/13
 */
public class CommodityListBannerModel implements ICommodityListBannerModel, INetworkResponseListener {

    private String userToken;

    private NetworkResponseListener responseListener;

    private IDataSourceListener<List<Banner>> dataListener;

    public CommodityListBannerModel() {
        userToken = SubmeterApp.getInstance().getUserToken();
        responseListener = new NetworkResponseListener(this);
    }

    @Override
    public void onResponse(String result) {
        if (null == dataListener) {
            return;
        }
        List<Banner> bannerList = null;
        if(!responseListener.handleInnerError(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("ads");
                bannerList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Banner>>() {}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        dataListener.onLoadFinish(bannerList);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void getBannerList(IDataSourceListener<List<Banner>> listener) {
        dataListener = listener;
        CommodityListAction.getBannerList(userToken, responseListener);
    }
}
