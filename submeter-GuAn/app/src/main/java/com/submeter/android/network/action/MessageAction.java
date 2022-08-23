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
 * Created by yangzhao on 2019/4/11.
 */

public class MessageAction {

    public static void getWarnMsgData(int pageNum,int pageSize,String search,String token,NetworkResponseListener responseListener){
        HashMap<String,Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }
        keyValue.put("pageNum", pageNum);
        keyValue.put("pageSize", pageSize);
        keyValue.put("key", search);

        StringBuffer stringBuffer = new StringBuffer(NetworkResConstant.post_warnList);
        stringBuffer.append(NetworkRequestTool.getRequestParam(keyValue));

        JSONObject jsonObject = new JSONObject();

        MyNetworkRequest request = new MyNetworkRequest(Request.Method.POST,stringBuffer.toString(),jsonObject.toString(),responseListener);
        NetworkRequestTool.getInstance().sendRequest(request);
    }

    public static void getSysMsgData(String userToken,int pageNum ,int pageSize,String search,
                                     NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }

        keyValue.put("pageNum", pageNum);
        keyValue.put("pageSize", pageSize);
        keyValue.put("key", search);

        JSONObject object = new JSONObject();

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.post_noticeList);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.POST, strUrl.toString(), object.toString(), responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }
}
