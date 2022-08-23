package com.submeter.android.activity.invitation.presenter;


import com.submeter.android.activity.invitation.params.InvitationParam;

/**
 * @author thm
 * @date 2018/12/6
 */
public interface IInvitationPresenter {

    void loadData(InvitationParam params);

    void refreshData();

    void loadMore();

    boolean isRefresh();

    void getFilterStatusList();

    void getFilterCategoryList();

    void gotoDetails(String id);

}
