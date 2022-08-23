package com.submeter.android.activity.cityPie.view;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.entity.CityPieData;

import java.util.List;

/**
 * Created by  on 2018/11/22.
 */

public interface ICityPieView {

    BaseActivity getBaseActivity();

    void updateView(List<CityPieData.DataBean> data);
}
