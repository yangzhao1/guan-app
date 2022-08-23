package com.submeter.android.activity.forgetPassword.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.submeter.android.R;
import com.submeter.android.activity.forgetPassword.model.IUpdatePasswordModel;
import com.submeter.android.activity.forgetPassword.model.UpdatePasswordModel;
import com.submeter.android.activity.forgetPassword.view.IUpdatePasswordView;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.tools.Utils;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public class UpdatePasswordPresenter implements IUpdatePasswordPresenter,IDataSourceListener {

    private IUpdatePasswordView updatePasswordView;

    private IUpdatePasswordModel loginModel;

    public UpdatePasswordPresenter(IUpdatePasswordView updatePasswordView) {
        this.updatePasswordView = updatePasswordView;
        loginModel = new UpdatePasswordModel();
    }

    private boolean checkAccount(String account) {
        if(TextUtils.isEmpty(account)) {
            updatePasswordView.showToast(updatePasswordView.getBaseActivity().getString(R.string.account_hint));
            return false;
        } else if(!Utils.isValidName(account)) {
            updatePasswordView.showToast(updatePasswordView.getBaseActivity().getString(R.string.invalidate_account));
            return false;
        }

        return true;
    }

    @Override
    public void goBack() {
        updatePasswordView.getBaseActivity().finish();
    }

    @Override
    public void changePassword(String newPassword, String oldPassword,String newPassword2) {
        int chenkId = checkPassword(newPassword, oldPassword,newPassword2);
        if (chenkId==0){
            loginModel.changePassword(this,newPassword, oldPassword);
        }else {
            showErrorMessage(chenkId);
        }
    }

    private int checkPassword(String newPassword, String oldPassword,String newPassword2) {
        if (TextUtils.isEmpty(newPassword)||TextUtils.isEmpty(oldPassword)){
            return IUpdatePasswordPresenter.ERROR_PASSWORD_EMPTY;
        }else if (!newPassword2.equals(newPassword)){
            return IUpdatePasswordPresenter.ERROR_PASSWORD_THESAME;
        }
        return 0;
    }

    private void showErrorMessage(int errorCode) {
        Context context = updatePasswordView.getBaseActivity();
        switch (errorCode) {
            case IUpdatePasswordPresenter.ERROR_PASSWORD_EMPTY: {
                updatePasswordView.showToast(context.getString(R.string.invalidate_password));
                break;
            }

            case IUpdatePasswordPresenter.ERROR_PASSWORD_THESAME: {
                updatePasswordView.showToast(context.getString(R.string.thesame_password));
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onLoadFinish(Object data) {
        updatePasswordView.updateView(data);
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        updatePasswordView.updateView(null);
    }
}
