package com.submeter.android.activity.welcome.model;

import com.submeter.android.entity.Banner;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public interface IWelcomeModel {

    public void loadData(IDataSourceListener dataSourceListener);

    public Banner getBanner();

}
