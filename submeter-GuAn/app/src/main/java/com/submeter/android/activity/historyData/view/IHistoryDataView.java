package com.submeter.android.activity.historyData.view;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.entity.Power;

import org.json.JSONArray;

/**
 * Created by  on 2018/11/22.
 */

public interface IHistoryDataView {

    BaseActivity getBaseActivity();

    void updateView(Power data);

    void updatCharDay(JSONArray data);
}
