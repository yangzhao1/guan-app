package com.submeter.android.activity.commodityList.view;

import com.submeter.android.entity.Banner;
import com.submeter.android.entity.Commodity;
import com.submeter.android.entity.ProductFrontCategory;
import com.submeter.android.interfacce.IBaseView;
import java.util.List;

/**
 * @author thm
 * @date 2018/12/13
 */
public interface ICommodityListView extends IBaseView {

    void updateView(List<Commodity> commodities);

    void notifyNoData();

    void updateFilterCategoryView(List<ProductFrontCategory> productFrontCategoryList);

    void updateBannerView(List<Banner> bannerArrayList);
}
