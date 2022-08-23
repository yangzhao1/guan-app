package com.submeter.android.activity.invitation.presenter;

import android.content.Intent;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.invitation.model.IInvitationCategoryModel;
import com.submeter.android.activity.invitation.model.IInvitationStatusModel;
import com.submeter.android.activity.invitation.model.InvitationCategoryModel;
import com.submeter.android.activity.invitation.model.InvitationStatusModel;
import com.submeter.android.activity.invitation.params.InvitationParam;
import com.submeter.android.activity.invitation.model.InvitationModel;
import com.submeter.android.activity.invitation.model.IInvitationModel;
import com.submeter.android.activity.invitation.view.IInvitationView;
import com.submeter.android.activity.invitation.view.InvitationDetailsActivity;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.entity.Invitation;
import com.submeter.android.entity.InvitationCategory;
import com.submeter.android.entity.InvitationStatus;
import com.submeter.android.interfacce.IDataSourceListener;

import java.util.List;

/**
 * @author thm
 * @date 2018/12/6
 */
public class InvitationPresenter implements IInvitationPresenter {

    private IInvitationView invitationView;

    private IInvitationModel invitationModel;

    private IInvitationStatusModel invitationStatusModel;

    private IInvitationCategoryModel invitationCategoryModel;

    private boolean isFirstLoad = true;

    public InvitationPresenter(IInvitationView invitationView) {
        this.invitationView = invitationView;
        invitationModel = new InvitationModel();
        invitationStatusModel = new InvitationStatusModel();
        invitationCategoryModel = new InvitationCategoryModel();
    }

    @Override
    public void loadData(InvitationParam params) {
        invitationModel.loadData(params, new IDataSourceListener<List<Invitation>>() {
            @Override
            public void onLoadFinish(List<Invitation> data) {
                if (data != null) {
                    invitationView.updateView(data);
                    isFirstLoad = false;
                } else {
                    invitationView.notifyNoData();
                }
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                if(isFirstLoad) {
                    invitationView.getBaseActivity().finish();
                }
            }
        });
    }

    @Override
    public void refreshData() {
        invitationModel.refreshData();
    }

    @Override
    public void loadMore() {
        invitationModel.loadMore();
    }

    @Override
    public boolean isRefresh() {
        return invitationModel.isRefresh();
    }

    @Override
    public void getFilterStatusList() {
        invitationStatusModel.getFilterStatusList(new IDataSourceListener<List<InvitationStatus>>() {
            @Override
            public void onLoadFinish(List<InvitationStatus> data) {
                invitationView.updateFilterStatusView(data);
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                invitationView.getBaseActivity().finish();
            }
        });
    }

    @Override
    public void getFilterCategoryList() {
        invitationCategoryModel.getFilterCategoryList(new IDataSourceListener<List<InvitationCategory>>() {
            @Override
            public void onLoadFinish(List<InvitationCategory> data) {
                invitationView.updateFilterCategoryView(data);
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                invitationView.getBaseActivity().finish();
            }
        });
    }

    @Override
    public void gotoDetails(String id) {
        BaseActivity activity = invitationView.getBaseActivity();
        Intent intent = new Intent(activity, InvitationDetailsActivity.class);
        intent.putExtra(SystemConstant.INVITATION_KEY, id);
        activity.startActivity(intent);
    }

}
