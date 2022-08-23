package com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.presenter.DisposeMsgPresenter;
import com.submeter.android.activity.main.fragment.message.view.fragment.WarningMsg.presenter.IDisposeMsgPresenter;
import com.submeter.android.activity.messageDetail.view.MessageDetailActivity;
import com.submeter.android.adapter.MsgAdapter;
import com.submeter.android.adapter.SpaceItemDecoration;
import com.submeter.android.entity.WarningMessage;
import com.submeter.android.interfacce.IChildFragment;
import com.submeter.android.tools.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DisposeMsgFragment extends Fragment implements IChildFragment, IDisposeMsgView {

    public static final String TAG = DisposeMsgFragment.class.getSimpleName();
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.rel)
    RelativeLayout rel;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.nodata)
    TextView nodata;

    private boolean isFirst = true;
    private boolean isHidden = true;

    private View contentView;

    private BaseActivity parentActivity;

    private Unbinder unbinder;

    private IDisposeMsgPresenter presenter;

    private MsgAdapter adapter =null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (BaseActivity) getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = Utils.inflateView(parentActivity, inflater, R.layout.fragment_warningmsg, container, false);
            unbinder = ButterKnife.bind(this, contentView);
            initView();
        } else {
            ViewGroup p = (ViewGroup) contentView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
            unbinder = ButterKnife.bind(this, contentView);
        }
        return contentView;
    }

    private void initView() {
        presenter = new DisposeMsgPresenter(this);
        presenter.loadData();

        srlRefresh.setOnRefreshListener(refreshLayout -> presenter.onRefresh());
        srlRefresh.setOnLoadMoreListener(refreshLayout -> presenter.onLoadMore());
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void updateView(List list) {
        handleRefreshView();
        if (list != null && list.size() != 0) {
            isFirst=false;
            nodata.setVisibility(View.GONE);
            if (adapter!=null){
                adapter.notifyDataSetChanged();
            }else {
                LinearLayoutManager manager = new LinearLayoutManager(parentActivity);
                recycler.setLayoutManager(manager);
                recycler.addItemDecoration(new SpaceItemDecoration(10));
                adapter = new MsgAdapter(parentActivity,list);
                recycler.setAdapter(adapter);
                adapter.setOnItemClickListener((view, pos) -> {
                    WarningMessage message = (WarningMessage) list.get(pos);
                    Intent intent = new Intent(parentActivity, MessageDetailActivity.class);
                    intent.putExtra("title",message.getLevel());
                    intent.putExtra("time",message.getCreateTime());
                    intent.putExtra("content",message.getWarnObj());
                    startActivity(intent);
                });
            }
        }else {
            if (presenter.isRefresh()||isFirst){
                if (isFirst){
                    isFirst = false;
                }
                nodata.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 处理加载后的视图刷新
     */
    private void handleRefreshView() {
        if (presenter.isRefresh()) {
            srlRefresh.finishRefresh();
            srlRefresh.setNoMoreData(false);
        } else {
            srlRefresh.finishLoadMore();
        }
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
            //initView();
        }
        isFirst = false;
    }

    public void pageHide() {
        if (isHidden) {
            return;
        }

        isHidden = true;
    }

    @Override
    public boolean getStatusbarDardMode() {
        return false;
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

        /*if(null != headUnbinder) {
            headUnbinder.unbind();
        }*/
    }

    @Override
    public BaseActivity getBaseActivity() {
        return parentActivity;
    }

}