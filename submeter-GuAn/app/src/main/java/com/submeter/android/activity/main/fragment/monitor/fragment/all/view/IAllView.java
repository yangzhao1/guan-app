package com.submeter.android.activity.main.fragment.monitor.fragment.all.view;

import android.support.v4.app.Fragment;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.entity.Monitor;

/**
 * Created by 赵勃 on 2018/11/22.
 */

public interface IAllView {

    public Fragment getFragment();

    BaseActivity getBaseActivity();

    public void updateView(Monitor monitor);

    public void updatePopData(Monitor monitor);
}
