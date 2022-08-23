package com.submeter.android.network.action;

import android.text.TextUtils;

import com.android.volley.Request;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.network.MyNetworkRequest;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.network.NetworkResponseListener;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by yangzhao on 2019/4/15.
 */

public class ProblemAction {

    public static void getProbloemList(String searchC,String isHandle,String userToken,int pageNum,
                                       int type,int isTodate,String startTime,String endTime,NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("isHandle",isHandle);
        keyValue.put("pageNum",pageNum);
        keyValue.put("name",searchC);
        keyValue.put("pageSize","10");
        if (isTodate==1){
            keyValue.put("type",type);
            keyValue.put("isTodate",isTodate);
        }
        keyValue.put("startTime",startTime);
        keyValue.put("endTime",endTime);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.post_outCompanyList);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getViolatorInfo(String id,String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("id",id);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_violatorInfo);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getCompanyPowerList(int id,String currentTime,String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("id",id);
        keyValue.put("currentTime",currentTime);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_powerList);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getDayCompanyPowerList(int id,String currentTime,int type,int stateType,String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("id",id);
        if (!currentTime.isEmpty()){
            keyValue.put("date",currentTime);
        }
        keyValue.put("type",type);
        keyValue.put("statType",stateType);
        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_dayPowerList);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getHoursCompanyPowerList(int id,String currentTime,int type,int stateType,String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("id",id);
        keyValue.put("type",type);
        keyValue.put("statType",stateType);
//        keyValue.put("currentTime",currentTime);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_hoursPowerList);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getDisposeInfo(String id,String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("id",id);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_handleInfo);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void postViolatorData(JSONObject jsonObject, String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }

        JSONObject object = jsonObject;

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.post_submit);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.POST, strUrl.toString(), object.toString(), responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void getFacilityList(String id,String currentTime,String userToken, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }
        keyValue.put("id",id);
        keyValue.put("vioTime",currentTime);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_facilityList);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }
}
