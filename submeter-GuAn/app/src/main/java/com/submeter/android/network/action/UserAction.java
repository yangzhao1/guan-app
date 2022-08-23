package com.submeter.android.network.action;

import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.network.MyNetworkRequest;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.network.NetworkResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zhao.bo on 2015/9/10.
 */
public class UserAction {

    /*
    * 发送注册短信
     */
    public static void sendRegisterSMS(String phone, NetworkResponseListener responseListener) {
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("phone", phone);

            MyNetworkRequest networkRequest = new MyNetworkRequest(Method.POST, NetworkResConstant.SEND_REGISTER_SMS, jsonRequest.toString(), responseListener);
            NetworkRequestTool.getInstance().sendRequest(networkRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /*
    * 用户注册
     */
    public static void register(String userName, String password, String phone, String activeCode, String email, int accountType, NetworkResponseListener responseListener) {

        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("authCode", activeCode);
            jsonRequest.put("businessType", accountType);
            if(!TextUtils.isEmpty(email)) {
                jsonRequest.put("email", email);
            }
            jsonRequest.put("mobile", phone);
            jsonRequest.put("password", password);
            jsonRequest.put("username", userName);

            MyNetworkRequest networkRequest = new MyNetworkRequest(Method.POST, NetworkResConstant.REGISTER, jsonRequest.toString(), responseListener);
            NetworkRequestTool.getInstance().sendRequest(networkRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * 发送登录短信
     */
    public static void sendLoginSMS(String phone, NetworkResponseListener responseListener) {

        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("phone", phone);

            MyNetworkRequest networkRequest = new MyNetworkRequest(Method.POST, NetworkResConstant.SEND_LOGIN_SMS, jsonRequest.toString(), responseListener);
            NetworkRequestTool.getInstance().sendRequest(networkRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * 用户注册
     */
    public static void login(String account, String password, NetworkResponseListener responseListener) {
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("userNo", account);
            jsonRequest.put("password", password);

            MyNetworkRequest networkRequest = new MyNetworkRequest(Method.POST, NetworkResConstant.post_login, jsonRequest.toString(), responseListener);
            NetworkRequestTool.getInstance().sendRequest(networkRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*获取用户信息*/
    public static void getUserInfo(String userToken,String userid,NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(userToken)) {
            keyValue.put("token", userToken);
        }

        String url = NetworkResConstant.get_myinfo + userid;
        StringBuffer strUrl = new StringBuffer(url);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.GET, strUrl.toString(), null, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    /*
    * 发送修改密码的手机验证码
     */
    public static void sendResetPsdSMS(String token, String phone, NetworkResponseListener responseListener) {
        try {
            JSONObject jsonRequest = new JSONObject();
            if(!TextUtils.isEmpty(token)) {
                jsonRequest.put("token", token);
            }
            jsonRequest.put("data", phone);

            MyNetworkRequest networkRequest = new MyNetworkRequest(Method.POST, NetworkResConstant.SEND_RESET_PSD_SMS, jsonRequest.toString(), responseListener);
            NetworkRequestTool.getInstance().sendRequest(networkRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * 发送修改密码的手机验证码
     */
    public static void resetPsd(String token, String authCode, String phone, String password, NetworkResponseListener responseListener) {
        try {
            JSONObject jsonRequest = new JSONObject();
            if(!TextUtils.isEmpty(token)) {
                jsonRequest.put("token", token);
            }

            JSONObject formObj = new JSONObject();
            formObj.put("authCode", authCode);
            formObj.put("mobile", phone);
            formObj.put("pass", password);
            jsonRequest.put("form", formObj);

            MyNetworkRequest networkRequest = new MyNetworkRequest(Method.POST, NetworkResConstant.RESET_PSD, jsonRequest.toString(), responseListener);
            NetworkRequestTool.getInstance().sendRequest(networkRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
   * 修改密码
    */
    public static void changePassword(String token, String newPasswrod, String oldPassword, NetworkResponseListener responseListener) {
            HashMap<String, Object> keyValue = new HashMap<>();
            if(!TextUtils.isEmpty(token)) {
                keyValue.put("token", token);
            }
            keyValue.put("npass", newPasswrod);
            keyValue.put("opass", oldPassword);

            StringBuffer strUrl = new StringBuffer(NetworkResConstant.post_modifyPass);
            strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

            JSONObject jsonObject = new JSONObject();

            MyNetworkRequest networkRequest = new MyNetworkRequest(Method.POST, strUrl.toString(), jsonObject.toString(), responseListener);
            NetworkRequestTool.getInstance().sendRequest(networkRequest);

    }

    /*
* 修改用户信息
*/
    public static void changeUser(String token, String url,String userid, NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }
        keyValue.put("userImg", url);

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.getPost_uploadImage+userid);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));

        JSONObject jsonObject = new JSONObject();

        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.POST, strUrl.toString(), jsonObject.toString(), responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }

    /**
     * 打卡签到
     * @param token
     * @param endId
     * @param responseListener
     */
    public static void signIn(String token, int endId,double lat,double lng ,NetworkResponseListener responseListener) {
        HashMap<String, Object> keyValue = new HashMap<>();
        if(!TextUtils.isEmpty(token)) {
            keyValue.put("token", token);
        }
        keyValue.put("endId", endId);
        keyValue.put("positionX", lng+"");
        keyValue.put("positionY", lat+"");

        StringBuffer strUrl = new StringBuffer(NetworkResConstant.post_signIn);
        strUrl.append(NetworkRequestTool.getRequestParam(keyValue));
        JSONObject jsonObject = new JSONObject();
        MyNetworkRequest networkRequest = new MyNetworkRequest(Method.POST, strUrl.toString(), jsonObject.toString(), responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);
    }
}