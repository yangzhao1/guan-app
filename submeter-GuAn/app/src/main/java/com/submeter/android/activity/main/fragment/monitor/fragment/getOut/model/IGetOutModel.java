package com.submeter.android.activity.main.fragment.monitor.fragment.getOut.model;

import com.submeter.android.entity.CooperativeBrandCategory;
import com.submeter.android.interfacce.IDataSourceListener;

import java.util.List;

/**
 * Created by 赵勃 on 2018/12/1.
 */

public interface IGetOutModel {

    public void loadData(IDataSourceListener<List<CooperativeBrandCategory>> listener);


}
