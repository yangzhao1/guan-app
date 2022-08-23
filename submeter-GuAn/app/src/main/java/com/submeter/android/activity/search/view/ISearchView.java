package com.submeter.android.activity.search.view;

import com.submeter.android.interfacce.IBaseView;

import java.util.List;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface ISearchView extends IBaseView {
    public static final int COMMODITY_TAB = 0;

    public static final int BRAND_TAB = 1;

    public void updateView(int tabView, List<String> listData);
}
