package com.submeter.android.activity.login.model;

import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.UserAction;

/**
 * Created by  on 2018/11/20.
 */

public class LoginModel implements ILoginModel {

    public LoginModel() {

    }

    @Override
    public void login(String account, String password, NetworkResponseListener listener) {
        UserAction.login(account, password, listener);
    }

    @Override
    public void sendLoginSms(String phone, NetworkResponseListener listener) {
        UserAction.sendLoginSMS(phone, listener);
    }
}
