package com.submeter.android.activity.processing.model;

import com.submeter.android.interfacce.IDataSourceListener;

import org.json.JSONObject;

/**
 * Created by  on 2018/12/1.
 */

public interface IProModel {
    void submit(IDataSourceListener listener, JSONObject object);
}
