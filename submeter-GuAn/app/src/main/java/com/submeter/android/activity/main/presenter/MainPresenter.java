package com.submeter.android.activity.main.presenter;

import android.content.Context;

import com.android.volley.VolleyError;
import com.submeter.android.activity.main.model.IMainModel;
import com.submeter.android.activity.main.model.MainModel;
import com.submeter.android.activity.main.view.IMainView;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.tools.NotificationTools;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public class MainPresenter implements IMainPresenter, INetworkResponseListener {

    private IMainView mainView;

    private IMainModel mainModel;

    private NetworkResponseListener checkNewVersionNetworkResponse;

    public MainPresenter(IMainView mainView) {
        this.mainView = mainView;

        mainModel = new MainModel();
    }

    @Override
    public void checkNewVersion() {
        checkNewVersionNetworkResponse = new NetworkResponseListener(this);
        //SystemAction.getNewestVersion(OuraltApp.getInstance().getUserToken(), checkNewVersionNetworkResponse);
    }

    @Override
    public void checkNotificationStatus() {
        if(NotificationTools.needCheckNotificationAgain) {
            NotificationTools.needCheckNotificationAgain = false;
            checkNotificationOpened();
        }
    }

    private void checkNotificationOpened() {
        Context context = mainView.getActivity();

        boolean enable = NotificationTools.notificationEnabled(context);
        if (!enable && NotificationTools.needShowNotification()) {
            mainView.showOpenSettingView();
        } else if (enable && NotificationTools.notificationShowed()) {
            mainView.closeNotificationView();
        }
    }

    @Override
    public void onResponse(String result) {
        handleCheckNewVersionResult(result);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        checkNotificationOpened();
    }

    private void handleCheckNewVersionResult(String result) {
        /*JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject != null) {
            int errorCode = jsonObject.getInteger("code");
            if (errorCode == 0) {
                JSONObject versionObj = jsonObject.getJSONObject("versionInfo");
                if (null != versionObj) {
                    String versionCode = versionObj.getString("version");
                    String updateDetail = versionObj.getString("description");
                    String apkUrl = versionObj.getString("androidUrl");
                    String forceUpgrade = jsonObject.getString("forceUpgrade");
                    if("Y".equalsIgnoreCase(forceUpgrade)) {
                        mainView.showUpgradeDialog(updateDetail, apkUrl, versionCode, true);

                        return;
                    } else {
                        String version = ShareStoreProcess.getInstance().getDataByKey(DBConstant.UPGRADE_VERSION);
                        String time = ShareStoreProcess.getInstance().getDataByKey(DBConstant.UPGRADE_TIME);
                        long timeSep = 259200000; //ic_common_sort_default * 24 * 60 * 60 * 1000 3days
                        if (Utils.compareVersion(versionCode, Utils.getAppVersionName(mainView.getActivity())) > 0
                                && (!version.equalsIgnoreCase(versionCode) || TextUtils.isEmpty(time)
                                || System.currentTimeMillis() - Long.parseLong(time) >= timeSep)) {
                            mainView.showUpgradeDialog(updateDetail, apkUrl, versionCode, false);

                            return;
                        }
                    }
                }
            }
        }*/

        checkNotificationOpened();
    }
}
