package com.submeter.android.activity.main.fragment.monitor.fragment.all.presenter;

import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.submeter.android.R;
import com.submeter.android.entity.Company;
import com.submeter.android.entity.Monitor;
import com.submeter.android.activity.main.fragment.monitor.fragment.all.model.AllModel;
import com.submeter.android.activity.main.fragment.monitor.fragment.all.model.IAllModel;
import com.submeter.android.activity.main.fragment.monitor.fragment.all.view.IAllView;
import com.submeter.android.entity.MonitorData;
import com.submeter.android.interfacce.IDataSourceListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/12/1.
 */

public class AllPresenter implements IAllPresenter, IDataSourceListener<Monitor> {
    private IAllView allView;

    private IAllModel model;

    private MarkerOptions markerOption;
    private String isHandle = "";
    private int requestType = -1;

    public AllPresenter(IAllView allView) {
        this.allView = allView;
        model = new AllModel();
    }

    @Override
    public void loadData(String search,AMap aMap,String isHandle) {
        this.isHandle = isHandle;
//        changeCamera(
//                aMap,CameraUpdateFactory
//                        .newCameraPosition(new CameraPosition(
//                        SystemConstant.LANGFANGSHI, SystemConstant.ZOOM_LEVEL_1, 0, 0)
//                        ),null);
//        aMap.clear();
//        aMap.addMarker(new MarkerOptions().position(SystemConstant.LANGFANGSHI)
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        allView.getBaseActivity().showLoadingView();
        if (search.isEmpty()){
            requestType=1;
        }else {
            requestType=2;
        }
        model.loadData(search,this,isHandle);
    }

    @Override
    public void gotoCompanyInfo(String markerID) {
        model.gotoCompanyInfo(markerID);
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(AMap aMap,CameraUpdate update, AMap.CancelableCallback callback) {
//        boolean animated = ((CompoundButton) findViewById(R.id.animate))
//                .isChecked();
//        if (animated) {
//            aMap.animateCamera(update, 1000, callback);
//        } else {
        aMap.moveCamera(update);
//        }
    }

    @Override
    public void onLoadFinish(Monitor monitor) {
        allView.getBaseActivity().hideLoadingView();


            MonitorData data = new MonitorData();
            data.setNormal("0");
            data.setAbnormal("0");
            data.setStoppage("0");
            List<Company> listCompany  = monitor.getCompanies();

            if (listCompany!=null){
                ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
                int i = 1;
                for (Company company:listCompany) {
                    if (company.getLat()==null||company.getLat().equals("null")||company.getLon()==null||company.getLon().equals("null")){
                        markerOption = new MarkerOptions();
//                    markerOption.position(new LatLng(Double.parseDouble(company.getLat()),Double.parseDouble(company.getLon())));
                        markerOption.position(null);
                        markerOption.title(company.getName()).snippet(company.getAddress()).period(i);
                        //0违规预警1违规2正常
                        if (isHandle.equals("0")){
                            markerOption.icon(
                                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(allView.getFragment().getResources(),
                                                    R.mipmap.precessly)));
                        }else if (isHandle.equals("1")){
                            markerOption.icon(
                                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(allView.getFragment().getResources(),
                                                    R.mipmap.outly)));
                        }else {
                            markerOption.icon(
                                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(allView.getFragment().getResources(),
                                                    R.mipmap.normally)));
                        }
                        markerOptions.add(markerOption);
                    }else {
                        markerOption = new MarkerOptions();
                        try {
                            markerOption.position(new LatLng(Double.parseDouble(company.getLat()),Double.parseDouble(company.getLon())));
                        }catch (Exception e){
                            markerOption.position(null);
                            e.printStackTrace();
                        }
                        markerOption.title(company.getName()).snippet(company.getAddress()).period(i);
                        //0违规预警1违规2正常
                        if (isHandle.equals("0")){
                            markerOption.icon(
                                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(allView.getFragment().getResources(),
                                                    R.mipmap.precessly)));
                        }else if (isHandle.equals("1")){
                            markerOption.icon(
                                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(allView.getFragment().getResources(),
                                                    R.mipmap.outly)));
                        }else {
                            markerOption.icon(
                                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(allView.getFragment().getResources(),
                                                    R.mipmap.normally)));
                        }
                        markerOptions.add(markerOption);
                    }
                    i++;
                }
                monitor.setData(data);
                monitor.setMarkerOptionsList(markerOptions);
                if (requestType==1){
                    allView.updateView(monitor);
                }else {
                    allView.updatePopData(monitor);
                }
            }
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        allView.getBaseActivity().hideLoadingView();
    }
}
