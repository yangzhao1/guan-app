package com.submeter.android.activity.main.view;

import com.submeter.android.activity.BaseActivity;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public interface IMainView {

    public BaseActivity getActivity();

    public void showOpenSettingView();

    public void closeNotificationView();

    public void showUpgradeDialog(String updateDetail, String apkUrl, String versionCode, boolean forceUpgrade);
}
