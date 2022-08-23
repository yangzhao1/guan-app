package com.submeter.android.activity.main.fragment.me.view;

import com.submeter.android.entity.User;
import com.submeter.android.interfacce.IBaseView;

/**
 * Created by 赵勃 on 2018/11/22.
 */

public interface IMeView extends IBaseView {

    /*刷新用户信息*/
    public void refreshUserInfo(User user);

    void refreshHeadImage(String headImage);
}
