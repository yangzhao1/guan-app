package com.submeter.android.activity.invitation.model;

import com.submeter.android.entity.InvitationStatus;
import com.submeter.android.interfacce.IDataSourceListener;

import java.util.List;

/**
 * @author thm
 * @date 2018/12/6
 */
public interface IInvitationStatusModel {

    void getFilterStatusList(IDataSourceListener<List<InvitationStatus>> listener);
}
