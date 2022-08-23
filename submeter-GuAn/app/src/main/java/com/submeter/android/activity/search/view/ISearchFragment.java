package com.submeter.android.activity.search.view;

import android.support.v4.app.Fragment;

import com.submeter.android.activity.search.presenter.ISearchPresenter;

import java.util.List;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface ISearchFragment {
    public Fragment getFragment();

    public void setPresenter(ISearchPresenter searchPresenter);

    public void updateView(List<String> searchHistory);
}
