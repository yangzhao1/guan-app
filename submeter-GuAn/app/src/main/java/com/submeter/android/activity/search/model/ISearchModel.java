package com.submeter.android.activity.search.model;

import com.submeter.android.entity.Brand;
import com.submeter.android.entity.Commodity;
import com.submeter.android.interfacce.IDataSourceListener;

import java.util.List;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface ISearchModel {
    /*搜索商品*/
    public static final int SEARCH_COMMODITY = 1;

    /*搜索品牌*/
    public static final int SEARCH_BRAND = 2;

    public void loadHistoryData(int type, IDataSourceListener<List<String>> dataSourceListener);

    public void searchCommodity(String key, int pageNum, int pageSize, IDataSourceListener<List<Commodity>> dataSourceListener);

    public void searchBrand(String key, int pageNum, int pageSize, IDataSourceListener<List<Brand>> dataSourceListener);

    public List<String> addSearchKeyword(String keyword, int type);

    public void deleteSearchHistory(int type);
}
