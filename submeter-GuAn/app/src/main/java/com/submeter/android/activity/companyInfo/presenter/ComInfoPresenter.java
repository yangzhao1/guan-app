package com.submeter.android.activity.companyInfo.presenter;

import com.android.volley.VolleyError;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.companyInfo.model.ComInfoModel;
import com.submeter.android.activity.companyInfo.view.IComInfoView;
import com.submeter.android.entity.Power;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.ProblemAction;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by  on 2018/12/1.
 */

public class ComInfoPresenter implements IComInfoPresenter, IDataSourceListener<Power> {
    private IComInfoView comInfoView;
    private ComInfoModel model;

    private NetworkResponseListener listener;
    public ComInfoPresenter(IComInfoView comInfoView) {
        this.comInfoView = comInfoView;
        model = new ComInfoModel();
    }

    @Override
    public void loadData(int id,String currentTime,int type,int stateType) {
        comInfoView.getBaseActivity().showLoadingView();
        model.loadData(this,id,currentTime);
        loadHoursData(id,currentTime,type,stateType);
        loadDayData(id,currentTime,type,stateType);
    }

    @Override
    public void loadHoursData(int id, String currentTime,int type,int stateType) {
        if (currentTime.isEmpty()){
            comInfoView.getBaseActivity().showLoadingView();
        }
        listener = new NetworkResponseListener(new INetworkResponseListener() {
            @Override
            public void onResponse(String result) {
                comInfoView.getBaseActivity().hideLoadingView();
                if (!listener.handleInnerError(result)){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray array = jsonObject.getJSONArray("eleList");
                        if (array!=null){
                            comInfoView.updatChar(array);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                comInfoView.getBaseActivity().hideLoadingView();

            }
        });

        ProblemAction.getHoursCompanyPowerList(id,"",type,stateType, SubmeterApp.getInstance().getUserToken(), listener);
    }

    @Override
    public void loadDayData(int id, String currentTime,int type,int stateType) {
        if (currentTime.isEmpty()){
            comInfoView.getBaseActivity().showLoadingView();
        }
        listener = new NetworkResponseListener(new INetworkResponseListener() {
            @Override
            public void onResponse(String result) {
                comInfoView.getBaseActivity().hideLoadingView();

                if (!listener.handleInnerError(result)){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray array = jsonObject.getJSONArray("eleList");
                        if (array!=null){
                            comInfoView.updatCharDay(array);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                comInfoView.getBaseActivity().hideLoadingView();

            }
        });

        ProblemAction.getDayCompanyPowerList(id,"", type,stateType,SubmeterApp.getInstance().getUserToken(), listener);
    }

    @Override
    public void onLoadFinish(Power data) {
        comInfoView.getBaseActivity().hideLoadingView();
        if (data!=null){
            comInfoView.updateView(data);
        }
//        if (data!=null){
//            comInfoView.updateView(data);
//        }else {
//            comInfoView.updateView(null);
//        }
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        comInfoView.getBaseActivity().hideLoadingView();
    }
}
