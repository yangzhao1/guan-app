package com.submeter.android.activity.main.fragment.me.model;

import com.submeter.android.entity.User;
import com.submeter.android.interfacce.IDataSourceListener;

import java.io.File;
import java.util.Map;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public interface IMeModel {
    public User loadUserInfo();

    void getNewVersions(IDataSourceListener<Map<String,String>> mapIDataSourceListener);

    void uploadHeadImage(File file);
}
