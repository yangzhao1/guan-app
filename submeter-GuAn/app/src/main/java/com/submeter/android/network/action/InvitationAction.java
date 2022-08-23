package com.submeter.android.network.action;

import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.submeter.android.activity.invitation.params.InvitationParam;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.network.MyNetworkRequest;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.network.NetworkResponseListener;

import java.util.HashMap;

/**
 * @author thm
 * @date 2018/12/6
 */
public class InvitationAction {

    /**
     * 获取招投标信息列表
     *
     * @param params           封装请求参数
     * @param token            用户Id
     * @param pageNum          当前页数
     * @param pageSize         每页数量
     * @param responseListener 接口回调
     */
    public static void getInvitationList(InvitationParam params,
                                         String token,
                                         int pageNum,
                                         int pageSize,
                                         NetworkResponseListener responseListener) {

        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }
        keyValue.put("pageNum", pageNum);
        keyValue.put("pageSize", pageSize);

        if (params != null) {
            if (!TextUtils.isEmpty(params.getKey())) {
                keyValue.put("key", params.getKey());
            }
            if (!TextUtils.isEmpty(params.getStatus())) {
                keyValue.put("status", params.getStatus());
            }
            if (!TextUtils.isEmpty(params.getInvitationCategoryId())) {
                keyValue.put("invitationCategoryId", params.getInvitationCategoryId());
            }
            if (!TextUtils.isEmpty(params.getStartDate())) {
                keyValue.put("startDate", params.getStartDate());
            }
            if (!TextUtils.isEmpty(params.getEndDate())) {
                keyValue.put("endDate", params.getEndDate());
            }
            if (!TextUtils.isEmpty(params.getIntegratedSort())) {
                keyValue.put("integratedSort", params.getIntegratedSort());
            }
            if (!TextUtils.isEmpty(params.getTimeSort())) {
                keyValue.put("timeSort", params.getTimeSort());
            }
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_INVITATION_SEARCH);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    /**
     * 获取招投标状态列表
     *
     * @param token            用户token
     * @param responseListener 接口回调
     */
    public static void getFilterStatusList(String token,
                                           NetworkResponseListener responseListener) {

        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_INVITATION_STATUS);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    /**
     * 获取招投标采购类别列表
     *
     * @param token            用户token
     * @param responseListener 接口回调
     */
    public static void getFilterCategoryList(String token,
                                             NetworkResponseListener responseListener) {

        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_INVITATION_CATEGORY);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

}