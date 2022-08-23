package com.submeter.android.activity.welcome.view;

import android.app.Activity;

import com.submeter.android.entity.Banner;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public interface IWelcomeView {

    public Activity getActivity();

    public void updateView(Banner banner);
}
