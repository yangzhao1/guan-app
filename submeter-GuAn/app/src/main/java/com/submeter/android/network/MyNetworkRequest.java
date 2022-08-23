package com.submeter.android.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.submeter.android.constants.NetworkResConstant;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * A request for retrieving a {@link JSONObject} response body at a given URL,
 * allowing for an optional {@link JSONObject} to be passed in as part of the
 * request body.
 */
public class MyNetworkRequest extends StringRequest {
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private String requestBody;

    public MyNetworkRequest(int method, String url, String jsonRequest,
                            NetworkResponseListener responseListener) {
        super(method, url.startsWith("http") ? url : NetworkResConstant.SDK_SERVER_URL + url, responseListener, responseListener);
        // TODO Auto-generated constructor stub
        requestBody = jsonRequest;
    }

    public static byte[] unGZip(byte[] data) {
        byte[] b = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();
            baos.close();
            gzip.close();
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        //headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        //headers.put("Accept-Encoding", "gzip");

        return headers;
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return requestBody == null ? null : requestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    requestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        return super.setRetryPolicy(NetworkRequestTool.retryPolicy);
    }

	/*
	@Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
        	String jsonString;
        	if(response.headers.containsKey("Content-Encoding") && "gzip".equalsIgnoreCase(response.headers.get("Content-Encoding"))) {
        		jsonString = new String(unGZip(response.data), HttpHeaderParser.parseCharset(response.headers));
        	} else {
        		jsonString = new String(response.data, "utf-8");
        	}
            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }*/

    @Override
    public RetryPolicy getRetryPolicy() {
        return NetworkRequestTool.retryPolicy;
    }
}