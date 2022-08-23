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
public class MonitorAction {
    public static void getCompanyList(String searchC,String sort,String userToken,String pageNum,String pageSize, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("status",sort);
        keyValue.put("name",searchC);
        keyValue.put("pageNum",pageNum);
        keyValue.put("pageSize",pageSize);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_monitorList);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getCompanyListNew(String searchC,String sort,String userToken,
                                         String pageNum,String pageSize,int state,String org,
                                         NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("name",searchC);
        keyValue.put("pageNum",pageNum);
        keyValue.put("pageSize",pageSize);
        if (state!=0){
            keyValue.put("status",state);
        }
        keyValue.put("org",org);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_companyList);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getArea(String userToken,NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_areaList);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getBrandCategory(String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_BRAND_CATEGORY);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getNum(String token, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_num);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

}