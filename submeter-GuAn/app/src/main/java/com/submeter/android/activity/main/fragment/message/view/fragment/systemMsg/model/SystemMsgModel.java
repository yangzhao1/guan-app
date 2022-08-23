package com.submeter.android.activity.main.fragment.message.view.fragment.systemMsg.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.Notice;
import com.submeter.android.entity.PageData;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.MessageAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzhao on 2019/4/11.
 */

public class SystemMsgModel implements ISystemMsgModel,INetworkResponseListener{

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener<List<Notice>> dataIDataSourceListener;


    /**
     * 当前是第几页
     */
    private int currentPageNum = 1;

    /**
     * 总页数
     */
    private int totalPageNum = 1;
    private int pageSize = NetworkResConstant.LIST_PAGE_SIZE;
    private boolean isRefresh = false;

    /**
     * 列表
     */
    List<Notice> notices = new ArrayList<>();

    public SystemMsgModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void onResponse(String result) {
        PageData<Notice> pageData = null;
        if (!networkResponseListener.handleInnerError(result)){
            try {
                JSONObject resultObj = new JSONObject(result);
                JSONObject dataObj = resultObj.getJSONObject("data");
                pageData = new Gson().fromJson(dataObj.toString(),new TypeToken<PageData<Notice>>(){}.getType());
                if (pageData!=null){
                    if (isRefresh){
                        notices.clear();
                    }
                    currentPageNum = pageData.getCurrPage();
                    totalPageNum = pageData.getTotalPage();
                    List<Notice> temps = pageData.getList();
                    if(temps != null && !temps.isEmpty()) {
                        notices.addAll(temps);
                    }
                    dataIDataSourceListener.onLoadFinish(notices);
                }else {
                    dataIDataSourceListener.onLoadFinish(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dataIDataSourceListener.onLoadFinish(null);
            }
        }else {
            dataIDataSourceListener.onLoadFinish(null);
        }
        isRefresh = false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dataIDataSourceListener!=null){
            dataIDataSourceListener.onLoadFail(networkResponseListener.getErrorCode(),networkResponseListener.getErrorMessage());
        }
        isRefresh = false;

    }

    @Override
    public void loadData(IDataSourceListener<List<Notice>> listener, int pageNum, int pageSize, String search) {
        this.dataIDataSourceListener = listener;
        MessageAction.getSysMsgData(SubmeterApp.getInstance().getUserToken(),pageNum,pageSize,search,networkResponseListener);

    }

    @Override
    public void onRefresh() {
        currentPageNum=1;
        loadData(dataIDataSourceListener,currentPageNum,pageSize,"");
        isRefresh = true;
    }

    @Override
    public void onLoadMore() {
        currentPageNum++;
        if (currentPageNum>totalPageNum){
            dataIDataSourceListener.onLoadFinish(null);
        }else {
            loadData(dataIDataSourceListener,currentPageNum,pageSize,"");
        }
        isRefresh = false;
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }
}
