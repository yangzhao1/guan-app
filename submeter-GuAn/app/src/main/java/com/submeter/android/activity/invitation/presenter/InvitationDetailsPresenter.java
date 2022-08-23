package com.submeter.android.activity.invitation.presenter;

import com.submeter.android.activity.invitation.model.IInvitationDetailsModel;
import com.submeter.android.activity.invitation.model.InvitationDetailsModel;
import com.submeter.android.activity.invitation.view.IInvitationDetailsView;
import com.submeter.android.entity.Invitation;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * @author thm
 * @date 2018/12/6
 */
public class InvitationDetailsPresenter implements IInvitationDetailsPresenter, IDataSourceListener<Invitation> {

    private IInvitationDetailsView invitationDetailsView;

    private IInvitationDetailsModel invitationModel;

    public InvitationDetailsPresenter(IInvitationDetailsView invitationDetailsView) {
        this.invitationDetailsView = invitationDetailsView;
        invitationModel = new InvitationDetailsModel();
    }

    @Override
    public void loadData(String id) {
        invitationModel.loadData(id, this);
    }

    @Override
    public void onLoadFinish(Invitation invitation) {
        invitationDetailsView.updateView(invitation);
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        invitationDetailsView.getBaseActivity().finish();
    }
}
