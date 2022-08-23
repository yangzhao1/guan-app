package com.submeter.android.network;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.NetworkResConstant;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<String> {
    private Response.Listener mListener = null;
    private MultipartRequestParams params = null;
    private HttpEntity httpEntity = null;

    public MultipartRequest(int method, String url, MultipartRequestParams params, Response.Listener<String> listener) {
        super(method, url.startsWith("http") ? url : NetworkResConstant.SERVER_IP + url, null);
        this.params = params;
        this.mListener = listener;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        // TODO Auto-generated method stub
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (params != null) {
            httpEntity = params.getEntity();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String str = new String(baos.toByteArray());
        }
        return baos.toByteArray();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // TODO Auto-generated method stub
        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        String token = SubmeterApp.getInstance().getUserToken();
        if(!TextUtils.isEmpty(token)) {
            headers.put("token", token);
        }
        return headers;
    }

    public String getBodyContentType() {
        // TODO Auto-generated method stub
        String str = httpEntity.getContentType().getValue();
        return httpEntity.getContentType().getValue();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        return super.setRetryPolicy(NetworkRequestTool.retryPolicy);
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return NetworkRequestTool.retryPolicy;
    }
}