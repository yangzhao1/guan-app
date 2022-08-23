package com.submeter.android.activity.commodityList.model;

import com.submeter.android.entity.ProductFrontCategory;
import com.submeter.android.interfacce.IDataSourceListener;
import java.util.List;

/**
 * @author thm
 * @date 2018/12/6
 */
public interface ICommodityListCategoryModel {

    void getFilterCategoryList(IDataSourceListener<List<ProductFrontCategory>> listener);
}
