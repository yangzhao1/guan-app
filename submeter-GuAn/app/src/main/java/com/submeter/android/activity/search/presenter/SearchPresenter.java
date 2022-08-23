package com.submeter.android.activity.search.presenter;

import android.content.Intent;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.commodityList.view.CommodityListActivity;
import com.submeter.android.activity.search.model.SearchModel;
import com.submeter.android.activity.search.model.ISearchModel;
import com.submeter.android.activity.search.view.ISearchView;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.interfacce.IDataSourceListener;

import java.util.List;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public class SearchPresenter implements ISearchPresenter {

    private int loadType = -1;

    private ISearchView searchView;

    private ISearchModel searchModel;

    private IDataSourceListener<List<String>> historyDataSourceListener;

    public SearchPresenter(ISearchView searchView) {
        this.searchView = searchView;

        searchModel = new SearchModel();
    }

    @Override
    public void loadSearchHistory(int historyType) {
        this.loadType = historyType;
        if(historyDataSourceListener == null) {
            historyDataSourceListener = new IDataSourceListener<List<String>>() {
                @Override
                public void onLoadFinish(List<String> data) {
                    if(loadType == ISearchModel.SEARCH_COMMODITY) {
                        searchView.updateView(ISearchView.COMMODITY_TAB, data);
                    } else if(loadType == ISearchModel.SEARCH_BRAND) {
                        searchView.updateView(ISearchView.BRAND_TAB, data);
                    }
                }

                @Override
                public void onLoadFail(int errorCode, String errorMessage) {

                }
            };
        }
        searchModel.loadHistoryData(loadType, historyDataSourceListener);
    }

    @Override
    public void search(int type, String keyword) {
        if(type == ISearchModel.SEARCH_COMMODITY) {
            List<String> newData = searchModel.addSearchKeyword(keyword, type);
            searchView.updateView(ISearchView.COMMODITY_TAB, newData);

            BaseActivity baseActivity = searchView.getBaseActivity();
            Intent intent = new Intent(baseActivity, CommodityListActivity.class);
            intent.putExtra(SystemConstant.COMMODITYLIST_KEYWORDS_KEY, keyword);
            baseActivity.startActivity(intent);
        } else if(type == ISearchModel.SEARCH_BRAND) {
            //searchModel.searchBrand();
            List<String> newData = searchModel.addSearchKeyword(keyword, type);
            searchView.updateView(ISearchView.BRAND_TAB, newData);
        }
    }

    @Override
    public void deleteHistory(int type) {
        searchModel.deleteSearchHistory(type);
        if(type == ISearchModel.SEARCH_COMMODITY) {
            searchView.updateView(ISearchView.COMMODITY_TAB, null);
        } else if(type == ISearchModel.SEARCH_BRAND) {
            searchView.updateView(ISearchView.BRAND_TAB, null);
        }

    }

    @Override
    public void goBack() {
        searchView.getBaseActivity().finish();
    }
}
