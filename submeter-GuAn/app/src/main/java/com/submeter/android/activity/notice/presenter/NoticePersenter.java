package com.submeter.android.activity.notice.presenter;

import com.submeter.android.activity.main.fragment.home.model.HomeModel;
import com.submeter.android.activity.main.fragment.home.model.IHomeModel;
import com.submeter.android.activity.notice.view.INotiveView;
import com.submeter.android.entity.HomeData;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by yangzhao on 2019/4/13.
 */

public class NoticePersenter implements INoticePresenter,IDataSourceListener<HomeData> {

    private INotiveView notiveView;
    private IHomeModel model;
    public NoticePersenter(INotiveView notiveView) {
        this.notiveView = notiveView;
        model = new HomeModel();
    }

    @Override
    public void loadData(int pageNum, int pageSize, String searchContent) {
        notiveView.showLoadingView();
        model.loadData(this);
    }

    @Override
    public void onLoadFinish(HomeData data) {
        notiveView.hideLoadingView();
        if (data!=null){
            notiveView.updateView(data.getNotices());
        }
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        notiveView.hideLoadingView();
    }
}
