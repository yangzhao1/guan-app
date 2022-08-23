package com.submeter.android.activity.main.fragment.monitor.fragment.all.presenter;

import com.amap.api.maps.AMap;

/**
 * Created by 赵勃 on 2018/12/1.
 */

public interface IAllPresenter {

    public void loadData(String search,AMap aMap,String isHandle);

    public void gotoCompanyInfo(String markerID);

}
