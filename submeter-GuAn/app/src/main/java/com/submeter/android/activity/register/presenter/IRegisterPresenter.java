package com.submeter.android.activity.register.presenter;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface IRegisterPresenter {

    public void goBack();

    public void requestRegisterSMS(String phone);

    public void register(String userName, String password, String phone, String activeCode, int accountType);
}
