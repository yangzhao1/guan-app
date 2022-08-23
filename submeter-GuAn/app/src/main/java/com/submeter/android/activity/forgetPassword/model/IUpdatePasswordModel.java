package com.submeter.android.activity.forgetPassword.model;

import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by  on 2018/11/20.
 */

public interface IUpdatePasswordModel {
    void changePassword(IDataSourceListener dataSourceListener,String newPassword,String oldPassword);
}
