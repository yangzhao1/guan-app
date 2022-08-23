package com.submeter.android.activity.handlingProblem.fragment.untreated.view;

import android.support.v4.app.Fragment;

import com.submeter.android.activity.BaseActivity;

import java.util.List;

/**
 * Created by 2 on 2018/11/22.
 */

public interface IUnView {

    public Fragment getFragment();

    BaseActivity getBaseActivity();

    public void updateView(List list);

}
