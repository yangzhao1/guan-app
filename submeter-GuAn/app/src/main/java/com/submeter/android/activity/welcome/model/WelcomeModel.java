package com.submeter.android.activity.welcome.model;

import com.android.volley.VolleyError;
import com.submeter.android.entity.Banner;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public class WelcomeModel implements IWelcomeModel, INetworkResponseListener {

    private NetworkResponseListener responseListener;

    private IDataSourceListener dataSourceListener;

    private Banner banner;

    public WelcomeModel() {
        responseListener = new NetworkResponseListener(this);
    }

    @Override
    public Banner getBanner() {
        return null;
    }

    @Override
    public void onResponse(String result) {
        if (!responseListener.handleInnerError(result)) {
            //parse data in to WelcomeModel
            //Banner banner = JSON.parseObject(result, new TypeReference<Banner>() {});
        }

        if(null != dataSourceListener) {
            dataSourceListener.onLoadFinish(this);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(null != dataSourceListener) {
            dataSourceListener.onLoadFinish(error);
        }
    }

    @Override
    public void loadData(IDataSourceListener dataSourceListener) {
        this.dataSourceListener = dataSourceListener;

        //获取banner接口
        /*bannerNetworkResponse = new NetworkResponseListener(bannerResponseListener);
            SystemAction.getBanners(MeetownApp.getInstance().getUserToken(), Banner.BANNER_START_PAGE, 1, bannerNetworkResponse);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    synchronized (showMainViewObj) {
                        if (!showed) {
                            showed = true;
                            startMainApp(null);
                        }
                    }
                }
            }, 4000);*/
    }
}
