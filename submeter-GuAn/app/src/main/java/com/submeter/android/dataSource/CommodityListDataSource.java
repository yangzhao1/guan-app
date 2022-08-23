package com.submeter.android.dataSource;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.commodityList.params.CommodityListParam;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.BaseResponse;
import com.submeter.android.entity.Commodity;
import com.submeter.android.entity.PageData;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.CommodityListAction;
import com.submeter.android.view.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author thm
 * @date 2018/12/13
 */
public class CommodityListDataSource implements INetworkResponseListener, OnRefreshListener {
    /**
     * 请求对象
     */
    private CommodityListParam params;

    private String userToken;

    private boolean isRefresh = false;
    /**
     * 当前是第几页
     */
    private int currentPageNum = 1;

    /**
     * 总页数
     */
    private int totalPageNum = 1;
    /**
     * 每页显示条数
     */
    private int pageSize = NetworkResConstant.LIST_PAGE_SIZE;
    /**
     * 商品列表
     */
    private List<Commodity> commodityDataList = new ArrayList<>();

    private IDataSourceListener<List<Commodity>> dataListener;

    private NetworkResponseListener responseListener;

    public CommodityListDataSource(IDataSourceListener<List<Commodity>> dataListener) {
        this.dataListener = dataListener;
        userToken = SubmeterApp.getInstance().getUserToken();
        responseListener = new NetworkResponseListener(this);
    }

    @Override
    public void onRefresh() {
        currentPageNum = 1;
        loadData();
        isRefresh = true;
    }

    @Override
    public void onLoadMore() {
        currentPageNum++;
        if(currentPageNum > totalPageNum){
            dataListener.onLoadFinish(null);
        }else{
            loadData();
        }
        isRefresh = false;
    }

    public void initData(CommodityListParam params) {
        this.params = params;
        loadData();
    }

    private void loadData() {
        CommodityListAction.getCommodityList(params, userToken, currentPageNum, pageSize, responseListener);
    }

    @Override
    public void onResponse(String result) {
        if (null == dataListener) {
            return;
        }
        BaseResponse<PageData<Commodity>> baseResponse = new Gson().fromJson(result, new TypeToken<BaseResponse<PageData<Commodity>>>() {}.getType());
        PageData<Commodity> pageDataList = responseListener.handleInnerError(baseResponse);
        if(pageDataList != null) {
            currentPageNum = pageDataList.getCurrPage();
            totalPageNum = pageDataList.getTotalPage();
            List<Commodity> temps = pageDataList.getList();
            if(temps != null && !temps.isEmpty()) {
                if(isRefresh) {
                    commodityDataList.clear();
                }
                commodityDataList.addAll(temps);
            }
        }
        dataListener.onLoadFinish(commodityDataList);
        isRefresh = false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (null == dataListener) {
            return;
        }
        dataListener.onLoadFail(responseListener.getErrorCode(), responseListener.getErrorMessage());
        isRefresh = false;
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }

}
