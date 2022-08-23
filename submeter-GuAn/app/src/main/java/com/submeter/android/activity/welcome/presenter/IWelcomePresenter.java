package com.submeter.android.activity.welcome.presenter;

import com.submeter.android.tools.PermissionUtils;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public interface IWelcomePresenter {

    public boolean needUpdateGPS();

    public void requestGPSPermission(PermissionUtils.IPermissionRequestCallback permissionRequestCallback);

}
