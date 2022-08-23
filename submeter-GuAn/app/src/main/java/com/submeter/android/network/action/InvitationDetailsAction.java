package com.submeter.android.network.action;

import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.network.MyNetworkRequest;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.network.NetworkResponseListener;
import java.util.HashMap;

/**
 * @author thm
 * @date 2018/12/6
 */
public class InvitationDetailsAction {

    /**
     * 获取招投标信息列表
     *
     * @param token            用户Id
     * @param id               招投标id
     * @param responseListener 接口回调
     */
    public static void getInvitationDetails(
                                         String token,
                                         String id,
                                         NetworkResponseListener responseListener) {

        HashMap<String, Object> keyValue = new HashMap<>();

        // TODO token需要写到header里面
        if(!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }
        keyValue.put("id", id);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_INVITATION_DETAILS);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

}