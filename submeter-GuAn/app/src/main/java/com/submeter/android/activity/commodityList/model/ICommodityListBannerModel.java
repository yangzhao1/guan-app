package com.submeter.android.activity.commodityList.model;

import com.submeter.android.entity.Banner;
import com.submeter.android.interfacce.IDataSourceListener;
import java.util.List;

/**
 * @author thm
 * @date 2018/12/13
 */
public interface ICommodityListBannerModel {

    void getBannerList(IDataSourceListener<List<Banner>> listener);
}
