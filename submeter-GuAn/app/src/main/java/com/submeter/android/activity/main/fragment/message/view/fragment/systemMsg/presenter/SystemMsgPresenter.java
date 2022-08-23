package com.submeter.android.activity.main.fragment.message.view.fragment.systemMsg.presenter;

import com.submeter.android.activity.main.fragment.message.view.fragment.systemMsg.model.ISystemMsgModel;
import com.submeter.android.activity.main.fragment.message.view.fragment.systemMsg.model.SystemMsgModel;
import com.submeter.android.activity.main.fragment.message.view.fragment.systemMsg.view.ISystemMsgView;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.Notice;
import com.submeter.android.interfacce.IDataSourceListener;

import java.util.List;

/**
 * Created by yangzhao on 2019/4/11.
 */

public class SystemMsgPresenter implements ISystemMsgPresenter,IDataSourceListener<List<Notice>>{

    private ISystemMsgView msgView;
    private ISystemMsgModel model;

    private int pageNum=1;
    private int pageSize= NetworkResConstant.LIST_PAGE_SIZE;

    public SystemMsgPresenter(ISystemMsgView msgView) {
        this.msgView = msgView;
        model = new SystemMsgModel();
    }

    @Override
    public void loadData() {
        msgView.getBaseActivity().showLoadingView();
        model.loadData(this,pageNum,pageSize,"");
    }

    @Override
    public void onLoadFinish(List<Notice> data) {
        msgView.getBaseActivity().hideLoadingView();
//        if (data!=null){
            msgView.updateView(data);
//        }
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        msgView.getBaseActivity().hideLoadingView();
    }

    @Override
    public void onRefresh() {
        model.onRefresh();
    }

    @Override
    public void onLoadMore() {
        model.onLoadMore();
    }

    @Override
    public boolean isRefresh() {
        return model.isRefresh();
    }
}
