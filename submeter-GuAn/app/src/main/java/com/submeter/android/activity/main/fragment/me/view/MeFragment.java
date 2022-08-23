package com.submeter.android.activity.main.fragment.me.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.submeter.android.R;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.aboutUs.AboutUsActivity;
import com.submeter.android.activity.forgetPassword.view.UpdatePasswordActivity;
import com.submeter.android.activity.login.view.LoginActivity;
import com.submeter.android.activity.main.fragment.me.presenter.IMePresenter;
import com.submeter.android.activity.main.fragment.me.presenter.MePresenter;
import com.submeter.android.activity.myinfo.MyInfoActivity;
import com.submeter.android.entity.User;
import com.submeter.android.interfacce.IChildFragment;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.UploadImageTool;
import com.submeter.android.network.action.UserAction;
import com.submeter.android.tools.SharedPreferencesUtils;
import com.submeter.android.tools.Utils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MeFragment extends Fragment implements IChildFragment, IMeView,INetworkResponseListener {

    public static final String TAG = MeFragment.class.getSimpleName();
    @BindView(R.id.changePS)
    RelativeLayout changePS;
    @BindView(R.id.update)
    RelativeLayout update;
    @BindView(R.id.loginOut)
    RelativeLayout loginOut;
    @BindView(R.id.aboutUs)
    RelativeLayout aboutUs;
    @BindView(R.id.statusbar_view)
    View statusbarView;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.headImage)
    ImageView headImage;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.info)
    LinearLayout info;

    private Unbinder unbinder;

    private boolean isFirst = true;

    private boolean isHidden = true;

    private View contentView;

    private BaseActivity parentActivity;

    private IMePresenter mePresenter;

    private NetworkResponseListener networkResponseListener;
    private UploadImageTool uploadImageTool;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (BaseActivity) getActivity();
        cropCircleTransformation = new CropCircleTransformation(new LruBitmapPool((int) (Runtime.getRuntime().maxMemory() / 4)));
        mePresenter = new MePresenter(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = Utils.inflateView(parentActivity, inflater, R.layout.activity_menew, container, false);
            initView();
        } else {
            ViewGroup p = (ViewGroup) contentView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }

        unbinder = ButterKnife.bind(this, contentView);

        textTitle.setText("我的");
        initEvent();
        return contentView;
    }

    private void initEvent() {

        changePS.setOnClickListener((view) -> {
            SubmeterApp.getInstance().getCurrentProfile();
            startActivity(new Intent(parentActivity, UpdatePasswordActivity.class));
        });

        update.setOnClickListener((view) -> {
            mePresenter.getNewVersions();
        });

        info.setOnClickListener((view) -> {
            if (SubmeterApp.getInstance().getCurrentProfile()!=null){
                startActivity(new Intent(parentActivity, MyInfoActivity.class));
            }
        });

        aboutUs.setOnClickListener((view) -> {
            startActivity(new Intent(parentActivity, AboutUsActivity.class));
        });

        loginOut.setOnClickListener((view) -> {
            showToast("退出成功");
            SubmeterApp.getInstance().updateLoginUserInfo("");
            startActivity(new Intent(parentActivity, LoginActivity.class));
//            parentActivity.finish();
        });

        headImage.setOnClickListener(v -> {
            selectImg();
        });

        networkResponseListener = new NetworkResponseListener(this);
    }
    private final int REQUEST_CODE_GET_PHOTO = 4;
    private final int REQUEST_CODE_CUT_PHOTO = 5;
    private Uri userImageUri;
    private String imagepath = "";
    private CropCircleTransformation cropCircleTransformation;

    /**
     * 选择头像
     */
    private void selectImg() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GET_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        showLog(resultCode + "     ");

        switch (requestCode) {
            case REQUEST_CODE_GET_PHOTO:
                if (data != null) {
//                    startPhotoZoom(data.getData(), 100);
                    Uri s = data.getData();
                    userImageUri = Utils.reSizeImage(data.getData(),userImageUri,getFragment());
                }
//                showLog(data.toString());
                break;
            case REQUEST_CODE_CUT_PHOTO:
                if (data != null) {
//                    setPicToViews(data);
                    Uri ur = data.getData();
                    imagepath = userImageUri.toString();
                    File file = null;
                    try {
                        file = new File(new URI(imagepath));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Glide.with(this)
                            .load(file)
                            .skipMemoryCache(true)   //跳过内置缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE)   //不要在 disk硬盘中缓存
                            .bitmapTransform(cropCircleTransformation)
                            .placeholder(R.mipmap.default_headicon)
                            .into(headImage);

                    Log.i("","----------路径----------" + imagepath);
                    List<String> img = new ArrayList<>();
//                    img.add(file);
//                    mePresenter.uploadHeadImage(file);
                    img.add(imagepath);
                    uploadImageTool = new UploadImageTool(img, new UploadImageTool.UploadListener() {
                        @Override
                        public void uploadFinished(List<String> resultList) {
                            if (resultList.size()!=0){
                                SharedPreferencesUtils.setParam(parentActivity,"headImage",resultList.get(0));
                                SubmeterApp.getInstance().getCurrentProfile().getUser();
                                UserAction.changeUser(SubmeterApp.getInstance().getUserToken(),resultList.get(0),
                                        SubmeterApp.getInstance().getCurrentProfile().getUser().getUserId(),
                                        networkResponseListener);
                            }
                        }
                    });
                    uploadImageTool.start();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        initStatusView();
    }

    private void initStatusView() {
        View statusbarView = contentView.findViewById(R.id.statusbar_view);
        statusbarView.getLayoutParams().height = parentActivity.getStatusBarHeight();
        statusbarView.setBackgroundColor(getResources().getColor(R.color.blue_color));
    }

    @Override
    public BaseActivity getBaseActivity() {
        return parentActivity;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    public void pageShow() {
        if (!isHidden) {
            return;
        }

        if (isFirst) {
            mePresenter.loadUserInfo();
        }

        isHidden = false;
        isFirst = false;
    }

    public void pageHide() {
        if (isHidden) {
            return;
        }

        isHidden = true;
    }

    public boolean getStatusbarDardMode() {
        return true;
    }

    @Override
    public void showLoadingView() {
        parentActivity.showLoadingView();
    }

    @Override
    public void hideLoadingView() {
        parentActivity.hideLoadingView();
    }

    @Override
    public void showToast(String toast) {
        Utils.showToast(parentActivity, toast);
    }

    @Override
    public void refreshUserInfo(User user) {
        if (null == user) {
            loginOut.setVisibility(View.GONE);
            name.setText("暂未登录");
        } else {
            name.setText(user.getUsername());
            loginOut.setVisibility(View.VISIBLE);
        }

        String url = (String) SharedPreferencesUtils.getParam(parentActivity,"headImage","");
        if (url.equals("")){
            Glide.with(this)
                    .load(user.getUserImg())
                    .skipMemoryCache(true)   //跳过内置缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)   //不要在 disk硬盘中缓存
                    .bitmapTransform(cropCircleTransformation)
                    .placeholder(R.mipmap.default_headicon)
                    .into(headImage);        }else {
            Glide.with(this)
                    .load(url)
                    .skipMemoryCache(true)   //跳过内置缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)   //不要在 disk硬盘中缓存
                    .bitmapTransform(cropCircleTransformation)
                    .placeholder(R.mipmap.default_headicon)
                    .into(headImage);        }
    }

    @Override
    public void refreshHeadImage(String image) {
//        Glide.with(this)
//                .load(image)
//                .skipMemoryCache(true)   //跳过内置缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)   //不要在 disk硬盘中缓存
//                .bitmapTransform(cropCircleTransformation)
//                .placeholder(R.mipmap.default_headicon)
//                .into(headImage);



    }

    @Override
    public void onResponse(String result) {
        if (!networkResponseListener.handleInnerError(result)) {
            showToast("修改成功");
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                String url = jsonObject.getString("url");
//                UserAction.changeUser(SubmeterApp.getInstance().getUserToken(),url,
//                        SubmeterApp.getInstance().getCurrentProfile().getUser().getUserId(),
//                        networkResponseListener);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String msg = networkResponseListener.getErrorMessage();
        showToast(msg);
    }
}