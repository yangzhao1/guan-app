package com.submeter.android.activity.invitation.view;

import com.submeter.android.entity.Invitation;
import com.submeter.android.entity.InvitationCategory;
import com.submeter.android.entity.InvitationStatus;
import com.submeter.android.interfacce.IBaseView;

import java.util.List;

/**
 * @author thm
 * @date 2018/12/6
 */
public interface IInvitationView extends IBaseView {

    void updateView(List<Invitation> invitations);

    void notifyNoData();

    void updateFilterStatusView(List<InvitationStatus> invitationStatusList);

    void updateFilterCategoryView(List<InvitationCategory> invitationCategoryList);
}
