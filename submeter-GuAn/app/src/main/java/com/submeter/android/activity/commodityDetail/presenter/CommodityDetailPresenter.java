package com.submeter.android.activity.commodityDetail.presenter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.submeter.android.R;
import com.submeter.android.activity.commodityDetail.model.CommodityDetailModel;
import com.submeter.android.activity.commodityDetail.model.ICommodityDetailModel;
import com.submeter.android.activity.commodityDetail.view.ICommodityDetailView;
import com.submeter.android.entity.Commodity;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.tools.JSHookUtils;

import java.util.HashMap;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public class CommodityDetailPresenter implements ICommodityDetailPresenter {

    private boolean webLoadFinished = false;

    private Commodity commodity;

    private ICommodityDetailView commodityDetailView;

    private ICommodityDetailModel commodityDetailModel;

    public CommodityDetailPresenter(ICommodityDetailView commodityDetailView) {
        this.commodityDetailView = commodityDetailView;

        commodityDetailModel = new CommodityDetailModel();
    }

    @Override
    public void goBack() {
        commodityDetailView.getBaseActivity().finish();
    }

    @Override
    public void gotoCS() {

    }

    @Override
    public void gotoShop() {

    }

    @Override
    public void follow() {

    }

    @Override
    public void addShoppingcart() {

    }

    @Override
    public void gotoBuy() {

    }

    @Override
    public void loadCommodity(int id) {
        commodityDetailModel.loadData(id, new IDataSourceListener<Commodity>() {

            @Override
            public void onLoadFinish(Commodity data) {
                if(data == null) {
                    commodityDetailView.showToast(commodityDetailView.getBaseActivity().getString(R.string.unknown_data_error));
                    goBack();
                } else {
                    commodity = data;
                    if (webLoadFinished) {
                        commodityDetailView.updateView(commodity);
                    }
                }
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                commodityDetailView.showToast(errorMessage);
                goBack();
            }
        });
    }

    @Override
    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(phone));
        commodityDetailView.getBaseActivity().startActivity(intent);
    }

    @Override
    public void webLoadFinish() {
        webLoadFinished = true;
        if(commodity != null) {
            commodityDetailView.updateView(commodity);
        }
    }

    @Override
    public boolean handleHook(String url) {
        String functionName = JSHookUtils.getFunctionName(url);
        if (!TextUtils.isEmpty(functionName)) {
            if (JSHookUtils.COMMODITY_PAY_HOOK.equals(functionName)) {
                HashMap<String, String> map = JSHookUtils.getParam(url);
                if (null != map && !map.isEmpty()) {

                }
            }

            return true;
        }

        return false;
    }
}
