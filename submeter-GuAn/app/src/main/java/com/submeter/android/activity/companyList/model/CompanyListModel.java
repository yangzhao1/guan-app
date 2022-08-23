package com.submeter.android.activity.companyList.model;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.Company;
import com.submeter.android.entity.CompanyDataBean;
import com.submeter.android.entity.PageData;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.MonitorAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/12/1.
 */

public class CompanyListModel implements ICompanyListModel, INetworkResponseListener {

    private List<Company> list = new ArrayList<>();
    private CompanyDataBean bean = new CompanyDataBean();

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
    private String isHandle="";
    private String seacrchC="";
    private int state=1;
    private String org="";

    public CompanyListModel() {
        networkResponseListener = new NetworkResponseListener(this);
    }

    @Override
    public void loadData(IDataSourceListener listener,String sort,int pageNum,String searchC,
                         int state,String org) {
        this.loadDataListener = listener;
        this.isHandle = sort;
        this.seacrchC = searchC;
        this.state = state;
        this.org = org;
        MonitorAction.getCompanyListNew(searchC,sort,SubmeterApp.getInstance().getUserToken(),
                pageNum+"",pageSize+"",state,org,networkResponseListener);
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
                JSONObject entSate = jsonObject.getJSONObject("entSate");
                bean.setProductNum(entSate.getString("scEnt").equals("null")?"0":entSate.getString("scEnt"));
                bean.setStopProductNum(entSate.getString("tcqyEnt").equals("null")?"0":entSate.getString("tcqyEnt"));
                bean.setNotStartNum(entSate.getString("zwbwkEnt").equals("null")?"0":entSate.getString("zwbwkEnt"));
                bean.setNotFoundNum(entSate.getString("hbbwkEnt").equals("null")?"0":entSate.getString("hbbwkEnt"));
                PageData<Company> pageData = new Gson().fromJson(object.toString(),new TypeToken<PageData<Company>>() {}.getType());
                if (pageData!=null){
                    if (isRefresh||isSearch){
                        list.clear();
                    }
                    currentPageNum = pageData.getCurrPage();
                    totalPageNum = pageData.getTotalPage();
                    List<Company> temps = pageData.getList();
                    if(temps != null && !temps.isEmpty()) {
                        list.addAll(temps);
                    }
                    bean.setList(list);
                    loadDataListener.onLoadFinish(bean);
                }else {
                    loadDataListener.onLoadFinish(bean);
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
    public void onSearch(String seacrchC,String sort,
                         int state,String org) {
        this.isHandle = sort;
        this.seacrchC = seacrchC;
        this.org = org;
        this.state = state;
        currentPageNum=1;
        loadData(loadDataListener,isHandle,currentPageNum,seacrchC,state,org);
        isSearch = true;
    }

    @Override
    public boolean isSearch() {
        return isSearch;
    }

    @Override
    public void onRefresh() {
        currentPageNum=1;
        loadData(loadDataListener,isHandle,currentPageNum,seacrchC,state,org);
        isRefresh = true;
    }

    @Override
    public void onLoadMore() {
        currentPageNum++;
        if (currentPageNum>totalPageNum){
            loadDataListener.onLoadFinish(null);
        }else {
            loadData(loadDataListener,isHandle,currentPageNum,seacrchC,state,org);
        }
        isRefresh = false;
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }
}
