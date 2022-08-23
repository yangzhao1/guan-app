package com.submeter.android.activity.invitation.model;

import com.submeter.android.activity.invitation.params.InvitationParam;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * @author thm
 * @date 2018/12/6
 */
public interface IInvitationModel {

    void loadData(InvitationParam params,IDataSourceListener listener);

    void refreshData();

    void loadMore();

    boolean isRefresh();

}
