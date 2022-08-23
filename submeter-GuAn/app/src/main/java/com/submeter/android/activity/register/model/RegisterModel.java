package com.submeter.android.activity.register.model;

import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.UserAction;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public class RegisterModel implements IRegisterModel {

    @Override
    public void register(String account, String password, String phone, String activeCode, String email, int accountType, NetworkResponseListener listener) {
        UserAction.register(account, password, phone, activeCode, email, accountType, listener);
    }

    @Override
    public void sendRegisterSms(String phone, NetworkResponseListener listener) {
        UserAction.sendRegisterSMS(phone, listener);
    }
}
