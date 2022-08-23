package com.submeter.android.activity.login.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.login.model.ILoginModel;
import com.submeter.android.activity.login.model.LoginModel;
import com.submeter.android.activity.login.view.ILoginView;
import com.submeter.android.activity.register.view.RegisterActivity;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.tools.Utils;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public class LoginPresenter implements ILoginPresenter, INetworkResponseListener {

    private final int SEND_LOGIN_SMS = 1;

    private final int LOGIN = 2;

    private int requestType = -1;

    private ILoginView loginView;

    private ILoginModel loginModel;

    private NetworkResponseListener networkResponseListener;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;

        loginModel = new LoginModel();

        networkResponseListener = new NetworkResponseListener(this);
    }

    public int checkLoginInfo(String account, String password) {
        if(TextUtils.isEmpty(account)) {
            return ILoginPresenter.ERROR_ACCOUNT_EMPTY;

//        } else if(!Utils.isValideName(account)) {
//            return ILoginPresenter.ERROR_ACCOUNT_INVALIDATE;
        } else if(TextUtils.isEmpty(password)) {
            return ILoginPresenter.ERROR_PASSWORD_EMPTY;

//        } else if(!Utils.isValidatePassword(password)) {
//            return ILoginPresenter.ERROR_PASSWORD_INVALIDATE;
        } else {
            return 0;
        }
//        return 0;
    }

    @Override
    public void sendLoginSms(String phone) {
        if(Utils.isValidPhone(phone)) {
            requestType = SEND_LOGIN_SMS;
            loginModel.sendLoginSms(phone, networkResponseListener);
        } else {
            loginView.showToast(loginView.getBaseActivity().getString(R.string.invalidate_phone));
        }
    }

    @Override
    public void login(String account, String password) {
        loginView.showLoadingView();
        int checkResult = checkLoginInfo(account, password);
        if(checkResult == 0) {
            requestType = LOGIN;
            loginModel.login(account, password, networkResponseListener);
        } else {
            showLoginErrorMessage(checkResult);
        }
    }

    private void showLoginErrorMessage(int errorCode) {
        Context context = loginView.getBaseActivity();
        switch (errorCode) {
            case ILoginPresenter.ERROR_ACCOUNT_EMPTY: {
                loginView.showToast(context.getString(R.string.account_hint));
                break;
            }

            case ILoginPresenter.ERROR_ACCOUNT_INVALIDATE: {
                loginView.showToast(context.getString(R.string.invalidate_account));
                break;
            }

            case ILoginPresenter.ERROR_PASSWORD_EMPTY: {
                loginView.showToast(context.getString(R.string.password_hint));
                break;
            }

            case ILoginPresenter.ERROR_PASSWORD_INVALIDATE: {
                loginView.showToast(context.getString(R.string.invalidate_password));
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void onResponse(String result) {
        loginView.hideLoadingView();
        if (!networkResponseListener.handleInnerError(result)) {
            if(SubmeterApp.getInstance().updateLoginUserInfo(result) != null) {
                loginView.loginSuccess();
                loginView.getBaseActivity().finish();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        loginView.hideLoadingView();
    }

    @Override
    public void goBack() {
        loginView.getBaseActivity().finish();
    }

    @Override
    public void gotoRegister() {
        BaseActivity activity = loginView.getBaseActivity();
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void forgetPassword() {
        BaseActivity activity = loginView.getBaseActivity();
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }
}
