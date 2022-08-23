package com.submeter.android.activity.register.model;

import com.submeter.android.network.NetworkResponseListener;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface IRegisterModel {
    public void sendRegisterSms(String phone, NetworkResponseListener listener);

    public void register(String account, String password, String phone, String activeCode, String email, int accountType, NetworkResponseListener listener);
}
