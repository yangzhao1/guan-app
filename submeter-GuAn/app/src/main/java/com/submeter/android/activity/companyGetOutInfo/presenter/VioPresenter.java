package com.submeter.android.activity.companyGetOutInfo.presenter;

import com.submeter.android.activity.companyGetOutInfo.model.VioModel;
import com.submeter.android.activity.companyGetOutInfo.view.IVioView;
import com.submeter.android.entity.ViolatorData;
import com.submeter.android.interfacce.IDataSourceListener;

/**
 * Created by 2 on 2018/12/1.
 */

public class VioPresenter implements IVioPresenter, IDataSourceListener<ViolatorData> {
    private IVioView vioView;
    private VioModel model;

    public VioPresenter(IVioView vioView) {
        this.vioView = vioView;
        model = new VioModel();
    }

    @Override
    public void loadData(String id) {
        vioView.getBaseActivity().showLoadingView();
        model.loadData(this,id);
    }

    @Override
    public void onLoadFinish(ViolatorData data) {
        vioView.getBaseActivity().hideLoadingView();
        vioView.updateView(data);
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        vioView.getBaseActivity().hideLoadingView();
    }
}
