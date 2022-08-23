package com.submeter.android.activity.historyData.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.Power;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.ProblemAction;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by  on 2018/12/1.
 */

public class HistoryDataModel implements IHistoryDataModel, INetworkResponseListener {

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener loadDataListener;

    public HistoryDataModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(IDataSourceListener listener,int id,String currentTime) {
        this.loadDataListener = listener;
        ProblemAction.getCompanyPowerList(id,currentTime,SubmeterApp.getInstance().getUserToken(), networkResponseListener);
    }

    @Override
    public void onResponse(String result) {
        if (!networkResponseListener.handleInnerError(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
//                JSONObject object = jsonObject.getJSONObject("data");
                Power data = new Gson().fromJson(result,new TypeToken<Power>() {}.getType());
                if (data!=null){
                    loadDataListener.onLoadFinish(data);
                }else {
                    loadDataListener.onLoadFinish(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                loadDataListener.onLoadFinish(null);
            }
        }else {
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
