package com.submeter.android.tools;


import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.submeter.android.SubmeterApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * zqSoft Automation
 * 
 * @File:ParseJson.java
 * @author yangming
 * @Description: <解析json类>
 * @Notes: <解析服务端传过来的数据，进行解析拿到需要的数据>
 * @Revision History: <<2014-11-7下午3:43:20>>, <<yangming>>, <<Create>>
 */
public class JsonUtil {

	/**
	 * 获取联网的数据状态
	 * 
	 * @Title: parseLogin
	 * @Description: TODO
	 * @param jsonData
	 *            默认值是-1. 成功值是0. 失败值是1.
	 * @return
	 * @return int
	 * @throws
	 */

	public static boolean parseCode(String jsonData) {
		String code = null;
		try {
			JSONObject object = new JSONObject(jsonData);
			code = object.getString("code");

			if (code.equals("200")) {
				code = object.getString("message");
				return true;
			}else {
				SubmeterApp app = SubmeterApp.getInstance();
				Toast.makeText(app,code, Toast.LENGTH_SHORT).show();
				return false;
			}

		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void showJsonMessage(){

	}

	public static JSONObject parseData(String request) {
		JSONObject data = null;
		try {
			JSONObject object = new JSONObject(request);
			data = object.getJSONObject("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 从asset路径下读取对应文件转String输出
	 * @param mContext
	 * @return
	 */
	public static String getJson(Context mContext, String fileName) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		AssetManager am = mContext.getAssets();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					am.open(fileName)));
			String next = "";
			while (null != (next = br.readLine())) {
				sb.append(next);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sb.delete(0, sb.length());
		}
		return sb.toString().trim();
	}
}