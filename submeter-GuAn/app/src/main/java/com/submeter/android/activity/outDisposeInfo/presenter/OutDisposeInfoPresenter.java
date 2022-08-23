package com.submeter.android.activity.outDisposeInfo.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.outDisposeInfo.view.IODInfoView;
import com.submeter.android.entity.OutDisposeData;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.ProblemAction;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangzhao on 2019/6/17.
 */

public class OutDisposeInfoPresenter implements IOutDInfoPresenter,INetworkResponseListener{

    private IODInfoView iodInfoView;
    private NetworkResponseListener listener;
    public OutDisposeInfoPresenter(IODInfoView iodInfoView) {
        this.iodInfoView = iodInfoView;
        listener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(String id) {
        iodInfoView.getBaseActivity().showLoadingView();
        ProblemAction.getDisposeInfo(id, SubmeterApp.getInstance().getUserToken(),listener);
    }

    @Override
    public void onResponse(String result) {
        iodInfoView.getBaseActivity().hideLoadingView();
        if (!listener.handleInnerError(result)) {
            try {
                JSONObject object = new JSONObject(result);
                JSONObject dataO = object.getJSONObject("violationReusultEntity");
                if (dataO!=null){
                    OutDisposeData data = new Gson().fromJson(dataO.toString(),OutDisposeData.class);
                    iodInfoView.updateView(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        iodInfoView.getBaseActivity().hideLoadingView();
        iodInfoView.getBaseActivity().showToast(listener.getErrorMessage());
    }
}
