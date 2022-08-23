package com.submeter.android.tools;

import android.annotation.TargetApi;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.submeter.android.SubmeterApp;

/**
 * Created by Herry on 2016/11/3.
 */
public class MyWebViewClient extends WebViewClient {
    private boolean pageLoadFinished = false;

    @Override
    public void onPageFinished(WebView webView, String url) {
        super.onPageFinished(webView, url);

        webView.getSettings().setBlockNetworkImage(false);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        //重新测量
        webView.measure(w, h);

        addImageClickListner(webView);

        pageLoadFinished = true;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (!handleUrl(url)) {
            webView.loadUrl(url);//载入网页
        }
        return true;
    }

    protected boolean handleUrl(String url) {
        return false;
    }

    private void addImageClickListner(WebView webView) {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过
        webView.loadUrl("javascript:(function() {"
                + "var objs = document.getElementsByTagName(\"img\");"
                + "for(var i = 0; i < objs.length; ++i) {"
                + "    var originalData = objs[i].getAttribute(\"data-original\");"
                + "    if(originalData) {"
                + "        objs[i].onclick = function() {"
                + "            var index = 0;"
                + "            var originalData;"
                + "            var urlArray = [];"
                + "            var objs = document.getElementsByTagName(\"img\");"
                + "            for(var j = 0; j < objs.length; ++j) {"
                + "                originalData = objs[j].getAttribute(\"data-original\");"
                + "                if(originalData) {"
                + "                    urlArray[index++] = originalData;"
                + "                }"
                + "            }"
                + "            window.imageListener.openImage(this.getAttribute(\"data-original\"), urlArray);"
                + "        }"
                + "    }"
                + "}"
                + "})()");
    }

    public void getHTMLImage(WebView webView) {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过
        webView.loadUrl("javascript:(function() {"
                + "var index = 0;"
                + "var originalData;"
                + "var urlArray = [];"
                + "var objs = document.getElementsByTagName(\"img\");"
                + "for(var j = 0; j < objs.length; ++j) {"
                + "    originalData = objs[j].getAttribute(\"data-original\");"
                + "    if(originalData) {"
                + "        urlArray[index++] = originalData;"
                + "    }"
                + "}"
                + "window.imageListener.openImage(null, urlArray);"
                + "})()");
    }

    public boolean isPageLoadFinished() {
        return pageLoadFinished;
    }

    @TargetApi(21)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return SubmeterApp.getInstance().getHybridCacheManager().interceptWebResRequest(request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return SubmeterApp.getInstance().getHybridCacheManager().interceptWebResRequest(url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if(handler != null) {
            handler.proceed();
        }
    }
}
