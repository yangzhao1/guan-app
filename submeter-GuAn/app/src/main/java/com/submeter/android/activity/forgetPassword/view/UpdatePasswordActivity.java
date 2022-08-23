package com.submeter.android.activity.forgetPassword.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.forgetPassword.presenter.IUpdatePasswordPresenter;
import com.submeter.android.activity.forgetPassword.presenter.UpdatePasswordPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdatePasswordActivity extends BaseActivity implements IUpdatePasswordView {

    @BindView(R.id.oldPassword)
    EditText oldPassword;
    @BindView(R.id.newPassword)
    EditText newPassword;
    @BindView(R.id.newPassword2)
    EditText newPassword2;

    private IUpdatePasswordPresenter updatePasswordPresenter;

    @BindView(R.id.submit)
    TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        darkStatusBar = false;

        setContentView(R.layout.activity_forgetpasswordnew, R.string.change_ps, R.drawable.ic_back, "", false);
        ButterKnife.bind(this);

        updatePasswordPresenter = new UpdatePasswordPresenter(this);

        submit.setOnClickListener((view) -> {
            updatePasswordPresenter.changePassword(newPassword.getText().toString(),
                    oldPassword.getText().toString(),newPassword2.getText().toString());
        });
    }

    @Override
    public void updateView(Object data) {
        if (data!=null){
            boolean f = (boolean) data;
            if (f){
                showToast("修改成功");
                finish();
            }
        }
    }
}
