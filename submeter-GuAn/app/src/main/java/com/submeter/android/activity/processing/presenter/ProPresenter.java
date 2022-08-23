package com.submeter.android.activity.processing.presenter;

import com.submeter.android.activity.processing.model.ProModel;
import com.submeter.android.activity.processing.view.IProView;
import com.submeter.android.interfacce.IDataSourceListener;

import org.json.JSONObject;

/**
 * Created by 赵勃 on 2018/12/1.
 */

public class ProPresenter implements IProPresenter, IDataSourceListener {
    private IProView proView;
    private ProModel model;

    public ProPresenter(IProView proView) {
        this.proView = proView;
        model = new ProModel();
    }

    @Override
    public void onLoadFinish(Object data) {
        proView.getBaseActivity().hideLoadingView();
        proView.updateView();
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        proView.getBaseActivity().hideLoadingView();
    }

    @Override
    public void submit(JSONObject object) {
        model.submit(this,object);
    }
}
