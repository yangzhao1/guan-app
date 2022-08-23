package com.submeter.android.dataSource;

import com.android.volley.VolleyError;
import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.view.OnRefreshListener;

/**
 * Created by zhao.bo on 2015/9/10.
 */
public class CategoryDataSource implements INetworkResponseListener, OnRefreshListener {

    private boolean isRefresh = false;

    private int pageNum = 1;

    private int pageSize = NetworkResConstant.LIST_PAGE_SIZE;

    private String userToken;

    private IDataSourceListener dataListener;

    private NetworkResponseListener responseListener;

    public CategoryDataSource(IDataSourceListener dataListener) {
        this.dataListener = dataListener;
        userToken = SubmeterApp.getInstance().getUserToken();
        responseListener = new NetworkResponseListener(this);
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        onLoadMore();
        isRefresh = true;
    }

    @Override
    public void onLoadMore() {
        //ChannelAction.getHomeChannels(pageNum, pageSize, userToken, responseListener);
        isRefresh = false;
    }

    @Override
    public void onResponse(String result) {
        // TODO Auto-generated method stub
        if (null == dataListener) {
            return;
        }

        /*HomeChannelData homeChannelData = null;
        if (!responseListener.handleInnerError(result)) {
            if (!TextUtils.isEmpty(result)) {
                try {
                    homeChannelData = new Gson().fromJson(result, HomeChannelData.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (null != homeChannelData && null != homeChannelData.getTopics() && homeChannelData.getTopics().size() > 0) {
                ++pageNum;

                if(isRefresh) {
                    ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.HOME_CHANNEL_CACHE, result);
                }
            }
        }

        dataListener.onLoadFinish(homeChannelData);*/
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (null == dataListener) {
            return;
        }

        dataListener.onLoadFail(responseListener.getErrorCode(), responseListener.getErrorMessage());
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }
}
