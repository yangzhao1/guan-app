package com.submeter.android.network;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.NetworkResConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NetworkRequestTool {
	private static NetworkRequestTool _instance = null;
	private static RequestQueue networkRequestQueue = null;

	public static RetryPolicy retryPolicy = new DefaultRetryPolicy(NetworkResConstant.NETWORK_REQUEST_TIMEOUT,
			DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
			DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	
	public static NetworkRequestTool getInstance() {
		if(_instance == null) {
			_instance = new NetworkRequestTool();
		}
		return _instance;
	}
	
	private NetworkRequestTool() {
		networkRequestQueue = Volley.newRequestQueue(SubmeterApp.getInstance());
		//disable start, because requestQueue was started when call newRequestQueue
		//networkRequestQueue.start();
	}
	
	public void sendRequest(Request request) {
		networkRequestQueue.add(request);
	}
	
	public void stopAllRequest() {
		networkRequestQueue.stop();
	}

    public void loadImage(Uri uri, SimpleDraweeView imageView) {
        loadImage(uri, imageView, true);
    }

    public void loadImage(Uri uri, SimpleDraweeView imageView, boolean checkTag) {
	    String parameter = uri.getQueryParameter("resize");
	    if(TextUtils.isEmpty(parameter)) {
            loadImage(uri, -1, -1, imageView, checkTag);
        } else {
	        String[] sizeArray = parameter.split(",");
	        int width = Integer.parseInt(sizeArray[0].substring(2));
	        int height = Integer.parseInt(sizeArray[1].substring(2));
            loadImage(uri, width, height, imageView, checkTag);
        }
    }

    public void loadImage(Uri uri, int width, int height, SimpleDraweeView imageView, boolean checkTag) {
        if(null == uri) {
            return;
        }

        if(checkTag) {
            if (uri == imageView.getTag()) {
                return;
            }
        }

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        if(uri.toString().contains(".gif")) {
            controller.setUri(uri)
                    .setAutoPlayAnimations(true)
                    .setOldController(imageView.getController());
            imageView.setController(controller.build());
        } else {
            if(width > 0 && height > 0) {
                ImageRequest request = ImageRequestBuilder
                        .newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(width, height))
                        .build();
                controller.setOldController(imageView.getController()).setImageRequest(request).build();
                imageView.setController(controller.build());
            } else {
                imageView.setImageURI(uri);
            }
        }
    }

    public void preloadImageToDisk(String url, Context context) {
        if(TextUtils.isEmpty(url) || url.startsWith("res://")) {
            return;
        }

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
        imagePipeline.prefetchToDiskCache(imageRequest, context);
    }

    public static String getRequestParam(HashMap<String, Object> map) {

	    StringBuffer requestParam = new StringBuffer();
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for(HashMap.Entry<String, Object> entry : entrySet) {
            requestParam.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        requestParam.replace(0, 1, "?");

        return requestParam.toString();
    }

    public static String getPostRequestParam(HashMap<String, Object> map) {
        ArrayList<Map.Entry<String, Object>> paramList = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
        Collections.sort(paramList, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

       /* try {
            JSONObject jsonRequest = new JSONObject();
            StringBuffer securityCode = new StringBuffer();
            for (HashMap.Entry<String, Object> entry : paramList) {
                jsonRequest.put(entry.getKey(), entry.getValue());
                securityCode.append(entry.getValue());
            }
            securityCode.append(NetworkResConstant.SECURITY_TOKEN);
            jsonRequest.put("securityCode", MD5Util.getMD5(securityCode.toString()));

            return jsonRequest.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return null;
    }

    public static String postRequestParam(HashMap<String, String> map) {
        ArrayList<Map.Entry<String, String>> paramList = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(paramList, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        /*try {
            JSONObject jsonRequest = new JSONObject();
            StringBuffer securityCode = new StringBuffer();
            for (HashMap.Entry<String, String> entry : paramList) {
                jsonRequest.put(entry.getKey(), entry.getValue());
                securityCode.append(entry.getValue());
            }
            securityCode.append(NetworkResConstant.SECURITY_TOKEN);
            jsonRequest.put("securityCode", MD5Util.getMD5(securityCode.toString()));

            return jsonRequest.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return null;
    }
}