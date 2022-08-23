package com.submeter.android.activity.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.login.presenter.ILoginPresenter;
import com.submeter.android.activity.login.presenter.LoginPresenter;
import com.submeter.android.activity.main.view.MainActivity;
import com.submeter.android.eventbus.MessageEvent;
import com.submeter.android.tools.SharedPreferencesUtils;
import com.submeter.android.view.DrawableEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangzhao on 2019/3/29.
 */

public class LoginActivity extends BaseActivity implements ILoginView{

    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    DrawableEditText passwordEdit;
    @BindView(R.id.back)
    ImageView back;
    private boolean showPassword = false;
    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginnew);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this);
        passwordEdit.setDrawableRightListener(view -> {
            passwordEdit.setEnabled(false);
            showPassword = !showPassword;
            if (showPassword) {
                //选择状态 显示明文--设置为可见的密码
                passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.psd, 0, R.mipmap.psd_hide, 0);
            } else {
                passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.psd, 0, R.mipmap.psd_show, 0);
            }
            passwordEdit.setSelection(passwordEdit.getEditableText().toString().length());
            passwordEdit.setEnabled(true);
        });

        login.setOnClickListener(view -> {
            String accountS = account.getEditableText().toString().trim();
            String password = passwordEdit.getEditableText().toString().trim();
            loginPresenter.login(accountS,password);

        });

        back.setOnClickListener(view -> {
            loginPresenter.goBack();
        });
    }

    @Override
    public void loginSuccess() {
        SharedPreferencesUtils.setParam(getApplicationContext(),"headImage","");
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SIGN_IN_SUCCESS));
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
