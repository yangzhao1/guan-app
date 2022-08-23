package com.submeter.android.activity.forgetPassword.model;

import com.android.volley.VolleyError;
import com.submeter.android.SubmeterApp;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.UserAction;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public class UpdatePasswordModel implements IUpdatePasswordModel, INetworkResponseListener {
    private final int SEND_SMS = 1;

    private final int RESET_PASSWORD = 2;

    private int requestType = -1;

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener<Boolean> dataSourceListener;

    public UpdatePasswordModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void changePassword(IDataSourceListener dataSourceListener,String newPassword, String oldPassword) {
        this.dataSourceListener = dataSourceListener;
        UserAction.changePassword(SubmeterApp.getInstance().getUserToken(),newPassword,oldPassword,networkResponseListener);
    }

    @Override
    public void onResponse(String result) {
        boolean success = false;
        if(!networkResponseListener.handleInnerError(result)) {
            success = true;
        }
        if(dataSourceListener != null) {
            dataSourceListener.onLoadFinish(success);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(dataSourceListener != null) {
            dataSourceListener.onLoadFail(networkResponseListener.getErrorCode(), networkResponseListener.getErrorMessage());
        }
    }
}
