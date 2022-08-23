package com.submeter.android.activity.processing.model;

import com.android.volley.VolleyError;
import com.submeter.android.SubmeterApp;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.ProblemAction;

import org.json.JSONObject;

/**
 * Created by  on 2018/12/1.
 */

public class ProModel implements IProModel, INetworkResponseListener {

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener loadDataListener;

    public ProModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void submit(IDataSourceListener listener,JSONObject jsonObject) {
        this.loadDataListener = listener;
        ProblemAction.postViolatorData(jsonObject,SubmeterApp.getInstance().getUserToken(), networkResponseListener);
    }

    @Override
    public void onResponse(String result) {
        if (!networkResponseListener.handleInnerError(result)) {
            loadDataListener.onLoadFinish(null);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(loadDataListener != null) {
            loadDataListener.onLoadFail(networkResponseListener.getErrorCode(), networkResponseListener.getErrorMessage());
        }
    }

}
