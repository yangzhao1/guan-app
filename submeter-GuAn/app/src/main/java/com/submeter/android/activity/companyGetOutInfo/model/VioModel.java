package com.submeter.android.activity.companyGetOutInfo.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.ViolatorData;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.ProblemAction;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by  on 2018/12/1.
 */

public class VioModel implements IVioModel, INetworkResponseListener {

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener loadDataListener;
    private boolean isRefresh = false;
    /**
     * 当前是第几页
     */
    private int currentPageNum = 1;

    /**
     * 总页数
     */
    private int totalPageNum = 1;
    private int pageSize = NetworkResConstant.LIST_PAGE_SIZE;

    public VioModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(IDataSourceListener listener,String id) {
        this.loadDataListener = listener;
        ProblemAction.getViolatorInfo(id,SubmeterApp.getInstance().getUserToken(), networkResponseListener);
    }

    @Override
    public void onResponse(String result) {
        if (!networkResponseListener.handleInnerError(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject object = jsonObject.getJSONObject("data");
                ViolatorData violatorData = new Gson().fromJson(object.toString(),new TypeToken<ViolatorData>() {}.getType());
                if (violatorData!=null){
                    loadDataListener.onLoadFinish(violatorData);
                }else {
                    loadDataListener.onLoadFinish(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                loadDataListener.onLoadFinish(null);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(loadDataListener != null) {
            loadDataListener.onLoadFail(networkResponseListener.getErrorCode(), networkResponseListener.getErrorMessage());
        }
        isRefresh = false;
    }

    @Override
    public void onRefresh() {
        currentPageNum=1;
//        loadData(loadDataListener,isHandle);
        isRefresh = true;
    }

    @Override
    public void onLoadMore() {
        currentPageNum++;
        if (currentPageNum>totalPageNum){
            loadDataListener.onLoadFinish(null);
        }else {
//            loadData(loadDataListener,isHandle);
        }
        isRefresh = false;
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }
}
