package com.submeter.android.activity.commodityList.presenter;


import com.submeter.android.activity.commodityList.model.CommodityListBannerModel;
import com.submeter.android.activity.commodityList.model.CommodityListCategoryModel;
import com.submeter.android.activity.commodityList.model.CommodityListModel;
import com.submeter.android.activity.commodityList.model.ICommodityListBannerModel;
import com.submeter.android.activity.commodityList.model.ICommodityListCategoryModel;
import com.submeter.android.activity.commodityList.model.ICommodityListModel;
import com.submeter.android.activity.commodityList.params.CommodityListParam;
import com.submeter.android.activity.commodityList.view.ICommodityListView;
import com.submeter.android.entity.Banner;
import com.submeter.android.entity.Commodity;
import com.submeter.android.entity.ProductFrontCategory;
import com.submeter.android.interfacce.IDataSourceListener;
import java.util.List;

/**
 * @author thm
 * @date 2018/12/13
 */
public class CommodityListPresenter implements ICommodityListPresenter {

    private ICommodityListView commodityListView;

    private ICommodityListModel commodityListModel;

    private ICommodityListCategoryModel commodityListCategoryModel;

    private ICommodityListBannerModel commodityListBannerModel;

    private boolean isFirstLoad = true;

    public CommodityListPresenter(ICommodityListView commodityListView) {
        this.commodityListView = commodityListView;
        commodityListModel = new CommodityListModel();
        commodityListCategoryModel = new CommodityListCategoryModel();
        commodityListBannerModel = new CommodityListBannerModel();
    }

    @Override
    public void loadData(CommodityListParam params) {
        commodityListModel.loadData(params, new IDataSourceListener<List<Commodity>>() {
            @Override
            public void onLoadFinish(List<Commodity> data) {
                if (data != null) {
                    commodityListView.updateView(data);
                    isFirstLoad = false;
                } else {
                    commodityListView.notifyNoData();
                }
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                if(isFirstLoad) {
                    commodityListView.getBaseActivity().finish();
                }
            }
        });
    }

    @Override
    public void refreshData() {
        commodityListModel.refreshData();
    }

    @Override
    public void loadMore() {
        commodityListModel.loadMore();
    }

    @Override
    public boolean isRefresh() {
        return commodityListModel.isRefresh();
    }

    @Override
    public void getFilterCategoryList() {
        commodityListCategoryModel.getFilterCategoryList(new IDataSourceListener<List<ProductFrontCategory>>() {
            @Override
            public void onLoadFinish(List<ProductFrontCategory> data) {
                commodityListView.updateFilterCategoryView(data);
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                commodityListView.getBaseActivity().finish();
            }
        });
    }

    @Override
    public void getBannerList() {
        commodityListBannerModel.getBannerList(new IDataSourceListener<List<Banner>>() {
            @Override
            public void onLoadFinish(List<Banner> data) {
                commodityListView.updateBannerView(data);
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                commodityListView.getBaseActivity().finish();
            }
        });
    }
//
//    @Override
//    public void gotoDetails(String id) {
//        BaseActivity activity = invitationView.getBaseActivity();
//        Intent intent = new Intent(activity, InvitationDetailsActivity.class);
//        intent.putExtra(SystemConstant.INVITATION_KEY, id);
//        activity.startActivity(intent);
//    }

}
