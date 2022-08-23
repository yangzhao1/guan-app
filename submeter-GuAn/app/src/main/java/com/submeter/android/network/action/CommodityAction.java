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
public class CommodityAction {
    public static void searchCommodity(String keyword, int pageNum, int pageSize, String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("userToken", userToken);
            keyValue.put("key", userToken);
            keyValue.put("pageNum", userToken);
            keyValue.put("pageSize", userToken);
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.SEARCH_COMMODITY);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getBrandCategory(String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("userToken", userToken);
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_BRAND_CATEGORY);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    /*
     * 获取商品详情
     * */
    public static void getCommodity(int productId, String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("userToken", userToken);
        }
        keyValue.put("productId", productId);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_COMMODITY);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }
}