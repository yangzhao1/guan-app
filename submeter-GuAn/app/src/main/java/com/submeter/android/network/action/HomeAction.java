package com.submeter.android.network.action;

import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.network.MyNetworkRequest;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.network.NetworkResponseListener;

import java.util.HashMap;

/**
 * Created by zhao.bo on 2015/9/10.
 */
public class HomeAction {
    public static void getHome(String userToken,NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_homeData);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getWeather(NetworkResponseListener responseListener) {
        StringBuffer strUrl = new StringBuffer(NetworkResConstant.getWeather);

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getPieArea(String name,String userToken,NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("name", name);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_area);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }
}