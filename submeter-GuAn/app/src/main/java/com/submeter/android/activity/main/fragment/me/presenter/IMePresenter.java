package com.submeter.android.activity.main.fragment.me.presenter;

import com.submeter.android.entity.User;

import java.io.File;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public interface IMePresenter {

    /*加载用户信息*/
    public void loadUserInfo();

    /*点击设置*/
    public void goSetting();

    /*点击登陆*/
    public void goLogin();

    /*刷新用户信息*/
    public void refreshUserInfo(User user);

    /**
     * 检查版本
     */
    void getNewVersions();

    void uploadHeadImage(File file);
}
