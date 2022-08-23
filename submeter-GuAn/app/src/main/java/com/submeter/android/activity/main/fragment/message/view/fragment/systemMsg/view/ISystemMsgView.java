package com.submeter.android.activity.main.fragment.message.view.fragment.systemMsg.view;

import android.support.v4.app.Fragment;

import com.submeter.android.activity.BaseActivity;

import java.util.List;

/**
 * Created by yangzhao on 2019/4/11.
 */

public interface ISystemMsgView  {

    Fragment getFragment();

    BaseActivity getBaseActivity();

    void updateView(List list);
}
