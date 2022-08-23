package com.submeter.android.activity.welcome.presenter;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.submeter.android.activity.welcome.model.IWelcomeModel;
import com.submeter.android.activity.welcome.model.WelcomeModel;
import com.submeter.android.activity.welcome.view.IWelcomeView;
import com.submeter.android.constants.DBConstant;
import com.submeter.android.db.ShareStoreProcess;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.tools.PermissionUtils;

import java.util.ArrayList;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public class WelcomePresenter implements IWelcomePresenter, IDataSourceListener {

    //welcome's data
    private IWelcomeModel mWelcomeModel;

    private IWelcomeView mWelcomeView;

    public WelcomePresenter(IWelcomeView welcomeView) {
        this.mWelcomeView = welcomeView;

        //new IWelcomeModel
        mWelcomeModel = new WelcomeModel();
        mWelcomeModel.loadData(this);
    }

    @Override
    public boolean needUpdateGPS() {
        String gpsCacheTime = ShareStoreProcess.getInstance().getDataByKey(DBConstant.CURRENT_LOCATION_TIME);
        if (TextUtils.isEmpty(gpsCacheTime)
                || System.currentTimeMillis() - Long.parseLong(gpsCacheTime) > 300000) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void requestGPSPermission(PermissionUtils.IPermissionRequestCallback permissionRequestCallback) {
        ArrayList<String> requestPermission = new ArrayList<String>();
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_SETTINGS
        };
        Activity activity = mWelcomeView.getActivity();
        for (String permission : permissions) {
            if (!PermissionUtils.enablePermission(activity, permission)) {
                //用intent启动拨打电话
                requestPermission.add(permission);
            }
        }

        if (requestPermission.size() > 0) {
            String[] requestPermissionArray = new String[requestPermission.size()];
            requestPermission.toArray(requestPermissionArray);
            ActivityCompat.requestPermissions(activity, requestPermissionArray, PermissionUtils.REQUEST_LOCATION_PERMISSION);
        } else {
            permissionRequestCallback.permissionGranted(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onLoadFinish(Object data) {
        //update view
        mWelcomeView.updateView(mWelcomeModel.getBanner());
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {

    }
}
