package com.submeter.android.activity.main.fragment.monitor.fragment.all.model;

import com.submeter.android.entity.Monitor;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by 赵勃 on 2018/12/1.
 */

public interface IAllModel {
    /*加载商品分类页面数据*/
    public void loadData(String search,IDataSourceListener<Monitor> listener,String isHandle);

    public void gotoCompanyInfo(String markerId);


}
