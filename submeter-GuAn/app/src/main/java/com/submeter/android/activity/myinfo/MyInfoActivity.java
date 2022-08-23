package com.submeter.android.activity.myinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.submeter.android.R;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.UserAction;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by yangzhao on 2019/5/9.
 */

public class MyInfoActivity extends BaseActivity implements INetworkResponseListener{

    @BindView(R.id.statusbar_view)
    View statusbarView;
    @BindView(R.id.headimage)
    ImageView headimage;
    @BindView(R.id.headimageRel)
    RelativeLayout headimageRel;
    @BindView(R.id.txt_username)
    TextView txtUsername;
    @BindView(R.id.txt_sex)
    TextView txtSex;
    @BindView(R.id.txt_mobile)
    TextView txtMobile;

    private NetworkResponseListener networkResponseListener;
    private CropCircleTransformation cropCircleTransformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo, R.string.myinfo, R.drawable.ic_back, "", false);
        ButterKnife.bind(this);

        cropCircleTransformation = new CropCircleTransformation(new LruBitmapPool((int) (Runtime.getRuntime().maxMemory() / 4)));
        networkResponseListener = new NetworkResponseListener(this);
        String userid = SubmeterApp.getInstance().getCurrentProfile().getUser().getUserId();
        if (userid!=null){
            UserAction.getUserInfo(SubmeterApp.getInstance().getUserToken(),userid,networkResponseListener);
            showLoadingView();
        }
    }

    @Override
    public void onResponse(String result) {
        hideLoadingView();
        if (!networkResponseListener.handleInnerError(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject user = jsonObject.getJSONObject("user");
                txtUsername.setText(user.getString("username"));
                txtMobile.setText(user.getString("mobile"));
                txtSex.setText("男");
                Glide.with(this)
                        .load(user.getString("userImg"))
                        .skipMemoryCache(true)   //跳过内置缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE)   //不要在 disk硬盘中缓存
                        .bitmapTransform(cropCircleTransformation)
                        .placeholder(R.mipmap.default_headicon)
                        .into(headimage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideLoadingView();
    }
}
