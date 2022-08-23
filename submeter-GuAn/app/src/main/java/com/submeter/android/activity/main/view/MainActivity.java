package com.submeter.android.activity.main.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.MapsInitializer;
import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.main.fragment.home.view.HomeFragment;
import com.submeter.android.activity.main.fragment.monitor.view.MonitorFragment;
import com.submeter.android.activity.main.fragment.me.view.MeFragment;
import com.submeter.android.activity.main.fragment.message.view.MessageFragment;
import com.submeter.android.interfacce.IChildFragment;
import com.submeter.android.tools.NotificationTools;
import com.submeter.android.tools.Utils;
import com.submeter.android.view.SliderPageTransformer;
import com.submeter.android.view.UpgradeDialog;
import com.submeter.android.view.ViewPagerSlide;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements IMainView {

    static String TAG = MainActivity.class.getSimpleName();

    private final int HOME_VIEW = 0;

    private final int CATEGORY_VIEW = 1;

    private final int MESSAGE_VIEW = 2;

    private final int ME_VIEW = 3;

    public static boolean isRunning = false;

    @BindColor(R.color.main_tab_selected_color)
    int mTabSelectedColor;

    @BindColor(R.color.main_tab_normal_color)
    int mTabNormalColor;

    @BindView(R.id.home_tabview)
    TextView mHomeTabView;

    @BindView(R.id.category_tabview)
    TextView mCategoryTabView;

    @BindView(R.id.message_tabview)
    TextView mMessageTabView;

    @BindView(R.id.me_tabview)
    TextView mMeTabView;

    @BindView(R.id.viewPages)
    ViewPagerSlide mViewPager;

    private int mViewIndex = -1;

    private IChildFragment[] childFragments = new IChildFragment[4];

    private long backClickTime = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasFragment = true;
        isRunning = true;
        swipeBackEnable = false;
        setContentView(R.layout.activity_main);

        MapsInitializer.updatePrivacyShow(this,true,true);
        MapsInitializer.updatePrivacyAgree(this,true);
    }

    @Override
    protected void onResume() {
        boolean isFirst = firstEnter;
        super.onResume();

        if (isFirst) {
            initView();
        } else {
            if (null != childFragments[mViewIndex]) {
                childFragments[mViewIndex].pageShow();
            }
        }
    }

    private void initView() {
        //关闭启动页
//        EventBus.getDefault().post(new MessageEvent(MessageEvent.CLOSE_WELCOME));

        mViewPager.setSlide(false);
        mViewPager.setAdapter(new MyPagerAdpter(getSupportFragmentManager()));
        mViewPager.setPageTransformer(true, new SliderPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switchView(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        switchView(HOME_VIEW);
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public void showOpenSettingView() {
        NotificationTools.showOpenSettingView(this);
    }

    @Override
    public void closeNotificationView() {
        NotificationTools.closeNotificationView();
    }

    /*
     * show banner
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventBusMessage(MessageEvent messageEvent) {
//        switch (messageEvent.getCode()) {
//            case MessageEvent.SHOW_SPLASH_BANNER: {
//                Object bannerObj = messageEvent.getData();
//                if (null != bannerObj && bannerObj instanceof Banner) {
//                    Utils.showBanner(this, (Banner) bannerObj);
//                }
//                break;
//            }
//
//            case MessageEvent.SHOW_MAIN_CATEGORY: {
//                switchView(CATEGORY_VIEW);
//                break;
//            }
//
//            default:
//                break;
//        }
//    }

    @Override
    public void showUpgradeDialog(String message, String downloadUrl, String versionCode, boolean isForceUpgrade) {
        UpgradeDialog upgradeDialog = new UpgradeDialog(this, message, versionCode, downloadUrl, isForceUpgrade);
        upgradeDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        for(IChildFragment childFragment : childFragments) {
            if(childFragment != null) {
                childFragment.pageHide();
            }
        }
    }

    @OnClick({R.id.layout_tab_home, R.id.layout_tab_category, R.id.layout_tab_message, R.id.layout_tab_me})
    public void tabClick(View view) {
        switch (view.getId()) {
            case R.id.layout_tab_home: {
                if (mViewPager.getCurrentItem() != HOME_VIEW) {
                    mViewPager.setCurrentItem(HOME_VIEW,false);
                }
                break;
            }

            case R.id.layout_tab_category: {
                if (mViewPager.getCurrentItem() != CATEGORY_VIEW) {
                    mViewPager.setCurrentItem(CATEGORY_VIEW,false);
                }
                break;
            }

            case R.id.layout_tab_message: {
                if (mViewPager.getCurrentItem() != MESSAGE_VIEW) {
                    mViewPager.setCurrentItem(MESSAGE_VIEW,false);
                }
                break;
            }

            case R.id.layout_tab_me: {
                if (mViewPager.getCurrentItem() != ME_VIEW) {
                    mViewPager.setCurrentItem(ME_VIEW,false);
                }
                break;
            }

            default: {
                break;
            }
        }
    }

    private void switchView(int type) {
        if (type == mViewIndex) {
            return;
        }

        mHomeTabView.setTextColor(mTabNormalColor);
        mCategoryTabView.setTextColor(mTabNormalColor);
        mMessageTabView.setTextColor(mTabNormalColor);
        mMeTabView.setTextColor(mTabNormalColor);

        mHomeTabView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.home, 0, 0);
        mCategoryTabView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.monitor, 0, 0);
        mMessageTabView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.message, 0, 0);
        mMeTabView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.me, 0, 0);

        for(int i = 0; i < childFragments.length; ++i) {
            if(childFragments[i] != null) {
                if (i == type) {
                    setStatusBar(childFragments[i].getStatusbarDardMode());
                    childFragments[i].pageShow();
                } else {
                    childFragments[i].pageHide();
                }
            }
        }
        if (type == HOME_VIEW) {
            mHomeTabView.setTextColor(mTabSelectedColor);
            mHomeTabView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.home_active, 0, 0);
        } else if (type == CATEGORY_VIEW) {
            mCategoryTabView.setTextColor(mTabSelectedColor);
            mCategoryTabView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.monitor_active, 0, 0);
        } else if (type == MESSAGE_VIEW) {
            mMessageTabView.setTextColor(mTabSelectedColor);
            mMessageTabView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.message_active, 0, 0);
        } else if (type == ME_VIEW) {
            mMeTabView.setTextColor(mTabSelectedColor);
            mMeTabView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.me_active, 0, 0);
        } else {
            return;
        }

        mViewIndex = type;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mViewIndex == HOME_VIEW) {
                if (System.currentTimeMillis() - backClickTime > 3000) {
                    Utils.showToast(this, String.format(getString(R.string.exit_toast), getString(R.string.app_name)));
                    backClickTime = System.currentTimeMillis();
                } else {
                    finish();
                    SubmeterApp.getInstance().exitApp();
                }
            } else if (null != mViewPager) {
                mViewPager.setCurrentItem(HOME_VIEW, true);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        isRunning = false;
//        EventBus.getDefault().unregister(this);

//        Utils.showToast(this, "main activity is closed");

        super.onDestroy();
    }

    public int getCurrentViewPage() {
        return mViewIndex;
    }

    public void goMonitorFragment(){
        mViewPager.setCurrentItem(CATEGORY_VIEW,false);
    }

    public void goMessageFragment(){
        mViewPager.setCurrentItem(MESSAGE_VIEW,false);
    }


    public class MyPagerAdpter extends FragmentPagerAdapter {

        public MyPagerAdpter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            if (null == childFragments[position]) {
                if (position == 0) {
                    childFragments[position] = new HomeFragment();
                } else if (position == 1) {
                    childFragments[position] = new MonitorFragment();
                } else if (position == 2) {
                    childFragments[position] = new MessageFragment();
                } else if (position == 3) {
                    childFragments[position] = new MeFragment();
                }
            }

            return childFragments[position].getFragment();
        }

        @Override
        public int getCount() {
            return childFragments.length;
        }
    }
}