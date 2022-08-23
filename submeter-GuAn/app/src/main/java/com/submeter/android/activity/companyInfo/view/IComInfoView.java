package com.submeter.android.activity.companyInfo.view;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.entity.Power;

import org.json.JSONArray;

/**
 * Created by  on 2018/11/22.
 */

public interface IComInfoView {

    BaseActivity getBaseActivity();

    void updateView(Power data);

    void updatChar(JSONArray data);

    void updatCharDay(JSONArray lineDatas);
}
