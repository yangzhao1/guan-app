package com.submeter.android.activity.invitation.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.BaseResponse;
import com.submeter.android.entity.Invitation;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.InvitationDetailsAction;

/**
 * @author thm
 * @date 2018/12/6
 */
public class InvitationDetailsModel implements IInvitationDetailsModel, INetworkResponseListener {

    private String userToken;

    private NetworkResponseListener responseListener;

    private IDataSourceListener<Invitation> dataListener;

    public InvitationDetailsModel() {
        userToken = SubmeterApp.getInstance().getUserToken();
        responseListener = new NetworkResponseListener(this);
    }

    @Override
    public void onResponse(String result) {
        if (null == dataListener) {
            return;
        }

        BaseResponse<Invitation> baseResponse = new Gson().fromJson(result, new TypeToken<BaseResponse<Invitation>>() {
        }.getType());
        Invitation invitationDetails = responseListener.handleInnerError(baseResponse);
        dataListener.onLoadFinish(invitationDetails);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (null == dataListener) {
            return;
        }
        dataListener.onLoadFail(responseListener.getErrorCode(),responseListener.getErrorMessage());
    }

    @Override
    public void loadData(String id, IDataSourceListener<Invitation> listener) {
        this.dataListener = listener;
        InvitationDetailsAction.getInvitationDetails(userToken, id, responseListener);
    }
}
