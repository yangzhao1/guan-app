package com.submeter.android.tools;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.submeter.android.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Herry on 2017/8/17.
 */
public class JSCallback {
    private BaseActivity activity;

    public JSCallback(BaseActivity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void openImage(String currentUrl, String[] urls) {
        ArrayList<String> urlArray = new ArrayList<>(urls.length);

        urlArray.addAll(Arrays.asList(urls));
        int index = urlArray.indexOf(currentUrl);
        openImage(index, urlArray);
    }

    protected void openImage(int index, ArrayList<String> urlArray) {
        if(null == urlArray || urlArray.isEmpty()) {
            return;
        }

        int validImageCount = 0;
        int size = urlArray.size();
        String url;
        for(int i = 0; i < size; ++i) {
            url = urlArray.get(i);
            if(TextUtils.isEmpty(url)) {
                continue;
            }
            /*if(url.indexOf("?x-oss-process") == -1) {
                url = MeetownApp.getInstance().getFullScreenImageStyle(url);
            } else if(url.indexOf("/watermark") == -1 && url.indexOf(".gif") == -1) {
                url += SystemConstant.ADD_IMAGE_WATER_STYLE;
            }*/
            urlArray.set(i, url);
            ++validImageCount;
        }

        if(validImageCount > 0) {
            /*Intent intent = new Intent(activity, ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urlArray);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, index);
            activity.startActivityFadeIn(intent);*/
        }
    }
}
