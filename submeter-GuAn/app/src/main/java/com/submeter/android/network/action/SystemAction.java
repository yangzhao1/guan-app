package com.submeter.android.network.action;

import com.android.volley.Request;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.network.MultipartRequest;
import com.submeter.android.network.MultipartRequestParams;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.network.NetworkResponseListener;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by zhao.bo on 2015/9/10.
 */
public class SystemAction {
    /**
     * 上传图片的接口
     *
     * @param imagePath        文件路径
     * @param responseListener
     */
    public static Request uploadImage(String imagePath, NetworkResponseListener responseListener) {
        MultipartRequestParams params = new MultipartRequestParams();
        File file = null;
        try {
            file = new File(new URI(imagePath));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        params.put("file", file);

        MultipartRequest networkRequest = new MultipartRequest(Request.Method.POST,
                NetworkResConstant.post_uploadImage, params, responseListener);
        NetworkRequestTool.getInstance().sendRequest(networkRequest);

        return networkRequest;
    }
}