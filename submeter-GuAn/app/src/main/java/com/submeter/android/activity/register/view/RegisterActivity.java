package com.submeter.android.activity.register.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.register.presenter.IRegisterPresenter;
import com.submeter.android.activity.register.presenter.RegisterPresenter;
import com.submeter.android.view.DrawableEditText;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements IRegisterView {

    private boolean showPassword = false;

    private boolean showConfirmPassword = false;

    @BindView(R.id.statusbar_view)
    View statusView;

    @BindView(R.id.sp_view)
    TextView spView;

    @BindView(R.id.sp_indicator)
    View spIndicator;

    @BindView(R.id.buyer)
    TextView buyerView;

    @BindView(R.id.buyer_indicator)
    View buyerIndicator;

    @BindView(R.id.supplier)
    TextView supplierView;

    @BindView(R.id.supplier_indicator)
    View supplierIndicator;

    @BindView(R.id.account_edit)
    EditText accountEdit;

    @BindView(R.id.psd_edit)
    DrawableEditText passwordEdit;

    @BindView(R.id.confirm_psd_edit)
    DrawableEditText confirmPsdEdit;

    @BindView(R.id.phone_edit)
    EditText phoneEdit;

    @BindView(R.id.active_code_edit)
    EditText activityCodeEdit;

    @BindView(R.id.get_activity_code)
    TextView verifyCodeBtn;

    @BindView(R.id.ok_btn)
    Button okBtn;

    @BindColor(R.color.blue_color)
    int selectedColor;

    @BindColor(R.color.black_color)
    int normalColor;

    private boolean verifyCodeEnable = true;

    private CountDownTimer timer;

    private IRegisterPresenter registerPresenter;

    private int accountType = -1; //1：供应商；2：经销商(服务商)；3：采购商（开发商）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        darkStatusBar = false;

        setContentView(R.layout.activity_register);

        registerPresenter = new RegisterPresenter(this);

        passwordEdit.setDrawableRightListener(new DrawableEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                showPassword = !showPassword;
                showPasswordClicked(passwordEdit, showPassword);
            }
        });

        confirmPsdEdit.setDrawableRightListener(new DrawableEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                showConfirmPassword = !showConfirmPassword;
                showPasswordClicked(confirmPsdEdit, showConfirmPassword);
            }
        });

        switchAccountType(1);
    }

    private void showPasswordClicked(DrawableEditText passwordEdit, boolean showPassword) {
        passwordEdit.setEnabled(false);
        if(showPassword){
            //选择状态 显示明文--设置为可见的密码
            passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.psd, 0, R.mipmap.psd_hide, 0);
        } else {
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.psd, 0, R.mipmap.psd_show, 0);
        }
        passwordEdit.setSelection(passwordEdit.getEditableText().toString().length());
        passwordEdit.setEnabled(true);
    }

    @OnCheckedChanged(R.id.register_terms_text)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            okBtn.setEnabled(true);
        } else {
            okBtn.setEnabled(false);
        }
    }

    public void switchAccountType(int accountType) {
        if (accountType != this.accountType) {
            this.accountType = accountType;

            spView.setTextColor(normalColor);
            spIndicator.setVisibility(View.GONE);

            supplierView.setTextColor(normalColor);
            supplierIndicator.setVisibility(View.GONE);

            buyerView.setTextColor(normalColor);
            buyerIndicator.setVisibility(View.GONE);

            if (accountType == 1) {
                supplierView.setTextColor(selectedColor);
                supplierIndicator.setVisibility(View.VISIBLE);
            } else if (accountType == 2) {
                spView.setTextColor(selectedColor);
                spIndicator.setVisibility(View.VISIBLE);
            } else if (accountType == 3) {
                buyerView.setTextColor(selectedColor);
                buyerIndicator.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.left_image, R.id.sp_view, R.id.supplier, R.id.buyer, R.id.ok_btn, R.id.get_activity_code})
    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.left_image: {
                registerPresenter.goBack();
                break;
            }

            case R.id.sp_view: {
                switchAccountType(2);
                break;
            }

            case R.id.supplier: {
                switchAccountType(1);
                break;
            }

            case R.id.buyer: {
                switchAccountType(3);
                break;
            }

            case R.id.get_activity_code: {
                String phone = phoneEdit.getEditableText().toString().trim();
                registerPresenter.requestRegisterSMS(phone);

                break;
            }

            case R.id.ok_btn: {
                String account = accountEdit.getEditableText().toString().trim();
                String password = passwordEdit.getEditableText().toString().trim();
                String phoneNumber = phoneEdit.getEditableText().toString().trim();
                String activeCode = activityCodeEdit.getEditableText().toString().trim();
                registerPresenter.register(account, password, phoneNumber, activeCode, accountType);
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void startCountDownTimer(int time) {
        if(!verifyCodeEnable) {
            return;
        }

        verifyCodeEnable = false;
        changeActiveCodeBtnStatus(false);
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onFinish() {
                cancelCountDownTimer();
            }

            @Override
            public void onTick(long l) {
                verifyCodeBtn.setText(String.format(getString(R.string.register_get_active_disable), l / 1000));
            }
        }.start();
    }

    @Override
    public void cancelCountDownTimer() {
        if(verifyCodeEnable) {
            return;
        }

        verifyCodeEnable = true;
        verifyCodeBtn.setText(R.string.get_activity_code_label);
        changeActiveCodeBtnStatus(true);
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void registerSuccess() {

    }

    @Override
    public void registerFailure() {

    }

    private void changeActiveCodeBtnStatus(boolean enable) {
        if (enable) {
            verifyCodeBtn.setEnabled(true);
            verifyCodeBtn.setTextColor(getResources().getColor(R.color.white_color));
        } else {
            verifyCodeBtn.setEnabled(false);
            verifyCodeBtn.setTextColor(getResources().getColor(R.color.login_hint_color));
        }
    }
}
