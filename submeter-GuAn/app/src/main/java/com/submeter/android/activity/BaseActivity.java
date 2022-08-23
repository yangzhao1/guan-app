package com.submeter.android.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.activity.login.view.LoginActivity;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.eventbus.MessageEvent;
import com.submeter.android.interfacce.IBaseView;
import com.submeter.android.tools.DeviceUtils;
import com.submeter.android.tools.JSCallback;
import com.submeter.android.tools.PermissionUtils;
import com.submeter.android.tools.SharedPreferencesUtils;
import com.submeter.android.tools.Utils;
import com.submeter.android.updateApk.DownloadManagerUtil;
import com.submeter.swipebacklib.SwipeBackActivity;
import com.submeter.swipebacklib.SwipeBackLayout;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends SwipeBackActivity implements IBaseView, OnClickListener {

    protected final int REQUEST_APPLY = 2;

    protected final int REQUEST_FAVORITE = 3;

    protected final int REQUEST_QUESTION = 4;

    protected boolean firstEnter = true;

    private boolean isFullScreen = false;

    protected boolean hasFragment = false;

    protected boolean swipeBackEnable = true;

    private boolean statusbarTranlucent = false;

    protected int signinRequestType;

    private long lastClickTime;

    protected boolean darkStatusBar = true;

    @Nullable
    @BindView(R.id.left_title_btn)
    View leftTitleBtn;

    @Nullable
    @BindView(R.id.right_title_btn)
    View rightTitleBtn;

    @Nullable
    @BindView(R.id.left_image)
    ImageView leftImageView;

    @Nullable
    @BindView(R.id.left_text)
    TextView leftTextView;

    @Nullable
    @BindView(R.id.right_image)
    ImageView rightImageView;

    @Nullable
    @BindView(R.id.right_text)
    TextView rightTextView;

    @Nullable
    @BindView(R.id.text_title)
    TextView titleView;

    @Nullable
    @BindView(R.id.title_search)
    TextView titleSearch;

    private Unbinder unbinder;

    private View loadingDlgView;

    private Dialog loadingDlg;

    protected PermissionUtils.IPermissionRequestCallback permissionRequestCallback;
    private boolean isTopActivity = false;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    /**
     * 在{@link Activity#setContentView}之后调用
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setStatusBar(boolean darkMode) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setStatusBarTranslucent();
                if (darkMode) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    int flag = getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    getWindow().getDecorView().setSystemUiVisibility(flag);
                }
            }

            if (DeviceUtils.isMIUI() && setMiuiStatusBarDarkMode(this, darkMode)) {
                setStatusBarTranslucent();
                return;
            }

            if (DeviceUtils.isMeizuFlyme() && setMeizuStatusBarDarkIcon(this, darkMode)) {
                setStatusBarTranslucent();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStatusBarTranslucent() {
        statusbarTranlucent = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //设置自定义statusbar的高度，为了漏出系统的statusbar
        View statusbarView = findViewById(R.id.statusbar_view);
        if (null != statusbarView) {
            statusbarView.getLayoutParams().height = getStatusBarHeight();
        }
    }

    public boolean isStatusbarTranlucent() {
        return statusbarTranlucent;
    }

    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isTopActivity = true;
        firstEnter = false;
        MobclickAgent.onResume(this);
        if (!hasFragment) {
            MobclickAgent.onPageStart(getLocalClassName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isTopActivity = false;
        MobclickAgent.onPause(this);
        if (!hasFragment) {
            MobclickAgent.onPageEnd(getLocalClassName());
        }
    }

    public void setContentView(int layoutResID) {
        setContentView(layoutResID, -1, -1, -1, false);
    }

    public void setContentView(int layoutResID, int titleTextValue, int leftBtnImgRes, int rightBtnImgRes, boolean isShowSearchView) {
        setContentView(layoutResID, titleTextValue, leftBtnImgRes, null, rightBtnImgRes, null, isShowSearchView);
    }

    public void setContentView(int layoutResID, int titleTextValue, int leftBtnImgRes, String leftText, boolean isShowSearchView) {
        setContentView(layoutResID, titleTextValue, leftBtnImgRes, leftText, -1, null, isShowSearchView);
    }

    public void setContentView(int layoutResID, int titleTextValue, int leftBtnImgRes, String leftText, int rightBtnImgRes, boolean isShowSearchView) {
        setContentView(layoutResID, titleTextValue, leftBtnImgRes, leftText, rightBtnImgRes, null, isShowSearchView);
    }

    public void setContentView(int layoutResID, int titleTextValue, int leftBtnImgRes, int rightBtnImgRes, String rightText, boolean isShowSearchView) {
        setContentView(layoutResID, titleTextValue, leftBtnImgRes, null, rightBtnImgRes, rightText, isShowSearchView);
    }

    public void setContentView(int layoutResID, int titleTextValue, int leftBtnImgRes, String leftText, int rightBtnImgRes, String rightText, boolean isShowSearchView) {
        super.setContentView(layoutResID);

        unbinder = ButterKnife.bind(this);

        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        if (swipeBackEnable) {
            /*add swipe oritention*/
            swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        } else {
            swipeBackLayout.setEnableGesture(false);
        }
        setStatusBar(darkStatusBar);

        if(titleSearch != null && titleView != null) {
            if (isShowSearchView) {
                titleSearch.setVisibility(View.VISIBLE);
                titleSearch.setOnClickListener(this);
                titleView.setVisibility(View.GONE);
            }else{
                titleSearch.setVisibility(View.GONE);
                titleView.setVisibility(View.VISIBLE);
                if (titleTextValue != -1) {
                    if (titleView != null) {
                        titleView.setText(getString(titleTextValue));
                    }
                }
            }
        }

        if (leftTitleBtn != null) {
            if (leftBtnImgRes != -1 || !TextUtils.isEmpty(leftText)) {
                showLeftTitleBtn(leftBtnImgRes, leftText);
            } else {
                hideLeftImageBtn();
            }
        }

        if (null != rightTitleBtn) {
            if (rightBtnImgRes != -1 || !TextUtils.isEmpty(rightText)) {
                showRightTitleBtn(rightBtnImgRes, rightText);
            } else {
                hideRightImageBtn();
            }
        }
    }

    protected void showLeftTitleBtn(int leftBtnImgRes, String leftText) {
        if (null == leftTitleBtn) {
            return;
        }

        if (-1 == leftBtnImgRes && TextUtils.isEmpty(leftText)) {
            leftTitleBtn.setVisibility(View.GONE);
        } else {
            if (leftBtnImgRes != -1) {
                leftImageView.setImageResource(leftBtnImgRes);
                leftImageView.setVisibility(View.VISIBLE);
            } else {
                leftImageView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(leftText)) {
                leftTextView.setText(leftText);
                leftTextView.setVisibility(View.VISIBLE);
            } else {
                leftTextView.setVisibility(View.GONE);
            }
            leftTitleBtn.setVisibility(View.VISIBLE);
            leftTitleBtn.setOnClickListener(this);
        }
    }

    protected void showRightTitleBtn(int rightBtnImgRes, String rightText) {
        if (null == rightTitleBtn) {
            return;
        }

        if (-1 == rightBtnImgRes && TextUtils.isEmpty(rightText)) {
            rightTitleBtn.setVisibility(View.GONE);
        } else {
            if (null == rightImageView) {
                rightImageView = (ImageView) findViewById(R.id.right_image);
            }

            if (rightBtnImgRes != -1) {
                rightImageView.setImageResource(rightBtnImgRes);
                rightImageView.setVisibility(View.VISIBLE);
            } else {
                rightImageView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(rightText)) {
                rightTextView.setText(rightText);
                rightTextView.setVisibility(View.VISIBLE);
            } else {
                rightTextView.setVisibility(View.GONE);
            }

            rightTitleBtn.setVisibility(View.VISIBLE);
            if (rightBtnImgRes == R.drawable.ic_more) {
                rightTitleBtn.setOnClickListener(view -> popupOverflowMenu(view));
            } else {
                rightTitleBtn.setOnClickListener(this);
            }
        }
    }

    protected void hideLeftImageBtn() {
        if (leftTitleBtn != null) {
            leftTitleBtn.setVisibility(View.GONE);
        }
    }

    public void hideRightImageBtn() {
        if (rightTitleBtn != null) {
            rightTitleBtn.setVisibility(View.GONE);
        }
    }

    protected void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);

        isFullScreen = true;
    }

    protected void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);

        isFullScreen = false;
    }

    @Override
    public BaseActivity getBaseActivity() {
        return this;
    }

    @Override
    public void showLoadingView() {
        try {
            if (loadingDlg == null) {
                loadingDlg = createLoadingDialog();
            }

            loadingDlg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideLoadingView() {
        try {
            if (null != loadingDlgView) {
                ImageView imageView = (ImageView) loadingDlgView.findViewById(R.id.progressbar);
                AnimationDrawable ad = (AnimationDrawable) imageView.getDrawable();
                ad.stop();
            }

            if (loadingDlg != null && loadingDlg.isShowing()) {
                loadingDlg.dismiss();
            }
            loadingDlg = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToast(String toast) {
        Utils.showToast(this, toast);
    }

    public Dialog createLoadingDialog() {
        if (null == loadingDlgView) {
            loadingDlgView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null);
        } else {
            ((ViewGroup) loadingDlgView.getParent()).removeAllViews();
        }

        ImageView imageView = (ImageView) loadingDlgView.findViewById(R.id.progressbar);
        AnimationDrawable ad = (AnimationDrawable) imageView.getDrawable();
        ad.start();

        Dialog loadingDialog = new Dialog(this, R.style.loading_dialog);
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    hideLoadingView();
                }
                return false;
            }
        });
        loadingDialog.setCancelable(false);
        int loadingWidth = Utils.dip2px(this, 80);
        loadingDialog.setContentView(loadingDlgView, new LinearLayout.LayoutParams(loadingWidth, loadingWidth));
        return loadingDialog;
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.left_title_btn: {
                hideSoftKeyboard();
                finish();
                break;
            }
            case R.id.title_search: {
                Utils.gotoSearch(this);
                break;
            }

            default:
                break;
        }
    }

    /*public void registerExitBrsoadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SystemConstant.SEND_EXIT_BROADCAST);
        registerReceiver(broadcastReceiver, filter);
    }*/

    /*protected void registerSigninsBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SystemConstant.SEND_SIGNIN_BROADCAST);
        registerReceiver(broadcastReceiver, filter);
    }

    protected void registerSigsnoutBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SystemConstant.SEND_SIGNOUT_BROADCAST);
        registerReceiver(broadcastReceiver, filter);
    }

    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SystemConstant.SEND_EXIT_BROADCAST.equals(action)) {
                finish();
            } else if (SystemConstant.SEND_SIGNIN_BROADCAST.equals(action)) {
                handleSigninBroadcast();
            } else if (SystemConstant.SEND_SIGNOUT_BROADCAST.equals(action)) {
                handleSignoutBroadcast();
            }
        }
    };*/

    /*protected void handleSigninBroadcast() {
        finish();
    }

    protected void handleSignoutBroadcast() {
        finish();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null) {
            unbinder.unbind();
        }
        System.gc();
        //unregisterReceiver(broadcastReceiver);
    }

    public boolean isKeyboardShown() {
        boolean isShown = false;
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Application.INPUT_METHOD_SERVICE);
            if (imm != null) {
                isShown = imm.isActive();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return isShown;
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Application.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void showSoftKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, 0);
        }
    }

    @Override
    public void finish() {
        hideSoftKeyboard();
        super.finish();
        overridePendingTransition(R.anim.slide_from_left_enter, R.anim.slide_from_right_exit);
    }

    public void finish(boolean fadeOut) {
        hideSoftKeyboard();
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_from_right_enter, R.anim.slide_from_left_exit);
    }

    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right_enter, R.anim.slide_from_left_exit);
    }

    public void startActivityFadeIn(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /*固定APP字体大小，保证不随系统设置改变*/
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    protected void initWebView(final WebView webView, JSCallback jsCallback) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//可用JS
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBlockNetworkImage(true);
        int fontSize = (int) getResources().getDimension(R.dimen.font_12_size);
        webSettings.setDefaultFontSize(fontSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //remove the searchBoxJavaBridge, avoid js injection.
        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            try {
                webView.removeJavascriptInterface("searchBoxJavaBridge_");
                webView.removeJavascriptInterface("accessibility");
                webView.removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }

        webView.setBackgroundColor(0); // 设置背景色
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        if (null != jsCallback) {
            webView.addJavascriptInterface(jsCallback, "imageListener");
        }
    }

    protected void removeWebviewCache(WebView webView) {
        if (null != webView) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再destory()
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();

            try {
                webView.destroy();
            } catch (Throwable ex) {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                if (null != permissionRequestCallback) {
                    permissionRequestCallback.permissionGranted(permissions[i]);
                }
            } else {
                if (null != permissionRequestCallback) {
                    permissionRequestCallback.permissionDenied(permissions[i]);
                }
            }
        }
    }

    protected void handleGoBackEvent() {
        if (!SubmeterApp.getInstance().isAppRunning()) {
            Intent startIntent = new Intent();
            startIntent.setClassName(getPackageName(), getPackageName() + ".activity.WelcomeActivity");
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startIntent);
        } else {
            setResult(RESULT_OK);
        }
        finish();
    }

    /**
     * 弹出自定义溢出菜单
     */
    public void popupOverflowMenu(View view) {
        // 获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // 状态栏高度 frame.top
        int xOffset = frame.top + Utils.dip2px(this, 46) + Utils.dip2px(this, -12f); // 减去阴影宽度，适配UI
        int yOffset = Utils.dip2px(this, 6f); // 设置x方向offset为5dp

        View popView = getLayoutInflater().inflate(R.layout.menu_popup, null);
        TextView tvMessage = (TextView) popView.findViewById(R.id.tv_menu_message);
        TextView tvHome = (TextView) popView.findViewById(R.id.tv_menu_home);
        TextView tvSearch = (TextView) popView.findViewById(R.id.tv_menu_search);
        TextView tvAttention = (TextView) popView.findViewById(R.id.tv_menu_attention);

        // popView即popupWindow布局
        PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true); // 点击外部关闭
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置Gravity,让它显示在右上角
        popupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, yOffset, xOffset);

        tvMessage.setOnClickListener(v -> {
            Utils.showToast(this, "您点击了消息");
            popupWindow.dismiss();
        });
        tvHome.setOnClickListener(v -> {
            Utils.showToast(this, "您点击了首页");
            popupWindow.dismiss();
        });
        tvSearch.setOnClickListener(v -> {
            Utils.showToast(this, "您点击了搜索");
            popupWindow.dismiss();
        });
        tvAttention.setOnClickListener(v -> {
            Utils.showToast(this, "您点击了我的关注");
            popupWindow.dismiss();
        });

    }

    public void setTitleSearchText(String keyWords) {
        if(titleSearch != null) {
            titleSearch.setText(keyWords);
        }
    }

    public void showNoticeDialog(String str, final String url) {
        DownloadManagerUtil downloadManagerUtil = new DownloadManagerUtil(getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        builder.setTitle(String.format(getString(R.string.app_name)));
        builder.setMessage(str);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showToast("正在下载中...");
                long downloadId = (long) SharedPreferencesUtils.getParam(getApplicationContext(),"downloadId",0l);
                if (downloadId!=0){
                    downloadManagerUtil.clearCurrentTask(downloadId);
                }
                downloadId = downloadManagerUtil.download(url, SystemConstant.APK_NAME,"点击安装");
                SharedPreferencesUtils.setParam(getApplicationContext(),"downloadId",downloadId);
            }
        });
        builder.setNegativeButton("暂不更新",(dialog,which)->{
                dialog.dismiss();
        });
        builder.create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if(isTopActivity && messageEvent.getCode() == MessageEvent.TOKEN_EXPIRED) {
            SubmeterApp.getInstance().handleSignOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}