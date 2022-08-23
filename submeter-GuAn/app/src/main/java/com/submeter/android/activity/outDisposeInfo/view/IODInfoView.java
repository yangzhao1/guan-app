package com.submeter.android.activity.outDisposeInfo.view;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.entity.OutDisposeData;

/**
 * Created by  on 2018/11/22.
 */

public interface IODInfoView {

    BaseActivity getBaseActivity();

    void updateView(OutDisposeData data);
}
