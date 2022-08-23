package com.submeter.android.activity.companyGetOutInfo.view;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.entity.ViolatorData;

/**
 * Created by  on 2018/11/22.
 */

public interface IVioView {

    BaseActivity getBaseActivity();

    public void updateView(ViolatorData data);
}
