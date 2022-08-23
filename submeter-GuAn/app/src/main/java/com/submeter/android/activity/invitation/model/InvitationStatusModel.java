package com.submeter.android.activity.invitation.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.BaseResponse;
import com.submeter.android.entity.InvitationStatus;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.InvitationAction;

import java.util.List;

/**
 * @author thm
 * @date 2018/12/6
 */
public class InvitationStatusModel implements IInvitationStatusModel, INetworkResponseListener {

    private String userToken;

    private NetworkResponseListener responseListener;

    private IDataSourceListener<List<InvitationStatus>> dataListener;

    public InvitationStatusModel() {
        userToken = SubmeterApp.getInstance().getUserToken();
        responseListener = new NetworkResponseListener(this);
    }

    @Override
    public void getFilterStatusList(IDataSourceListener<List<InvitationStatus>> listener) {
        dataListener = listener;
        InvitationAction.getFilterStatusList(userToken, responseListener);
    }

    @Override
    public void onResponse(String result) {
        if (null == dataListener) {
            return;
        }

        BaseResponse<List<InvitationStatus>> baseResponse = new Gson().fromJson(result, new TypeToken<BaseResponse<List<InvitationStatus>>>() {}.getType());
        List<InvitationStatus> invitationStatusList = responseListener.handleInnerError(baseResponse);
        dataListener.onLoadFinish(invitationStatusList);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
