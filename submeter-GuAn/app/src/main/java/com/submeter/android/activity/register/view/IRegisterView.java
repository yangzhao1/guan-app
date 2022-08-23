package com.submeter.android.activity.register.view;

import com.submeter.android.interfacce.IBaseView;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface IRegisterView extends IBaseView {
    /*开始倒计时*/
    public void startCountDownTimer(int time);

    /*取消倒计时*/
    public void cancelCountDownTimer();

    /*注册成功*/
    public void registerSuccess();

    /*注册失败*/
    public void registerFailure();
}
