package com.submeter.android.activity.search.model;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.DBConstant;
import com.submeter.android.db.ShareStoreProcess;
import com.submeter.android.entity.Brand;
import com.submeter.android.entity.Commodity;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.CommodityAction;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public class SearchModel implements ISearchModel, INetworkResponseListener {
    private int type = -1;

    private NetworkResponseListener networkResponseListener;

    private List<String> commodityHistoryList = new ArrayList<>();

    private List<String> brandHistoryList = new ArrayList<>();

    @Override
    public void loadHistoryData(int type, IDataSourceListener<List<String>> dataSourceListener) {
        String history = null;
        if(type == SEARCH_COMMODITY) {
            history = ShareStoreProcess.getInstance().getDataByKey(DBConstant.SEARCH_COMMODITY_HISTORY);

            if(!TextUtils.isEmpty(history)) {
                try {
                    JSONArray historyArray = new JSONArray(history);
                    int length = historyArray.length();
                    for(int i = 0; i < length; ++i) {
                        commodityHistoryList.add(historyArray.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(dataSourceListener != null) {
                dataSourceListener.onLoadFinish(commodityHistoryList);
            }
        } else if(type == SEARCH_BRAND) {
            history = ShareStoreProcess.getInstance().getDataByKey(DBConstant.SEARCH_BRAND_HISTORY);

            if(!TextUtils.isEmpty(history)) {
                try {
                    JSONArray historyArray = new JSONArray(history);
                    int length = historyArray.length();
                    for(int i = 0; i < length; ++i) {
                        brandHistoryList.add(historyArray.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(dataSourceListener != null) {
                dataSourceListener.onLoadFinish(brandHistoryList);
            }
        }
    }

    @Override
    public void searchCommodity(String key, int pageNum, int pageSize, IDataSourceListener<List<Commodity>> dataSourceListener) {
        if(networkResponseListener == null) {
            networkResponseListener = new NetworkResponseListener(this);
        }

        type = SEARCH_COMMODITY;
        CommodityAction.searchCommodity(key, pageNum, pageSize, SubmeterApp.getInstance().getUserToken(), networkResponseListener);
    }

    @Override
    public void searchBrand(String key, int pageNum, int pageSize, IDataSourceListener<List<Brand>> dataSourceListener) {
        if(networkResponseListener == null) {
            networkResponseListener = new NetworkResponseListener(this);
        }

        type = SEARCH_BRAND;
    }

    @Override
    public void onResponse(String result) {
        if(!networkResponseListener.handleInnerError(result)) {
            if(type == SEARCH_COMMODITY) {

            } else if(type == SEARCH_BRAND) {

            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public List<String> addSearchKeyword(String keyword, int type) {
        if(type == SEARCH_COMMODITY) {
            int index = commodityHistoryList.indexOf(keyword);
            if(index == -1) {
                commodityHistoryList.add(0, keyword);
            } else {
                commodityHistoryList.remove(index);
                commodityHistoryList.add(0, keyword);
            }
            ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.SEARCH_COMMODITY_HISTORY, new Gson().toJson(commodityHistoryList));

            return commodityHistoryList;
        } else if(type == SEARCH_BRAND) {
            int index = brandHistoryList.indexOf(keyword);
            if(index == -1) {
                brandHistoryList.add(0, keyword);
            } else {
                brandHistoryList.remove(index);
                brandHistoryList.add(0, keyword);
            }
            ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.SEARCH_BRAND_HISTORY, new Gson().toJson(brandHistoryList));

            return brandHistoryList;
        }

        return null;
    }

    @Override
    public void deleteSearchHistory(int type) {
        if(type == SEARCH_COMMODITY) {
            commodityHistoryList.clear();
            ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.SEARCH_COMMODITY_HISTORY, new Gson().toJson(commodityHistoryList));
        } else if(type == SEARCH_BRAND) {
            brandHistoryList.clear();
            ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.SEARCH_BRAND_HISTORY, new Gson().toJson(brandHistoryList));
        }
    }
}
