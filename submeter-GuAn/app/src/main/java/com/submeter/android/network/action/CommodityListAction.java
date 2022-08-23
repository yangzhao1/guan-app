package com.submeter.android.network.action;

import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.submeter.android.activity.commodityList.params.CommodityListParam;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.network.MyNetworkRequest;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.network.NetworkResponseListener;

import java.util.HashMap;

/**
 * @author thm
 * @date 2018/12/13
 */
public class CommodityListAction {

    /**
     * 获取商品信息列表
     *
     * @param params           封装请求参数
     * @param token            用户Id
     * @param pageNum          当前页数
     * @param pageSize         每页数量
     * @param responseListener 接口回调
     */
    public static void getCommodityList(CommodityListParam params,
                                        String token,
                                        int pageNum,
                                        int pageSize,
                                        NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if (!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }
        keyValue.put("pageNum", pageNum);
        keyValue.put("pageSize", pageSize);

        if (params != null) {
            if (!TextUtils.isEmpty(params.getKey())) {
                keyValue.put("key", params.getKey());
            }
            if (!TextUtils.isEmpty(params.getProductFrontCategoryId())) {
                keyValue.put("productFrontCategoryId", params.getProductFrontCategoryId());
            }
            if (!TextUtils.isEmpty(params.getStoreId())) {
                keyValue.put("storeId", params.getStoreId());
            }
            if (!TextUtils.isEmpty(params.getBrandId())) {
                keyValue.put("brandId", params.getBrandId());
            }
            if (!TextUtils.isEmpty(params.getIntegratedSort())) {
                keyValue.put("integratedSort", params.getIntegratedSort());
            }
            if (!TextUtils.isEmpty(params.getPriceSort())) {
                keyValue.put("priceSort", params.getPriceSort());
            }
            if (!TextUtils.isEmpty(params.getSalesSort())) {
                keyValue.put("salesSort", params.getSalesSort());
            }
            if (!TextUtils.isEmpty(params.getLowMum())) {
                keyValue.put("lowMum", params.getLowMum());
            }
            if (!TextUtils.isEmpty(params.getTopMum())) {
                keyValue.put("topMum", params.getTopMum());
            }
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_SEARCHPRODUCT);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }


    /**
     * 获取前台采购类别列表
     *
     * @param token            用户token
     * @param responseListener 接口回调
     */
    public static void getFilterCategoryList(String token,
                                             NetworkResponseListener responseListener) {

        HashMap<String, Object> keyValue = new HashMap<>();

        if (!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_PRODUCTFRONTCATEGORY_SEARCH);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    /**
     * 获取广告位列表
     *
     * @param token            用户token
     * @param responseListener 接口回调
     */
    public static void getBannerList(String token,
                                     NetworkResponseListener responseListener) {

        HashMap<String, Object> keyValue = new HashMap<>();

        if (!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }
        keyValue.put("adPositionId", 321);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.GET_AD_SEARCH);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

}