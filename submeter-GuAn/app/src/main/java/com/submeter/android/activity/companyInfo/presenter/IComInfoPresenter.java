package com.submeter.android.activity.companyInfo.presenter;

/**
 * Created by  on 2018/12/1.
 */

public interface IComInfoPresenter {

    void loadData(int id,String currentTime,int type,int stateType);

    void loadHoursData(int id,String currentTime,int type,int stateType);

    void loadDayData(int id,String currentTime,int type,int stateType);
}
