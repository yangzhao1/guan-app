package com.submeter.android.activity.main.fragment.monitor.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.main.fragment.monitor.fragment.all.view.AllFragment;
import com.submeter.android.activity.main.fragment.monitor.fragment.comp.view.CompFragment;
import com.submeter.android.activity.main.fragment.monitor.fragment.getOut.view.GetOutFragment;
import com.submeter.android.activity.main.fragment.monitor.fragment.processed.view.ProcessFragment;
import com.submeter.android.activity.main.fragment.monitor.presenter.IMonitorPresenter;
import com.submeter.android.activity.main.fragment.monitor.presenter.MonitorPresenter;
import com.submeter.android.interfacce.IChildFragment;
import com.submeter.android.interfacce.IChildMFragment;
import com.submeter.android.tools.Utils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MonitorFragment extends Fragment implements IChildFragment, IMonitorView {

    public static final String TAG = MonitorFragment.class.getSimpleName();

    private final int ALL_TAB = 1;
    private final int GETOUT_TAB = 4;
    private final int COMP_TAB = 2;
    private final int PRECESS_TAB = 3;
    @BindView(R.id.statusbar_view)
    View statusbarView;
    @BindView(R.id.text_title)
    TextView textTitle;

    private boolean isFirst = true;

    private boolean isHidden = true;

    //当前选中的tab view
    private int currentTabView;

    private View contentView;

    private BaseActivity parentActivity;

    @BindColor(R.color.blue_color)
    int selectedTextColor;

    @BindColor(R.color.black_color)
    int unselectedTextColor;

    @BindView(R.id.all)
    TextView all;

    @BindView(R.id.getOut)
    TextView getOut;

    @BindView(R.id.comp)
    TextView comp;

    @BindView(R.id.precess)
    TextView precess;

    @BindView(R.id.all_indicator)
    View allIndicatorView;

    @BindView(R.id.getOut_indicator)
    View getOutIndicatorView;

    @BindView(R.id.comp_indicator)
    View compIndicatorView;

    @BindView(R.id.precess_indicator)
    View precess_indicator;

    private FragmentManager fragmentManager;

    private Unbinder unbinder;

    private IMonitorPresenter monitorPresenter;

    private IChildMFragment allFragment;

    private IChildMFragment precessFragment;

    private IChildMFragment getOutFragment;

    private IChildMFragment compFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (BaseActivity) getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = Utils.inflateView(parentActivity, inflater, R.layout.activity_categorynew, container, false);
            initStatusView();
        } else {
            ViewGroup p = (ViewGroup) contentView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }

        unbinder = ButterKnife.bind(this, contentView);

        return contentView;
    }

    private void initView() {
        textTitle.setText(R.string.monitor);
        monitorPresenter = new MonitorPresenter(this);
        fragmentManager = getChildFragmentManager();

        initStatusView();
        switchTabView(ALL_TAB);
    }

    private void switchTabView(int view) {
        if (view == currentTabView) {
            return;
        }
        this.currentTabView = view;
        if (view == ALL_TAB) {
            all.setTextColor(selectedTextColor);
            allIndicatorView.setVisibility(View.VISIBLE);

            getOut.setTextColor(unselectedTextColor);
            getOutIndicatorView.setVisibility(View.GONE);

            comp.setTextColor(unselectedTextColor);
            compIndicatorView.setVisibility(View.GONE);

            precess.setTextColor(unselectedTextColor);
            precess_indicator.setVisibility(View.GONE);
            if (getOutFragment != null) {
                getOutFragment.pageHide();
            }

            if (compFragment != null) {
                compFragment.pageHide();
            }

            if (precessFragment != null) {
                precessFragment.pageHide();
            }

            if (fragmentManager == null) {
                fragmentManager = getChildFragmentManager();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (allFragment == null) {
                allFragment = new AllFragment();
            }

            fragmentTransaction.replace(R.id.content_layout, allFragment.getFragment());
//            allFragment.pageShow();
            fragmentTransaction.commit();
        } else if (view == GETOUT_TAB) {
            getOut.setTextColor(selectedTextColor);
            getOutIndicatorView.setVisibility(View.VISIBLE);

            all.setTextColor(unselectedTextColor);
            allIndicatorView.setVisibility(View.GONE);

            comp.setTextColor(unselectedTextColor);
            compIndicatorView.setVisibility(View.GONE);
            precess.setTextColor(unselectedTextColor);
            precess_indicator.setVisibility(View.GONE);
            if (allFragment != null) {
                allFragment.pageHide();
            }
            if (compFragment != null) {
                compFragment.pageHide();
            }
            if (precessFragment != null) {
                precessFragment.pageHide();
            }
            if (fragmentManager == null) {
                fragmentManager = getChildFragmentManager();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (getOutFragment == null) {
                getOutFragment = new GetOutFragment();
            }

            fragmentTransaction.replace(R.id.content_layout, getOutFragment.getFragment());
//            getOutFragment.pageShow();
            fragmentTransaction.commit();
        } else if (view == COMP_TAB) {
            getOut.setTextColor(unselectedTextColor);
            getOutIndicatorView.setVisibility(View.GONE);

            all.setTextColor(unselectedTextColor);
            allIndicatorView.setVisibility(View.GONE);

            comp.setTextColor(selectedTextColor);
            compIndicatorView.setVisibility(View.VISIBLE);
            precess.setTextColor(unselectedTextColor);
            precess_indicator.setVisibility(View.GONE);
            if (allFragment != null) {
                allFragment.pageHide();
            }
            if (precessFragment != null) {
                precessFragment.pageHide();
            }
            if (getOutFragment != null) {
                getOutFragment.pageHide();
            }

            if (fragmentManager == null) {
                fragmentManager = getChildFragmentManager();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (compFragment == null) {
                compFragment = new CompFragment();
            }

            fragmentTransaction.replace(R.id.content_layout, compFragment.getFragment());
//            compFragment.pageShow();
            fragmentTransaction.commit();
        } else if (view == PRECESS_TAB) {
            getOut.setTextColor(unselectedTextColor);
            getOutIndicatorView.setVisibility(View.GONE);

            all.setTextColor(unselectedTextColor);
            allIndicatorView.setVisibility(View.GONE);

            comp.setTextColor(unselectedTextColor);
            compIndicatorView.setVisibility(View.GONE);

            precess.setTextColor(selectedTextColor);
            precess_indicator.setVisibility(View.VISIBLE);
            if (allFragment != null) {
                allFragment.pageHide();
            }

            if (getOutFragment != null) {
                getOutFragment.pageHide();
            }

            if (compFragment != null) {
                compFragment.pageHide();
            }

            if (fragmentManager == null) {
                fragmentManager = getChildFragmentManager();
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (precessFragment == null) {
                precessFragment = new ProcessFragment();
            }

            fragmentTransaction.replace(R.id.content_layout, precessFragment.getFragment());
//            compFragment.pageShow();
            fragmentTransaction.commit();
        }
    }

    private void initStatusView() {
        View statusbarView = contentView.findViewById(R.id.statusbar_view);
        statusbarView.getLayoutParams().height = parentActivity.getStatusBarHeight();
        statusbarView.setBackgroundColor(getResources().getColor(R.color.blue_color));
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

    public void pageShow() {
        if (!isHidden) {
            return;
        }

        isHidden = false;
        if (isFirst) {
            initView();
        }
        isFirst = false;
    }

    public void pageHide() {
        if (isHidden) {
            return;
        }

        isHidden = true;
    }

    @OnClick({R.id.all, R.id.getOut, R.id.comp,R.id.precess})
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.all: {
                switchTabView(ALL_TAB);
                break;
            }

            case R.id.getOut: {
                switchTabView(GETOUT_TAB);
                break;
            }

            case R.id.comp: {
                switchTabView(COMP_TAB);
                break;
            }

            case R.id.precess: {
                switchTabView(PRECESS_TAB);
                break;
            }
            default:
                break;
        }
    }

    public boolean getStatusbarDardMode() {
        return true;
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
}