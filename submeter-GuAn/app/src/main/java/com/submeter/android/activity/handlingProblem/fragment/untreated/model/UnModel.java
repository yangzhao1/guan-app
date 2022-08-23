package com.submeter.android.activity.handlingProblem.fragment.untreated.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.GetOutUn;
import com.submeter.android.entity.PageData;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.ProblemAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/12/1.
 */

public class UnModel implements IUnModel, INetworkResponseListener {

    private List<GetOutUn> list = new ArrayList<>();

    private NetworkResponseListener networkResponseListener;

    private IDataSourceListener loadDataListener;
    private boolean isRefresh = false;
    private boolean isSearch = false;

    /**
     * 当前是第几页
     */
    private int currentPageNum = 1;

    /**
     * 总页数
     */
    private int totalPageNum = 1;
    private int pageSize = NetworkResConstant.LIST_PAGE_SIZE;
    private String isHandle="0";
    private String SearchC;
    private String startTime="";
    private String endTime="";
    private int type;
    private int isTodate;

    public UnModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(IDataSourceListener listener,String isHandle,int pageNum,String searchC,
                         int type,int isTodate) {
        this.loadDataListener = listener;
        this.isHandle = isHandle;
        this.SearchC = searchC;
        this.type = type;
        this.isTodate = isTodate;


        ProblemAction.getProbloemList(SearchC,isHandle,SubmeterApp.getInstance().getUserToken(),
                pageNum,type,isTodate,startTime,endTime, networkResponseListener);
    }

    @Override
    public void gotoCompanyInfo(String markerId) {
//        MonitorAction.getCompanyInfo(SubmeterApp.getInstance().getUserToken(),networkResponseListener);
    }

    @Override
    public void onResponse(String result) {
        if (!networkResponseListener.handleInnerError(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject object = jsonObject.getJSONObject("data");
                PageData<GetOutUn> pageData = new Gson().fromJson(object.toString(),new TypeToken<PageData<GetOutUn>>() {}.getType());
                if (pageData!=null){
                    if (isRefresh||isSearch){
                        list.clear();
                    }
                    currentPageNum = pageData.getCurrPage();
                    totalPageNum = pageData.getTotalPage();
                    List<GetOutUn> temps = pageData.getList();
                    if(temps != null && !temps.isEmpty()) {
                        list.addAll(temps);
                    }
                    loadDataListener.onLoadFinish(list);
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
        isRefresh = false;
        isSearch = false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(loadDataListener != null) {
            loadDataListener.onLoadFail(networkResponseListener.getErrorCode(), networkResponseListener.getErrorMessage());
        }
        isRefresh = false;
        isSearch = false;
    }

    @Override
    public void onSearch(String isHandle,String searchC,int type,
                         int isTodate,String startTime,String endTime) {
        currentPageNum=1;
        this.startTime = startTime;
        this.endTime = endTime;
        loadData(loadDataListener,isHandle,currentPageNum,searchC,type,isTodate);
        isSearch = true;
    }

    @Override
    public boolean isSearch() {
        return isSearch;
    }

    @Override
    public void onRefresh() {
        currentPageNum=1;
        loadData(loadDataListener,isHandle,currentPageNum,SearchC,type,isTodate);
        isRefresh = true;
    }

    @Override
    public void onLoadMore() {
        currentPageNum++;
        if (currentPageNum>totalPageNum){
            loadDataListener.onLoadFinish(null);
        }else {
            loadData(loadDataListener,isHandle,currentPageNum,SearchC,type,isTodate);
        }
        isRefresh = false;
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }
}
