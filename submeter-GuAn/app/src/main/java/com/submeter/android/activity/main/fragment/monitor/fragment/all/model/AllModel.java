package com.submeter.android.activity.main.fragment.monitor.fragment.all.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.Company;
import com.submeter.android.entity.Monitor;
import com.submeter.android.entity.PageData;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.MonitorAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by  on 2018/12/1.
 */

public class AllModel implements IAllModel, INetworkResponseListener {

    private Monitor monitor;

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener<Monitor> loadDataListener;

    public AllModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(String search,IDataSourceListener<Monitor> listener,String sort) {
        this.loadDataListener = listener;
        MonitorAction.getCompanyList(search,sort,SubmeterApp.getInstance().getUserToken(),"1","1000", networkResponseListener);
    }

    @Override
    public void gotoCompanyInfo(String markerId) {
//        MonitorAction.getCompanyInfo(SubmeterApp.getInstance().getUserToken(),networkResponseListener);
    }

    @Override
    public void onResponse(String result) {
        List<Company> list = null;
        if (!networkResponseListener.handleInnerError(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject object = jsonObject.getJSONObject("data");
                PageData<Company> pageData = new Gson().fromJson(object.toString(),new TypeToken<PageData<Company>>() {}.getType());
                if (pageData!=null&&pageData.getList()!=null){
                    list = pageData.getList();
                    Monitor monitor = new Monitor();
                    monitor.setCompanies(list);
                    loadDataListener.onLoadFinish(monitor);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Monitor monitor = new Monitor();
                monitor.setCompanies(null);
                loadDataListener.onLoadFinish(monitor);
            }
        }else {
            Monitor monitor = new Monitor();
            monitor.setCompanies(null);
            loadDataListener.onLoadFinish(monitor);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(loadDataListener != null) {
            loadDataListener.onLoadFail(networkResponseListener.getErrorCode(), networkResponseListener.getErrorMessage());
        }
    }
}
