package com.submeter.android.activity.main.fragment.home.view;

import com.submeter.android.interfacce.IBaseView;

/**
 * Created by 赵勃 on 2018/11/22.
 */

public interface IHomeView<T> extends IBaseView {
    public static final int HOT_ACTIVITIES = 1;

    public static final int RECOMMEND_BRAND = 2;

    public static final int GOOD = 3;

    public void updateView(T data);

    void updateWeather(T data);

    public void loginStatusChanged(boolean loginStatus);

    public void showCurrentLocation(String location);

}
