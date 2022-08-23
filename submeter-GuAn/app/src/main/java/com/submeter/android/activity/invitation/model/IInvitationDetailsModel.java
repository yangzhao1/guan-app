package com.submeter.android.activity.invitation.model;

import com.submeter.android.entity.Invitation;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * @author thm
 * @date 2018/12/6
 */
public interface IInvitationDetailsModel {

    void loadData(String id, IDataSourceListener<Invitation> listener);
}
