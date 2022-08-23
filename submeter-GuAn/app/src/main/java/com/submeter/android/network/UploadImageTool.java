package com.submeter.android.network;

import com.android.volley.VolleyError;
import com.submeter.android.network.action.SystemAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaobo on 2019/1/11.
 * 图片上传类，可上传多张图片，上传完成后，将对应的url返回，接收者需要判断结果中是否有空的url，如果有空的，则表明有未部分图片未上传成功
 * 由于是异步上传，所以只能判断出是否有失败的，不能获取到，那一个图片上传失败。
 */

public class UploadImageTool implements INetworkResponseListener {
    boolean loading = false;

    private int imageCount = 0;

    private List<String> imageFileList;

    private List<String> resultUrlList;

    private UploadListener uploadListener;

    private NetworkResponseListener networkResponseListener;

    public UploadImageTool(List<String> imageFileList, UploadListener uploadListener) {
        this.imageFileList = imageFileList;
        this.uploadListener = uploadListener;
    }

    public void start() {
        if(loading) {
            return;
        }

        loading = true;
        if(imageFileList == null || imageFileList.isEmpty()) {
            loading = false;
            if(uploadListener != null) {
                uploadListener.uploadFinished(null);
            }
            return;
        }

        networkResponseListener = new NetworkResponseListener(this);

        imageCount = imageFileList.size();
        resultUrlList = new ArrayList<>();
        for (String url : imageFileList) {
            SystemAction.uploadImage(url, networkResponseListener);
        }
    }

    @Override
    public void onResponse(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String url = jsonObject.optString("url");
            resultUrlList.add(url);
        } catch (JSONException e) {
            e.printStackTrace();
            resultUrlList.add("");
        }

        if (resultUrlList.size() == imageCount && uploadListener != null) {
            loading = false;
            uploadListener.uploadFinished(resultUrlList);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        resultUrlList.add("");
        if (resultUrlList.size() == imageCount && uploadListener != null) {
            loading = false;
            uploadListener.uploadFinished(resultUrlList);
        }
    }

    public interface UploadListener {
        void uploadFinished(List<String> resultList);
    }
}
