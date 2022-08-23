package com.submeter.android.db;

import android.app.Activity;
import android.content.SharedPreferences;

import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.DBConstant;

public class ShareStoreProcess {
	
	private static ShareStoreProcess _instance = null;

	private SharedPreferences sharePreferences = null;

	public static ShareStoreProcess getInstance() {
		if (_instance == null) {
			_instance = new ShareStoreProcess();
		}
		return _instance;
	}

	private ShareStoreProcess() {
		sharePreferences = SubmeterApp.getInstance().getSharedPreferences(DBConstant.SHARE_STORE_INFO, Activity.MODE_PRIVATE);
	}

	public boolean setKeyAndValue(String key, String value) {
		return sharePreferences.edit().putString(key, value).commit();
	}

    public String getDataByKey(String key) {
        String value = sharePreferences.getString(key, "");
        if(null == value) {
            value = "";
        }

        return value;
    }

	public void clearCache() {
		sharePreferences.edit().clear().commit();
	}
}