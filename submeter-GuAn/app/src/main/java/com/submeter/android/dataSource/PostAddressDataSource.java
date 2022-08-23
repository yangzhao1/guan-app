package com.submeter.android.dataSource;

import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.view.OnRefreshListener;

/**
 * Created by zhao.bo on 2015/9/10.
 */
public class PostAddressDataSource implements OnRefreshListener {
    private boolean cancelled = false;

    private boolean isRefresh = false;

    private String userId;

    private IDataSourceListener dataListener;

    public PostAddressDataSource(IDataSourceListener dataListener, String userId) {
        this.dataListener = dataListener;
        this.userId = userId;
    }

    @Override
    public void onRefresh() {
        onLoadMore();
        isRefresh = true;
    }

    @Override
    public void onLoadMore() {
        //OuraltApp.getInstance().getPostAddressArrayList();
        dataListener.onLoadFinish(null);
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }
}
