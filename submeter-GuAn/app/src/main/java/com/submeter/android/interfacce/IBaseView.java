package com.submeter.android.interfacce;

import com.submeter.android.activity.BaseActivity;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface IBaseView {
    public BaseActivity getBaseActivity();

    public void showLoadingView();

    public void hideLoadingView();

    public void showToast(String toast);
}
