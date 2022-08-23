package com.submeter.android.activity.welcome.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.activity.login.view.LoginActivity;
import com.submeter.android.activity.main.view.MainActivity;
import com.submeter.android.activity.produceIntroduce.ProduceIntroductionActivity;
import com.submeter.android.activity.welcome.presenter.IWelcomePresenter;
import com.submeter.android.constants.DBConstant;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.db.ShareStoreProcess;
import com.submeter.android.entity.Banner;
import com.submeter.android.eventbus.MessageEvent;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.tools.FileUtils;
import com.submeter.android.tools.PermissionUtils;
import com.submeter.android.tools.Utils;
import com.submeter.android.view.CircleProgressBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WelcomeActivity extends Activity implements IWelcomeView {
    public static final String TAG = WelcomeActivity.class.getSimpleName();

    private int keyRepeatCount = 0;

    private long firstKeyDownTime = 0;

    @BindView(R.id.splash_view)
    SimpleDraweeView mSplashBannerView;

    @BindView(R.id.timer_view)
    CircleProgressBar mProgressBar;

    private Unbinder unbinder;

    private IWelcomePresenter mWelcomePresenter;

    private CountDownTimer mTimer;

    protected PermissionUtils.IPermissionRequestCallback mPermissionRequestCallback;

    private static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_SETTINGS,Manifest.permission.CALL_PHONE};

    private static final int REQUEST_CONTACTS = 1000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.Transparent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        unbinder = ButterKnife.bind(this);

        //add eventbus listener for closing this page
//        EventBus.getDefault().register(this);

        mPermissionRequestCallback = new PermissionUtils.IPermissionRequestCallback() {
            @Override
            public void permissionGranted(String permission) {
                launch();
                //start gps listener
//                SubmeterApp.getInstance().startGpsService();
            }

            @Override
            public void permissionDenied(String permission) {
                launch();
//                SubmeterApp.getInstance().startGpsService();
            }
        };

        //sdk大于23的。动态权限自动获取
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        }else {
            launch();
        }
        //check gps permission & request permission
//        mWelcomePresenter = new WelcomePresenter(this);
//        if(mWelcomePresenter.needUpdateGPS()) {
//            mWelcomePresenter.requestGPSPermission(mPermissionRequestCallback);
//        } else {
//        }
    }

    private void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            requestSetPermissions();
        } else {
//            mLocClient.start();
            launch();
        }
    }

    private void requestSetPermissions() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_SETTINGS) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)){
//            Snackbar.make(view, "permission_contacts_rationale",
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("ok", new View.OnClickListener() {
//                        @Override
//                        publics void onClick(View view) {
            ActivityCompat.requestPermissions(WelcomeActivity.this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
//                        }
//                    })
//                    .show();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CONTACTS) {
            launch();
//            if (PermissionUtil.verifyPermissions(grantResults)) {
//                mLocClient.start();
//            } else {
//                Toast.makeText(getApplicationContext(),"授权不通过",Toast.LENGTH_SHORT).show();
//            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void updateView(Banner banner) {
        //update welcome view
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        FileUtils.clearAdCache();
        mSplashBannerView.setVisibility(View.VISIBLE);
        mSplashBannerView.setTag(banner);
        mSplashBannerView.setOnClickListener(bannerItemClick);
        NetworkRequestTool.getInstance().loadImage(Uri.parse(banner.getPath()), mSplashBannerView);

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(100);
        mProgressBar.setDuration(3000);
        mProgressBar.setCountDownListener(new CircleProgressBar.CountDownListener() {
            @Override
            public void onFinish() {
                startUp(null);
            }
        });
        mProgressBar.start();
    }

    private void launch() {
        String productIntroduce = ShareStoreProcess.getInstance().getDataByKey(DBConstant.SHOW_PRODUCT_INTRODUCE);
        if(TextUtils.isEmpty(productIntroduce)) {
            Intent intent = new Intent(WelcomeActivity.this, ProduceIntroductionActivity.class);
            startActivity(intent);
            finish();
        } else {
            if(mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            mTimer = new CountDownTimer(SystemConstant.SPLASH_DURATION, 1000) {
                @Override
                public void onFinish() {
                    mTimer.cancel();
                    mTimer = null;

                    startUp(null);
                }

                @Override
                public void onTick(long l) {
                }
            }.start();
        }
    }

    private void startUp(Banner banner) {
        if(null != banner) {
            //push sticky event to show banner
//            EventBus.getDefault().postSticky(new MessageEvent(MessageEvent.SHOW_SPLASH_BANNER, banner));
        }
        String profileInfo = ShareStoreProcess.getInstance().getDataByKey(DBConstant.CURRENT_PROFILE_INFO);
        Intent intent =null;
        if (!TextUtils.isEmpty(profileInfo)) {
            intent = new Intent(WelcomeActivity.this, MainActivity.class);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.SIGN_IN_SUCCESS));
        } else{
            intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

    /*
     * finish the splash page
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void closeWelcome(MessageEvent messageEvent) {
//        if(messageEvent.getCode() == MessageEvent.CLOSE_WELCOME) {
//            finish();
//        }
//    }

    private INetworkResponseListener signInResponseListener = new INetworkResponseListener() {
        @Override
        public void onResponse(String result) {
            // TODO Auto-generated method stub
            /*if (signInNetworkResponse.handleInnerError(result)) {
                MeetownApp.getInstance().updateLoginUserInfo("");
                sendBroadcast(new Intent(SystemConstant.UPDATE_USER_BROADCAST));
            } else {
                handleSignInResult(result);
            }*/
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.showToast(WelcomeActivity.this, getString(R.string.network_error));

            finish();
        }
    };

    private void handleSignInResult(String result) {
        /*try {
            JSONObject responseObj = new JSONObject(result);
            if (responseObj != null) {
                int errorCode = responseObj.optInt("code");
                if (errorCode == 0) {
                    JSONObject userObj = responseObj.optJSONObject("user");
                    if (null != userObj) {
                        String userToken = userObj.optString("userToken");
                        if (TextUtils.isEmpty(userToken)) {
                            userObj.put("userToken", MeetownApp.getInstance().getUserToken());
                        }

                        SubmeterApp.getInstance().updateLoginUserInfo(responseObj.toString());
                        sendBroadcast(new Intent(SystemConstant.UPDATE_USER_BROADCAST));
                        MobclickAgent.onEvent(this, UmengAnalyticsConstant.AUTO_LOGIN);
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    @Override
    public void onDestroy() {
        //cancel eventbus listener
//        EventBus.getDefault().unregister(this);
        unbinder.unbind();

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - firstKeyDownTime > 5000) {
                firstKeyDownTime = System.currentTimeMillis();
                keyRepeatCount = 0;
            }
            ++keyRepeatCount;
            if (keyRepeatCount > 3) {
                SubmeterApp.getInstance().exitApp();
            }

            return true;
        } else {
            keyRepeatCount = 0;
        }

        return super.onKeyDown(keyCode, event);
    }
/*
    private INetworkResponseListener bannerResponseListener = new INetworkResponseListener() {
        @Override
        public void onResponse(String result) {
            // TODO Auto-generated method stub
            synchronized (showMainViewObj) {
                if (showed) {
                    return;
                } else {
                    showed = true;
                }
            }
            try {
                if (bannerNetworkResponse.handleInnerError(result)) {
                    startMainApp(null);
                } else {
                    Banners banners = new Gson().fromJson(result, Banners.class);
                    if (null != banners && null != banners.getBanners() && banners.getBanners().size() > 0) {
                        Banner banner = banners.getBanners().get(0);
                        if (Banner.BANNER_TYPE_IMAGE.equals(banner.getType())) {
                            FileUtils.clearAdCache();
                            splashBannerView = (SimpleDraweeView) findViewById(R.id.splash_view);
                            splashBannerView.setVisibility(View.VISIBLE);
                            splashBannerView.setTag(banner);
                            splashBannerView.setOnClickListener(bannerItemClick);
                            NetworkRequestTool.getInstance().loadImage(Uri.parse(banner.getFileUrl()), splashBannerView);

                            progressBar = (CircleProgressBar) findViewById(R.id.timer_view);
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(100);
                            progressBar.setDuration(3000);
                            progressBar.setCountDownListener(new CircleProgressBar.CountDownListener() {
                                @Override
                                public void onFinish() {
                                    startMainApp(null);
                                }
                            });
                            progressBar.start();
                        } else if (Banner.BANNER_TYPE_VIDEO.equals(banner.getType())) {
                            final String fileUrl = banner.getFileUrl();
                            File cacheFile = MeetownApp.getInstance().getExternalFilesDir(null);
                            final String pathName = cacheFile.getAbsolutePath() + SystemConstant.AD_CACHE_DIR + banner.getId() + fileUrl.substring(fileUrl.lastIndexOf("."));
                            File localFile = new File(pathName);
                            if (localFile.exists()) {
                                showVideoView(banner, pathName);
                            } else {
                                FileUtils.clearAdCache();
                                startMainApp(null);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        FileUtils.downloadFile(fileUrl, pathName, null);
                                    }
                                }).start();
                            }
                        }
                    } else {
                        startMainApp(null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            synchronized (showMainViewObj) {
                if (showed) {
                    return;
                } else {
                    showed = true;
                }
            }

            startUp(null);
        }
    };*/

    private View.OnClickListener bannerItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Object tagObj = v.getTag();
            if (null != tagObj) {
                bannerClicked((Banner) tagObj);
            }
        }
    };

    private void bannerClicked(Banner banner) {
        /*if (null != progressBar) {
            progressBar.cancel();
            progressBar = null;
        }*/

        startUp(banner);
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}