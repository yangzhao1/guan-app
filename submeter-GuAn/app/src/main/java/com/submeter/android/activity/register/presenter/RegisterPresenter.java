package com.submeter.android.activity.register.presenter;

import com.android.volley.VolleyError;
import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.register.model.IRegisterModel;
import com.submeter.android.activity.register.model.RegisterModel;
import com.submeter.android.activity.register.view.IRegisterView;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.entity.Profile;
import com.submeter.android.eventbus.MessageEvent;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.tools.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public class RegisterPresenter implements IRegisterPresenter, INetworkResponseListener {
    private final int REQUEST_SEND_SMS = 1;

    private final int REQUEST_REGISTER = 2;

    private int requestType = -1;

    private IRegisterView registerView;

    private IRegisterModel registerModel;

    private NetworkResponseListener networkResponseListener;

    public RegisterPresenter(IRegisterView registerView) {
        this.registerView = registerView;

        registerModel = new RegisterModel();

        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void requestRegisterSMS(String phone) {
        if (Utils.isValidPhone(phone)) {
            registerView.showLoadingView();
            requestType = REQUEST_SEND_SMS;
            registerModel.sendRegisterSms(phone, networkResponseListener);
            registerView.startCountDownTimer(SystemConstant.ACTIVE_CODE_VALID_DURATION);
        } else {
            registerView.showToast(registerView.getBaseActivity().getString(R.string.invalidate_phone));
        }
    }

    @Override
    public void register(String userName, String password, String phone, String activeCode, int accountType) {
        if(checkRegisterInfo(userName, password, phone, activeCode)) {
            requestType = REQUEST_REGISTER;
            registerModel.register(userName, password, phone, activeCode, null, accountType, networkResponseListener);
        }
    }

    private boolean checkRegisterInfo(String userName, String password, String phone, String activeCode) {
        BaseActivity activity = registerView.getBaseActivity();
        if (!Utils.isValidName(userName)) {
            registerView.showToast(activity.getString(R.string.invalidate_account));
            return false;
        }

        if (!Utils.isValidPassword(password)) {
            registerView.showToast(activity.getString(R.string.invalidate_password));
            return false;
        }

        if (!Utils.isValidPhone(phone)) {
            registerView.showToast(activity.getString(R.string.invalidate_phone));
            return false;
        }

        if (!Utils.isValidActiveCode(activeCode)) {
            registerView.showToast(activity.getString(R.string.invalidate_active_code));
            return false;
        }

        return true;
    }

    @Override
    public void onResponse(String result) {
        registerView.hideLoadingView();
        if (!networkResponseListener.handleInnerError(result)) {
            if(requestType == REQUEST_SEND_SMS) {
                registerView.showToast(registerView.getBaseActivity().getString(R.string.active_code_has_send));
            } else if(requestType == REQUEST_REGISTER) {
                Profile profile = SubmeterApp.getInstance().updateLoginUserInfo(result);
                if(profile != null) {
                    registerView.registerSuccess();
                    registerView.cancelCountDownTimer();
                    handleRegisterFinish();
                } else {
                    registerView.registerFailure();
                }
            }
        } else {
            registerView.cancelCountDownTimer();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        registerView.hideLoadingView();
        registerView.cancelCountDownTimer();
        if(requestType == REQUEST_SEND_SMS) {
            registerView.showToast(registerView.getBaseActivity().getString(R.string.active_code_has_send));
        } else if(requestType == REQUEST_REGISTER) {
            registerView.registerFailure();
        }
    }

    private void handleRegisterFinish() {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.REGISTER_SUCCESS));
        registerView.getBaseActivity().finish();
    }

    @Override
    public void goBack() {
        registerView.getBaseActivity().finish();
    }
}
