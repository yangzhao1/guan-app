package com.submeter.android.network;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.BaseResponse;
import com.submeter.android.eventbus.MessageEvent;
import com.submeter.android.tools.NetworkUtil;
import com.submeter.android.tools.Utils;

import org.apache.http.HttpStatus;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class NetworkResponseListener implements Response.Listener<String>, ErrorListener {
    private int statusCode = 200;
    private int errorCode = -1;
    private String errorMessage = "";

    private WeakReference<INetworkResponseListener> responseListenerWeakReference;

    public int getStatusCode() {
        return statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public NetworkResponseListener(INetworkResponseListener listener) {
        if (null != listener) {
            responseListenerWeakReference = new WeakReference<INetworkResponseListener>(listener);
            listener = null;
        }
    }

    @Override
    public void onResponse(String result) {
        if (null != responseListenerWeakReference && null != responseListenerWeakReference.get()) {
            responseListenerWeakReference.get().onResponse(result);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        parseNetworkError(error);
        handleError();

        if (null != responseListenerWeakReference && null != responseListenerWeakReference.get()) {
            responseListenerWeakReference.get().onErrorResponse(error);
        }
    }

    public void onHandleErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        parseNetworkError(error);
        handleError();
    }

    protected void parseNetworkError(VolleyError volleyError) {
        try {
            errorCode = -1;
            errorMessage = "";
            if (null != volleyError.networkResponse) {
                statusCode = volleyError.networkResponse.statusCode;
                String errorDetail = new String(volleyError.networkResponse.data, HttpHeaderParser.parseCharset(volleyError.networkResponse.headers));
                JSONObject errorObj = new JSONObject(errorDetail);
                if (errorObj != null) {
                    errorCode = errorObj.optInt("code");
                    errorMessage = errorObj.optString("msg");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T handleInnerError(BaseResponse<T> baseResponse) {
        if (baseResponse.isSuccess()) {
            if(baseResponse.getData() != null) {
                return baseResponse.getData();
            }else if(baseResponse.getPage() != null) {
                return baseResponse.getPage();
            }
        }
        handleError();
        return null;
    }

    public boolean handleInnerError(String error) {
        boolean handleResult = false;
        try {
            JSONObject errorObj = new JSONObject(error);
            if (errorObj != null) {
                errorCode = errorObj.optInt("code");
                if (errorObj.has("msg")) {
                    errorMessage = errorObj.optString("msg");
                } else if (errorObj.has("message")) {
                    errorMessage = errorObj.optString("message");
                }

                if (errorCode != NetworkResConstant.REQUEST_SUCCESS) {
                    handleError();
                    handleResult = true;
                    if (errorCode == NetworkResConstant.TOKEN_EXPIRED) {
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.TOKEN_EXPIRED));
                    }
                }
            }
        } catch (JSONException e) {
            handleResult = true;
            e.printStackTrace();
        }

        return handleResult;
    }

    private void handleError() {
        Application app = SubmeterApp.getInstance();
        if (TextUtils.isEmpty(errorMessage)) {
            int errorMsgResId = -1;
            switch (statusCode) {
                case HttpStatus.SC_BAD_GATEWAY: {
                    errorMsgResId = R.string.network_error;
                    break;
                }

                case HttpStatus.SC_BAD_REQUEST: {
                    errorMessage = app.getString(R.string.network_inner_error);
                    break;
                }

                case HttpStatus.SC_INTERNAL_SERVER_ERROR: {
                    errorMsgResId = R.string.network_error;
                    break;
                }

                case HttpStatus.SC_NOT_MODIFIED: {
                    errorMsgResId = R.string.network_error;
                    break;
                }

                case HttpStatus.SC_REQUEST_TIMEOUT: {
                    errorMsgResId = R.string.network_error;
                    break;
                }

                case HttpStatus.SC_NOT_FOUND: {
                    errorMsgResId = R.string.network_error;
                    break;
                }

                default:
                    errorMsgResId = R.string.network_error;
                    break;
            }
            if (errorMsgResId > 0) {
                errorMessage = app.getString(errorMsgResId);
            }

            if (!NetworkUtil.isNetworkEnabled(app)) {
                errorMessage = app.getString(R.string.network_error);
            }
        }

        Utils.showToast(app, errorMessage);
    }
}