package com.submeter.android.activity.commodityList.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.BaseResponse;
import com.submeter.android.entity.ProductFrontCategory;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.CommodityListAction;

import java.util.List;

/**
 * @author thm
 * @date 2018/12/13
 */
public class CommodityListCategoryModel implements ICommodityListCategoryModel, INetworkResponseListener {

    private String userToken;

    private NetworkResponseListener responseListener;

    private IDataSourceListener<List<ProductFrontCategory>> dataListener;

    public CommodityListCategoryModel() {
        userToken = SubmeterApp.getInstance().getUserToken();
        responseListener = new NetworkResponseListener(this);
    }

    @Override
    public void getFilterCategoryList(IDataSourceListener<List<ProductFrontCategory>> listener) {
        dataListener = listener;
        CommodityListAction.getFilterCategoryList(userToken, responseListener);
    }

    @Override
    public void onResponse(String result) {
        if (null == dataListener) {
            return;
        }
        BaseResponse<List<ProductFrontCategory>> baseResponse = new Gson().fromJson(result, new TypeToken<BaseResponse<List<ProductFrontCategory>>>() {}.getType());
        List<ProductFrontCategory> invitationCategoryList = responseListener.handleInnerError(baseResponse);
        dataListener.onLoadFinish(invitationCategoryList);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
