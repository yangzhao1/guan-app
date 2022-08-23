package com.submeter.android.activity.main.fragment.monitor.fragment.getOut.view;

import com.submeter.android.entity.CooperativeBrandCategory;
import com.submeter.android.interfacce.IBaseView;

import java.util.List;

/**
 * Created by on 2018/11/22.
 */

public interface IGetOutView extends IBaseView {

    public void updateView(List<CooperativeBrandCategory> data);
}
