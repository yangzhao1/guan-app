package com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.presenter;

import com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.model.DisposeMsgModel;
import com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.model.IDisposeMsgModel;
import com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.view.IDisposeMsgView;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.MessageData;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by yangzhao on 2019/4/11.
 */

public class DisposeMsgPresenter implements IDisposeMsgPresenter{

    private IDisposeMsgView msgView;
    private IDisposeMsgModel model;

    private int pageNum=1;
    private int pageSize= NetworkResConstant.LIST_PAGE_SIZE;

    public DisposeMsgPresenter(IDisposeMsgView msgView) {
        this.msgView = msgView;
        model = new DisposeMsgModel();
    }

    @Override
    public void loadData() {
        msgView.getBaseActivity().showLoadingView();
        model.loadData(new IDataSourceListener<MessageData>() {
            @Override
            public void onLoadFinish(MessageData data) {
                msgView.getBaseActivity().hideLoadingView();
                if (data!=null){
                    msgView.updateView(data.getMessages());
                }else {
                    msgView.updateView(null);
                }
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                msgView.getBaseActivity().hideLoadingView();
            }
        },pageNum,pageSize,"");
    }

    @Override
    public void onRefresh() {
        model.refreshData();
    }

    @Override
    public void onLoadMore() {
        model.loadMoreData();
    }

    @Override
    public boolean isRefresh() {
        return model.isRefresh();
    }
}
