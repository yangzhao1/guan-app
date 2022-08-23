package com.submeter.android.activity.webActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.entity.ShareInfo;
import com.submeter.android.tools.JSCallback;
import com.submeter.android.tools.JSHookUtils;
import com.submeter.android.tools.MyWebViewClient;
import com.submeter.android.view.ShareDialog;
import com.submeter.swipebacklib.SwipeBackLayout;

public class ShowWebActivity extends BaseActivity implements SwipeBackLayout.SwipeListener {

    public static final String TAG = ShowWebActivity.class.getSimpleName();

    private final int HIDE_LOADING_MESSAGE = 1;

    private String bannerId;

    private String title;

    private String coverImage;

    private String h5pageUrl;

    private String wxh5pageUrl;

    private WebView webView;

    private Handler handler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bData = getIntent().getExtras();
            String url = bData.getString("url");
            if (url != null && url.length() > 0) {
                url = url.trim();
                if (!url.startsWith("http")) {
                    url = "http://" + url;
                }
            } else {
                finish();
                return;
            }

            bannerId = bData.getString("bannerId");
            coverImage = bData.getString("coverImage");

            boolean canShare = true;
            h5pageUrl = bData.getString("h5pageUrl");
            wxh5pageUrl = bData.getString("wxh5pageUrl");
            if (TextUtils.isEmpty(h5pageUrl) || TextUtils.isEmpty(wxh5pageUrl)) {
                canShare = false;
            }

            title = bData.getString("title");
            setContentView(R.layout.activity_webview, -1, R.drawable.bg_title_back, /*canShare ? R.mipmap.share :*/ -1,false);
            if (null != title) {
                ((TextView) findViewById(R.id.text_title)).setText(title);
            }

            init();

            webView.loadUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

    }

    private void init() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HIDE_LOADING_MESSAGE:
                        //hideInnerLoading();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        webView = (WebView) findViewById(R.id.webview);
        initWebView(webView, new JSCallback(this));
        webView.setWebViewClient(new MyWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                handler.sendEmptyMessage(HIDE_LOADING_MESSAGE);//如果全部载入,隐藏进度对话框
            }

            @Override
            protected boolean handleUrl(String url) {
                boolean handled = false;
                String functionName = JSHookUtils.getFunctionName(url);
                if (!TextUtils.isEmpty(functionName)) {
                    if (JSHookUtils.CLOSE_PAGE.equals(functionName)) {
                        finish();
                    }

                    handled = true;
                } else if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    handled = true;
                }

                return handled;
            }

        });

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url.contains(".apk")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        getSwipeBackLayout().addSwipeListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_title_btn: {
                handleGoBackEvent();
                break;
            }

            case R.id.right_title_btn: {
                ShareInfo shareInfo = new ShareInfo();
                shareInfo.setH5PageUrl(h5pageUrl);
                shareInfo.setWxH5PageUrl(wxh5pageUrl);
                shareInfo.setId(bannerId);
                //shareInfo.setShareType(SystemConstant.SHARE_BANNER);
                shareInfo.setIcon(coverImage);
                shareInfo.setTitle(title);
                shareInfo.setSubTitle(null);
                ShareDialog.showShareView(this, shareInfo);
                break;
            }

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (null != handler) {
            handler.removeCallbacksAndMessages(null);
        }

        removeWebviewCache(webView);

        super.onDestroy();
    }

    @Override
    public void onScrollStateChange(int state, float scrollPercent) {
        if (state == SwipeBackLayout.STATE_DRAGGING && isKeyboardShown()) {
            hideSoftKeyboard();
        }
    }

    @Override
    public void onEdgeTouch(int edgeFlag) {

    }

    @Override
    public void onScrollOverThreshold() {

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            handleGoBackEvent();
        }
    }
}
