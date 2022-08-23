package com.submeter.android.activity.search.presenter;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface ISearchPresenter {

    public void loadSearchHistory(int historyType);

    public void search(int historyType, String keyword);

    public void deleteHistory(int historyType);

    public void goBack();
}
