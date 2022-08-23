package com.submeter.android.activity.historyData.presenter;

/**
 * Created by  on 2018/12/1.
 */

public interface IHistoryDataPresenter {

    public void loadData(int id,String currentTime,int type, int stateType);

    void loadDayData(int id,String currentTime,int type,int stateType);
}
