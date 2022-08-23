package com.submeter.android.entity;

import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao.bo on 2015/8/12.
 */
public class Monitor {

    private List<Company> companies;

    private ArrayList<MarkerOptions> markerOptionsList;

    private MonitorData data;

    public void setMarkerOptionsList(ArrayList<MarkerOptions> markerOptionsList) {
        this.markerOptionsList = markerOptionsList;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public ArrayList<MarkerOptions> getMarkerOptionsList() {
        return markerOptionsList;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public MonitorData getData() {
        return data;
    }

    public void setData(MonitorData data) {
        this.data = data;
    }
}
