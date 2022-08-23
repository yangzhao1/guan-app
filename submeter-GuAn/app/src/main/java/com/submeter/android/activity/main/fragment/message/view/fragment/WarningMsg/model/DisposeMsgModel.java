package com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.MessageData;
import com.submeter.android.entity.PageData;
import com.submeter.android.entity.WarningMessage;
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

public class DisposeMsgModel implements IDisposeMsgModel,INetworkResponseListener{

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener<MessageData> dataIDataSourceListener;

    /**
     * 当前是第几页
     */
    private int currentPageNum = 1;

    /**
     * 总页数
     */
    private int totalPageNum = 1;
    private int pageSize = NetworkResConstant.LIST_PAGE_SIZE;
    private MessageData messageData = new MessageData();
    private boolean isRefresh = false;
    /**
     * 列表
     */
    List<WarningMessage> warningMessages = new ArrayList<>();
    public DisposeMsgModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void onResponse(String result) {
        PageData<WarningMessage> pageData = null;
        if (!networkResponseListener.handleInnerError(result)){
            try {
                JSONObject resultObj = new JSONObject(result);
                JSONObject dataObj = resultObj.getJSONObject("data");
                pageData = new Gson().fromJson(dataObj.toString(),new TypeToken<PageData<WarningMessage>>(){}.getType());
                if (pageData!=null){
                    if (isRefresh){
                        warningMessages.clear();
                        messageData = new MessageData();
                    }
                    currentPageNum = pageData.getCurrPage();
                    totalPageNum = pageData.getTotalPage();
                    List<WarningMessage> temps = pageData.getList();
                    if(temps != null && !temps.isEmpty()) {
                        warningMessages.addAll(temps);
                    }
                    messageData.setMessages(warningMessages);
                    dataIDataSourceListener.onLoadFinish(messageData);
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
    public void loadData(IDataSourceListener<MessageData> listener, int pageNum, int pageSize, String search) {
        this.dataIDataSourceListener = listener;
        MessageAction.getWarnMsgData(pageNum,pageSize,search,SubmeterApp.getInstance().getUserToken(),networkResponseListener);
    }

    @Override
    public void refreshData() {
        currentPageNum=1;
        loadData(dataIDataSourceListener,currentPageNum,pageSize,"");
        isRefresh = true;
    }

    @Override
    public void loadMoreData() {
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
