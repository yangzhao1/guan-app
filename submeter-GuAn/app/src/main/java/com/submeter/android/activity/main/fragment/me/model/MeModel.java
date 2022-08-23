package com.submeter.android.activity.main.fragment.me.model;

import com.android.volley.VolleyError;
import com.submeter.android.SubmeterApp;
import com.submeter.android.entity.Profile;
import com.submeter.android.entity.User;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.MeAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public class MeModel implements IMeModel,INetworkResponseListener {


    private NetworkResponseListener listener;

    private IDataSourceListener loadDataListener;

    private Map<String,String> map=null;

    public MeModel() {
        listener = new NetworkResponseListener(this);
    }

    @Override
    public User loadUserInfo() {
        Profile profile = SubmeterApp.getInstance().getCurrentProfile();
        if(null == profile) {
            return null;
        } else {
            return profile.getUser();
        }
    }

    @Override
    public void getNewVersions(IDataSourceListener<Map<String,String>> loadDataListener) {
        this.loadDataListener = loadDataListener;
        MeAction.getNewVersions(listener);
    }

    @Override
    public void uploadHeadImage(File file) {
        MeAction.uploadHeadImage(SubmeterApp.getInstance().getUserToken(),file,listener);
    }

    @Override
    public void onResponse(String result) {
        if (!listener.handleInnerError(result)){
            try {
                JSONObject resultObj = new JSONObject(result);
                JSONObject dataObj = resultObj.getJSONObject("data");

                map = new HashMap();
                map.put("codeVersions",dataObj.getString("versionno"));
                map.put("url",dataObj.getString("url"));
                loadDataListener.onLoadFinish(map);
//                map.put("headImage","");
//                homeData = new Gson().fromJson(dataObj.toString(), HomeData.class);//.parseObject(dataObj.toJSONString(), new TypeReference<HomeData>() {});
            } catch (JSONException e) {
                loadDataListener.onLoadFinish(null);
            }
        }else {
            loadDataListener.onLoadFinish(null);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (loadDataListener!=null){
            loadDataListener.onLoadFail(listener.getErrorCode(),listener.getErrorMessage());
        }
    }
}
