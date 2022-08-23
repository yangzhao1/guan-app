package com.submeter.android.activity.commodityDetail.view;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.commodityDetail.presenter.CommodityDetailPresenter;
import com.submeter.android.activity.commodityDetail.presenter.ICommodityDetailPresenter;
import com.submeter.android.entity.Commodity;
import com.submeter.android.tools.JSCallback;
import com.submeter.android.tools.MyWebViewClient;
import com.submeter.android.tools.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class CommodityDetailActivity extends BaseActivity implements ICommodityDetailView {

    private ICommodityDetailPresenter detailPresenter;

    @BindView(R.id.statusbar_view)
    View statusView;

    @BindView(R.id.webview)
    WebView webView;

    @BindView(R.id.follow_view)
    TextView followView;

    private MyWebViewClient webViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        darkStatusBar = false;
        setContentView(R.layout.activity_commodity_detail);

        webView.loadUrl("http://www.baidu.com");
        initWebView(webView, new JSCallback(this));

        int commodityId = getIntent().getIntExtra("id", -1);
        detailPresenter = new CommodityDetailPresenter(this);
        detailPresenter.loadCommodity(commodityId);
    }

    @Override
    public void initWebView(WebView webView, JSCallback jsCallback) {
        super.initWebView(webView, jsCallback);
        webViewClient = new MyWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                detailPresenter.webLoadFinish();
            }

            @Override
            protected boolean handleUrl(String url) {
                boolean handled = false;
                if (url.startsWith("tel:")) {
                    detailPresenter.call(url);
                    handled = true;
                } else {
                    handled = detailPresenter.handleHook(url);
                }

                return handled;
            }
        };
        webView.setWebViewClient(webViewClient);
    }

    @OnClick({R.id.cs_view, R.id.shop_view, R.id.follow_view, R.id.add_shoppingcart_view, R.id.buy_view})
    public void clicked(View view) {
        switch (view.getId()) {

            case R.id.cs_view: {
                detailPresenter.gotoCS();
                break;
            }

            case R.id.shop_view: {
                detailPresenter.gotoShop();
                break;
            }

            case R.id.follow_view: {
                detailPresenter.follow();
                break;
            }

            case R.id.add_shoppingcart_view: {
                detailPresenter.addShoppingcart();
                break;
            }

            case R.id.buy_view: {
                detailPresenter.gotoBuy();
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void updateView(Commodity commodity) {
        Utils.showToast(this, "view updated");
    }

    @Override
    public void updateFollowStatus(boolean following) {
        if(following) {
            followView.setText(R.string.following_label);
            followView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.gztb_1, 0, 0);
        } else {
            followView.setText(R.string.follow_label);
            followView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.gztb, 0, 0);
        }
    }
}
