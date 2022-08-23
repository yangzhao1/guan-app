package com.submeter.android.activity.main.fragment.me.presenter;

import android.content.Intent;
import android.content.pm.PackageInfo;

import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.login.view.LoginActivity;
import com.submeter.android.activity.main.fragment.me.model.IMeModel;
import com.submeter.android.activity.main.fragment.me.model.MeModel;
import com.submeter.android.activity.main.fragment.me.view.IMeView;
import com.submeter.android.entity.Profile;
import com.submeter.android.entity.User;
import com.submeter.android.eventbus.MessageEvent;
import com.submeter.android.interfacce.IDataSourceListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Map;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public class MePresenter implements IMePresenter,IDataSourceListener<Map<String,String>> {

    IMeView meView;

    IMeModel meModel;

    public MePresenter(IMeView meView) {
        this.meView = meView;

        meModel = new MeModel();

        EventBus.getDefault().register(this);
    }

    @Override
    public void loadUserInfo() {
        User user = meModel.loadUserInfo();
        meView.refreshUserInfo(user);
    }

    @Override
    public void goSetting() {
        BaseActivity activity = meView.getBaseActivity();
        /*Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);*/
    }

    @Override
    public void goLogin() {
        BaseActivity activity = meView.getBaseActivity();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void refreshUserInfo(User user) {
        meView.refreshUserInfo(user);
    }

    @Override
    public void getNewVersions() {
        meModel.getNewVersions(this);
    }

    @Override
    public void uploadHeadImage(File file) {
        meModel.uploadHeadImage(file);
    }

    /*
     * finish the splash page
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeWelcome(MessageEvent messageEvent) {
        if(messageEvent.getCode() == MessageEvent.SIGN_IN_SUCCESS) {
            Profile profile = SubmeterApp.getInstance().getCurrentProfile();
            if(profile != null) {
                meView.refreshUserInfo(profile.getUser());
            } else {
                meView.refreshUserInfo(null);
            }
        } else if(messageEvent.getCode() == MessageEvent.SIGNOUT_SUCCESS) {
            meView.refreshUserInfo(null);
        }
    }

    @Override
    public void onLoadFinish(Map<String,String> data) {
        if (data!=null){
            String newVersions = data.get("codeVersions");
            String url = data.get("url");
            String oldVersionCode = getOldVersionCode();
            double oldverCode = Double.parseDouble(oldVersionCode);
            double newVerCode = Double.parseDouble(newVersions);
            if (newVerCode > oldverCode) {
                String updateInfo = "当前版本号:" + oldverCode
                        + ", 发现新版本号：" + newVerCode + ",是否更新?";
                meView.getBaseActivity().showNoticeDialog(updateInfo,url);
            } else {
                meView.getBaseActivity().showToast("暂无更新！");
            }
        }else {
            meView.getBaseActivity().showToast("暂无更新！");
        }
    }

    public String getOldVersionCode() {
        String versionName = null;
        try {
            PackageInfo info = meView.getBaseActivity().getPackageManager().getPackageInfo(
                    meView.getBaseActivity().getPackageName(), 0);
            versionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {

    }
}
