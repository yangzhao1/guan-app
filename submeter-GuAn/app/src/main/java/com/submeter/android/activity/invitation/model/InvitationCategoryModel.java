package com.submeter.android.activity.invitation.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.BaseResponse;
import com.submeter.android.entity.InvitationCategory;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.InvitationAction;

import java.util.List;

/**
 * @author thm
 * @date 2018/12/6
 */
public class InvitationCategoryModel implements IInvitationCategoryModel, INetworkResponseListener {

    private String userToken;

    private NetworkResponseListener responseListener;

    private IDataSourceListener<List<InvitationCategory>> dataListener;

    public InvitationCategoryModel() {
        userToken = SubmeterApp.getInstance().getUserToken();
        responseListener = new NetworkResponseListener(this);
    }

    @Override
    public void getFilterCategoryList(IDataSourceListener<List<InvitationCategory>> listener) {
        dataListener = listener;
        InvitationAction.getFilterCategoryList(userToken, responseListener);
    }

    @Override
    public void onResponse(String result) {
        if (null == dataListener) {
            return;
        }
        BaseResponse<List<InvitationCategory>> baseResponse = new Gson().fromJson(result, new TypeToken<BaseResponse<List<InvitationCategory>>>() {}.getType());
        List<InvitationCategory> invitationCategoryList = responseListener.handleInnerError(baseResponse);
        dataListener.onLoadFinish(invitationCategoryList);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
