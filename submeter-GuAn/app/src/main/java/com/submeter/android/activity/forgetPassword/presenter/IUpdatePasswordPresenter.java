package com.submeter.android.activity.forgetPassword.presenter;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface IUpdatePasswordPresenter {
    /*用户名不能为空*/
    public final int ERROR_ACCOUNT_EMPTY = 1;

    /*用户名不合法*/
    public final int ERROR_ACCOUNT_INVALIDATE = 2;

    public final int ERROR_PASSWORD_EMPTY = 3;

    public final int ERROR_PASSWORD_THESAME = 4;

    public final int ERROR_NOT_SAME_WITH_CONFIRM_PASSWORD = 5;

    public void goBack();

    public void changePassword(String newPassword, String oldPassword,String newPassword2);

}
