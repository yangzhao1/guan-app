package com.submeter.android.activity.main.fragment.message.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.view.DisposeMsgFragment;
import com.submeter.android.activity.main.fragment.message.view.fragment.systemMsg.view.SystemMsgFragment;
import com.submeter.android.interfacce.IChildFragment;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.tools.Utils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MessageFragment extends Fragment implements IChildFragment, IMessageView {

    public static final String TAG = MessageFragment.class.getSimpleName();
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.sep_line)
    View sepLine;
    @BindView(R.id.systemMsg)
    TextView systemMsg;
    @BindView(R.id.systemMsg_indicator)
    View systemMsgIndicator;
    @BindView(R.id.warning_msg)
    TextView warning_msg;
    @BindView(R.id.warning_msg_indicator)
    View warning_msgIndicator;
    @BindView(R.id.content_layout)
    FrameLayout contentLayout;
    @BindColor(R.color.blue_color)
    int selectedTextColor;

    @BindColor(R.color.black_color)
    int unselectedTextColor;
    private boolean isFirst = true;

    private boolean isHidden = true;

    private int unsignInClickedViewId = -1;

    private View contentView;

    private BaseActivity parentActivity;
    private Unbinder unbinder;

    private final int SYSTEM_TAB = 2;
    private final int DISPOSE_TAB = 1;
    private FragmentManager fragmentManager;

    private IChildFragment systemMsgFragment;
    private IChildFragment disposeMsgFragment;
    //当前选中的tab view
    private int currentTabView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (BaseActivity) getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = Utils.inflateView(parentActivity, inflater, R.layout.activity_message, container, false);
        } else {
            ViewGroup p = (ViewGroup) contentView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }

        unbinder = ButterKnife.bind(this, contentView);
        textTitle.setText("消息");

        return contentView;
    }

    private void initView() {
        initStatusView();
        fragmentManager = getChildFragmentManager();
        switchTabView(DISPOSE_TAB);
    }

    @BindView(R.id.statusbar_view)
    View statusbarView;

    private void initStatusView() {
        View statusbarView = contentView.findViewById(R.id.statusbar_view);
        statusbarView.getLayoutParams().height = parentActivity.getStatusBarHeight();
        statusbarView.setBackgroundColor(getResources().getColor(R.color.blue_color));
    }

    private void switchTabView(int view) {
        if (view == currentTabView) {
            return;
        }

        this.currentTabView = view;
        if (view == SYSTEM_TAB) {
            systemMsg.setTextColor(selectedTextColor);
            systemMsgIndicator.setVisibility(View.VISIBLE);

            warning_msg.setTextColor(unselectedTextColor);
            warning_msgIndicator.setVisibility(View.GONE);

            if (disposeMsgFragment != null) {
                disposeMsgFragment.pageHide();
            }

            if (fragmentManager == null) {
                fragmentManager = getChildFragmentManager();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (systemMsgFragment == null) {
                systemMsgFragment = new SystemMsgFragment();
            }

            fragmentTransaction.replace(R.id.content_layout, systemMsgFragment.getFragment());
            systemMsgFragment.pageShow();
            fragmentTransaction.commit();

        } else if (view == DISPOSE_TAB) {
            warning_msg.setTextColor(selectedTextColor);
            warning_msgIndicator.setVisibility(View.VISIBLE);

            systemMsg.setTextColor(unselectedTextColor);
            systemMsgIndicator.setVisibility(View.GONE);

            if (systemMsgFragment != null) {
                systemMsgFragment.pageHide();
            }

            if (fragmentManager == null) {
                fragmentManager = getChildFragmentManager();
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (disposeMsgFragment == null) {
                disposeMsgFragment = new DisposeMsgFragment();
            }

            fragmentTransaction.replace(R.id.content_layout, disposeMsgFragment.getFragment());
            disposeMsgFragment.pageShow();
            fragmentTransaction.commit();
        }
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

        isHidden = false;
        if (isFirst) {
            initView();
        } else {
            /*updateUserInfo();
            if(userStatusChanged && !MeetownApp.getInstance().isVisitor()) {
                refreshNewNotification();
            }*/
        }
        isFirst = false;
    }

    public void pageHide() {
        if (isHidden) {
            return;
        }

        isHidden = true;
    }

    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            parentActivity.onClick(v);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        /*if (requestCode == SystemConstant.REQUEST_RESET_PROFILE) {
            updateUserInfo();
        }*/
    }

    public boolean getStatusbarDardMode() {
        return true;
    }

    private INetworkResponseListener getNewNotificationResponseListener = new INetworkResponseListener() {
        @Override
        public void onResponse(String result) {
            handleNewNotificationResult(result);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
        }
    };

    private void handleNewNotificationResult(String result) {
        /*NotifyCategories notifyCategories = new Gson().fromJson(result, NotifyCategories.class);
        if(null != notifyCategories) {
            MeetownApp.getInstance().setLastestNotifications(notifyCategories);
            int newRemindNotificationCount = MeetownApp.getInstance().getNewRemindNotifyCount();
            int newRecommendNotificationCount = MeetownApp.getInstance().getNewRecommendNotifyCount();
            int newReplyNotificationCount = MeetownApp.getInstance().getNewReplyNotifyCount();
            int newSystemNotificationCount = MeetownApp.getInstance().getNewSystemNotifyCount();
            int count = newRemindNotificationCount + newRecommendNotificationCount + newReplyNotificationCount + newSystemNotificationCount;
            if (count > 0) {
                newNotificationFlagView.setVisibility(View.VISIBLE);
            } else {
                newNotificationFlagView.setVisibility(View.GONE);
            }
        }*/
    }

    @OnClick({R.id.systemMsg, R.id.warning_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.systemMsg:
                switchTabView(SYSTEM_TAB);
                break;
            case R.id.warning_msg:
                switchTabView(DISPOSE_TAB);
                break;
        }
    }
}