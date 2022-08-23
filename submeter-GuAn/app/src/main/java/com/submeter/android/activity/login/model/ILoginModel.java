package com.submeter.android.activity.login.model;

import com.submeter.android.network.NetworkResponseListener;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface ILoginModel {

    public void login(String account, String password, NetworkResponseListener listener);

    public void sendLoginSms(String phone, NetworkResponseListener listener);
}
