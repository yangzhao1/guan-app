package com.submeter.android.network.action;

import com.android.volley.Request;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.network.MyNetworkRequest;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.network.NetworkResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by yangzhao on 2019/4/10.
 */

public class MeAction {

    public static void getNewVersions(NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.get_versions);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    public static void uploadHeadImage(String token,File file, NetworkResponseListener listener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        keyValue.put("token",token);
        StringBuffer strUrl = new StringBuffer(NetworkResConstant.post_uploadImage);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        JSONObject object = new JSONObject();
        try {
            object.put("file",file);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyNetworkRequest networkRequest = new MyNetworkRequest(Request.Method.POST, strUrl.toString(),
                object.toString(), listener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }
}
