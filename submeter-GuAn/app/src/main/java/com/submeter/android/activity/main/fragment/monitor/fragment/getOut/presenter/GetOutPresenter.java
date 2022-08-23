package com.submeter.android.activity.main.fragment.monitor.fragment.getOut.presenter;

import android.content.Intent;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.commodityList.view.CommodityListActivity;
import com.submeter.android.activity.main.fragment.monitor.fragment.getOut.model.GetOutModel;
import com.submeter.android.activity.main.fragment.monitor.fragment.getOut.model.IGetOutModel;
import com.submeter.android.activity.main.fragment.monitor.fragment.getOut.view.IGetOutView;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.entity.CooperativeBrandCategory;
import com.submeter.android.interfacce.IDataSourceListener;

import java.util.List;

/**
 * Created by 赵勃 on 2018/12/1.
 */

public class GetOutPresenter implements IGetOutPresenter {
    private IGetOutView categoryView;

    private IGetOutModel categoryModel;

    private IDataSourceListener<List<CooperativeBrandCategory>> dataSourceListener;

    public GetOutPresenter(IGetOutView categoryView) {
        this.categoryView = categoryView;
        categoryModel = new GetOutModel();

        dataSourceListener = new IDataSourceListener<List<CooperativeBrandCategory>>() {
            @Override
            public void onLoadFinish(List<CooperativeBrandCategory> data) {
                GetOutPresenter.this.categoryView.updateView(data);
                GetOutPresenter.this.categoryView.hideLoadingView();
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                GetOutPresenter.this.categoryView.hideLoadingView();
                //GetOutPresenter.this.categoryView.showToast(errorMessage);
            }
        };
    }

    @Override
    public void loadData() {
        categoryModel.loadData(dataSourceListener);
    }

    @Override
    public void gotoBrand(int brandId) {
        BaseActivity activity = categoryView.getBaseActivity();
        Intent intent = new Intent(activity, CommodityListActivity.class);
        intent.putExtra(SystemConstant.COMMODITYLIST_BRANDID_KEY, String.valueOf(brandId));
        activity.startActivity(intent);
    }
}
