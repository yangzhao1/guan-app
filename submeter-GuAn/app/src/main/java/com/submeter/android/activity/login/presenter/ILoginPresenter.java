package com.submeter.android.activity.login.presenter;

/**
 * Created by  on 2018/11/20.
 */

public interface ILoginPresenter {
    /*用户名不能为空*/
    public final int ERROR_ACCOUNT_EMPTY = 1;

    /*用户名不合法*/
    public final int ERROR_ACCOUNT_INVALIDATE = 2;

    /*密码不能为空*/
    public final int ERROR_PASSWORD_EMPTY = 3;

    /*密码不合法*/
    public final int ERROR_PASSWORD_INVALIDATE = 4;

    public void goBack();

    public void gotoRegister();

    public void sendLoginSms(String phone);

    public void login(String account, String password);

    public void forgetPassword();
}
