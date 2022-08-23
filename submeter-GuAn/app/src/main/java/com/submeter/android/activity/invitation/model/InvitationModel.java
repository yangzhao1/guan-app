package com.submeter.android.activity.invitation.model;

import com.submeter.android.activity.invitation.params.InvitationParam;
import com.submeter.android.dataSource.InvitationDataSource;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * @author thm
 * @date 2018/12/6
 */
public class InvitationModel implements IInvitationModel {

    private InvitationDataSource invitationDataSource;

    @Override
    public void loadData(InvitationParam params,IDataSourceListener listener) {
        invitationDataSource = new InvitationDataSource(listener);
        invitationDataSource.initData(params);
    }

    @Override
    public void refreshData() {
        invitationDataSource.onRefresh();
    }

    @Override
    public void loadMore() {
        invitationDataSource.onLoadMore();
    }

    @Override
    public boolean isRefresh(){
        return invitationDataSource.isRefresh();
    }

}
