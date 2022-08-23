package com.submeter.android.activity.invitation.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.invitation.params.InvitationParam;
import com.submeter.android.activity.invitation.presenter.IInvitationPresenter;
import com.submeter.android.activity.invitation.presenter.InvitationPresenter;
import com.submeter.android.entity.Invitation;
import com.submeter.android.entity.InvitationCategory;
import com.submeter.android.entity.InvitationStatus;
import com.submeter.android.entity.Sort;
import com.submeter.android.tools.Utils;
import com.submeter.android.view.MarginDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 招投标列表
 *
 * @author thm
 * @date 2018/12/6
 */
public class InvitationActivity extends BaseActivity implements IInvitationView {

    @BindView(R.id.srl_refresh)
    SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R.id.rcv_content)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_invitation_filter)
    TextView mTvFilter;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    LinearLayout mNavigationView;

    @BindView(R.id.ll_invitation_status)
    LinearLayout mInvitationStatus;

    @BindView(R.id.ll_invitation_date)
    LinearLayout mInvitationDate;

    @BindView(R.id.ll_invitation_categroy)
    LinearLayout mInvitationCategroy;

    @BindView(R.id.tv_common_item_start_date)
    TextView mInvitationStartDate;

    @BindView(R.id.tv_common_item_end_date)
    TextView mInvitationEndDate;

    @BindView(R.id.tv_invitation_composite)
    TextView mTvInvitationComposite;

    @BindView(R.id.tv_invitation_date)
    TextView mTvInvitationTime;

    private IInvitationPresenter mInvitationPresenter;

    private BaseQuickAdapter<Invitation, BaseViewHolder> mInvitationListAdapter;

    /**
     * 招投标列表
     */
    private List<Invitation> mInvitations;
    /**
     * 招标阶段列表
     */
    private List<InvitationStatus> mInvitationStatusList;

    /**
     * 招标阶段列表
     */
    private List<InvitationCategory> mInvitationCategoryList;

    /**
     * 状态筛选条件
     */
    private List<String> mFilterStatusParams = new ArrayList<>();

    /**
     * 分类筛选条件
     */
    private List<String> mFilterCategoryParams = new ArrayList<>();

    private BaseQuickAdapter mFilterStatusAdapter;

    private BaseQuickAdapter mFilterCategoryAdapter;
    /**
     * 带条件的请求参数
     */
    private InvitationParam mFilterParams = new InvitationParam();

    /**
     * 管理排序控件
     */
    private List<TextView> mSortView = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation, R.string.label_invitation_title, R.drawable.ic_back, R.drawable.ic_more,false);

        initData();

        initView();

        addEvent();

    }

    private void initData() {
        mInvitations = new ArrayList<>();
        mInvitationPresenter = new InvitationPresenter(this);
        loadData();
        mInvitationPresenter.getFilterStatusList();
        mInvitationPresenter.getFilterCategoryList();
    }

    private void loadData() {
        mSmartRefreshLayout.setNoMoreData(false);
        mInvitationPresenter.loadData(mFilterParams);
    }

    private void initView() {
        initRecyclerView();
        initNavigationView();
        initSortView();
    }

    private void initSortView() {
        mSortView.add(mTvInvitationComposite);
        mSortView.add(mTvInvitationTime);
    }

    private void initNavigationView() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        mDrawerLayout.setStatusBarBackgroundColor(Color.WHITE);
        mTvFilter.setOnClickListener(v -> {
            openMenu();
            selectedFilterTitle();
        });
        initFilterDateView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mInvitationListAdapter = new BaseQuickAdapter<Invitation, BaseViewHolder>(R.layout.item_invitation_list, mInvitations) {
            @Override
            protected void convert(BaseViewHolder holder, Invitation bean) {
                setViewBg(holder, bean.getItem());
                holder.setText(R.id.tv_invitation_list_title, bean.getTitle());
                holder.setText(R.id.tv_invitation_list_company_name, String.format(getString(R.string.label_invitation_company_name), textFormat(bean.getInvitationCompanyName())));
                holder.setText(R.id.tv_invitation_list_address, String.format(getString(R.string.label_invitation_address), textFormat(bean.getAdress())));
                holder.setText(R.id.tv_invitation_list_end_time, String.format(getString(R.string.label_invitation_end_date), textFormat(Utils.formatDate(bean.getEndDate(), null))));
            }

            private void setViewBg(BaseViewHolder holder, Invitation.Item item) {
                if (item != null) {
                    holder.setBackgroundRes(R.id.v_invitation_list_item_color, item.getViewBgResId());
                    holder.setImageResource(R.id.iv_invitation_status, item.getImageViewResId());
                }
            }
        };
        mRecyclerView.setAdapter(mInvitationListAdapter);
    }

    public void closeMenu() {
        mDrawerLayout.closeDrawer(GravityCompat.END);
    }

    public void openMenu() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    private void addEvent() {
        mInvitationListAdapter.bindToRecyclerView(mRecyclerView);

        mInvitationListAdapter.setOnItemClickListener((adapter, view, position) -> {
            Invitation invitation = (Invitation) adapter.getData().get(position);
            mInvitationPresenter.gotoDetails(String.valueOf(invitation.getId()));
        });

        mSmartRefreshLayout.setOnRefreshListener(refreshlayout -> refresh());

        mSmartRefreshLayout.setOnLoadMoreListener(refreshlayout -> loadMore());
    }

    @OnClick({R.id.tv_invitation_composite, R.id.tv_invitation_date})
    public void onClickSort(View view) {
        titleSort(view);
    }

    private void titleSort(View view) {
        setDataAndResetOther(view);
        loadData();
    }

    private void setDataAndResetOther(View view) {
        String sort = (String) view.getTag();
        Sort currentSortEntity = null;
        if(TextUtils.isEmpty(sort)) {
            currentSortEntity = new Sort();
        }else{
            currentSortEntity = new Sort(sort);
        }
        String currentSort = currentSortEntity.getCurrentSort();
        String nextSort = currentSortEntity.getNextSort();
        for (TextView textView : mSortView) {
            if (textView == view) {
                textView.setTag(nextSort);
                textView.setSelected(true);
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_sort_default, 0);
                textView.setTag(null);
                textView.setSelected(false);
            }
        }
        mFilterParams.setIntegratedSort(null);
        mFilterParams.setTimeSort(null);
        if (view == mTvInvitationComposite) {
            mFilterParams.setIntegratedSort(currentSort);
        } else if (view == mTvInvitationTime) {
            mFilterParams.setTimeSort(currentSort);
        }
        currentSortEntity.setSortIcon(view);
    }

    /**
     * 重新刷新
     */
    private void refresh() {
        mInvitationPresenter.refreshData();
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        mInvitationPresenter.loadMore();
    }

    /**
     * 处理加载后的视图刷新
     */
    private void handleRefreshView() {
        if (mInvitationPresenter.isRefresh()) {
            mSmartRefreshLayout.finishRefresh();
            mSmartRefreshLayout.setNoMoreData(false);
        } else {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

    private String textFormat(String text) {
        return TextUtils.isEmpty(text) ? getString(R.string.label_invitation_no_date) : text;
    }

    @Override
    public void updateView(List<Invitation> invitations) {
        handleRefreshView();
//        if (invitations == null || invitations.isEmpty()) {
//            return;
//        }
        this.mInvitations = invitations;
        mInvitationListAdapter.setNewData(invitations);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void notifyNoData() {
        mSmartRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
    }

    @Override
    public void updateFilterStatusView(List<InvitationStatus> invitationStatusList) {
        if (invitationStatusList != null && !invitationStatusList.isEmpty()) {
            mInvitationStatusList = invitationStatusList;
            initFilterStatusListView();
        }
    }

    private void initFilterStatusListView() {
        TextView tvTitle = (TextView) mInvitationStatus.findViewById(R.id.tv_common_filter_title);
        tvTitle.setText(R.string.label_invitation_filter_status);
        RecyclerView recyclerView = (RecyclerView) mInvitationStatus.findViewById(R.id.rcv_common_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new MarginDecoration(Utils.dip2px(5)));
        mFilterStatusAdapter = new BaseQuickAdapter<InvitationStatus, BaseViewHolder>(R.layout.common_filter_item_layout, mInvitationStatusList) {
            @Override
            protected void convert(BaseViewHolder holder, InvitationStatus invitationStatus) {
                holder.setOnCheckedChangeListener(R.id.cb_common_item, (view, isChecked) -> {
                    String item = String.valueOf(invitationStatus.getId());
                    if (isChecked) {
                        mFilterStatusParams.add(item);
                    } else {
                        if (mFilterStatusParams.contains(item)) {
                            mFilterStatusParams.remove(item);
                        }
                    }
                });
                holder.setChecked(R.id.cb_common_item, invitationStatus.isChecked());
                holder.setText(R.id.cb_common_item, invitationStatus.getName());
            }
        };
        recyclerView.setAdapter(mFilterStatusAdapter);
    }

    @Override
    public void updateFilterCategoryView(List<InvitationCategory> invitationCategoryList) {
        if (invitationCategoryList != null && !invitationCategoryList.isEmpty()) {
            this.mInvitationCategoryList = invitationCategoryList;
//            //默认第一个选中
//            invitationCategoryList.get(0).setChecked(true);
            initFilterCategoryListView();
        }
    }

    private void initFilterCategoryListView() {
        TextView tvTitle = (TextView) mInvitationCategroy.findViewById(R.id.tv_common_filter_title);
        tvTitle.setText(R.string.label_invitation_filter_category);
        RecyclerView recyclerView = (RecyclerView) mInvitationCategroy.findViewById(R.id.rcv_common_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new MarginDecoration(Utils.dip2px(5)));
        mFilterCategoryAdapter = new BaseQuickAdapter<InvitationCategory, BaseViewHolder>(R.layout.common_filter_item_layout, mInvitationCategoryList) {

            @Override
            protected void convert(BaseViewHolder holder, InvitationCategory invitationCategory) {
                holder.setOnCheckedChangeListener(R.id.cb_common_item, (view, isChecked) -> {
                    String item = String.valueOf(invitationCategory.getId());
                    if (isChecked) {
                        mFilterCategoryParams.clear();
                        mFilterCategoryParams.add(item);
                    }
                });
                holder.setOnClickListener(R.id.cb_common_item, view -> {
                    //全部设为未选中
                    for (InvitationCategory bean : mInvitationCategoryList) {
                        bean.setChecked(false);
                    }
                    //点击的设为选中
                    invitationCategory.setChecked(true);
                    notifyDataSetChanged();
                });
                holder.setChecked(R.id.cb_common_item, invitationCategory.isChecked());
                holder.setText(R.id.cb_common_item, invitationCategory.getName());
            }
        };
        recyclerView.setAdapter(mFilterCategoryAdapter);
    }

    @OnClick({R.id.btn_common_ok, R.id.btn_common_reset})
    public void onClickFilter(View view) {
        switch (view.getId()) {
            case R.id.btn_common_ok:
                filterRequest();
                break;
            case R.id.btn_common_reset:
                resetFilter();
                break;
            default:
        }
    }

    private void filterRequest() {
        if (mFilterStatusParams != null && !mFilterStatusParams.isEmpty()) {
            mFilterParams.setStatus(setupSelectStr(mFilterStatusParams));
        }
        if (mFilterCategoryParams != null && !mFilterCategoryParams.isEmpty()) {
            mFilterParams.setInvitationCategoryId(setupSelectStr(mFilterCategoryParams));
        }
        loadData();
        closeMenu();
    }

    private String setupSelectStr(List<String> lists) {
        StringBuilder builder = new StringBuilder();
        if (lists != null) {
            for (int i = 0; i < lists.size(); i++) {
                if (lists.size() == 1 || i == lists.size() - 1) {
                    builder.append(lists.get(i));
                } else {
                    builder.append(lists.get(i)).append(",");
                }
            }
            return new String(builder);
        } else {
            return "";
        }
    }

    private void selectedFilterTitle() {
        mTvFilter.setSelected(true);
        mTvFilter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invitation_filter_selected, 0);
    }

    private void resetFilter() {
        resetFilterStatusView();
        resetFilterCategoryView();
        resetFilterDateView();
        resetFilterTitle();
        loadData();
    }

    private void resetFilterTitle() {
        mTvFilter.setSelected(false);
        mTvFilter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invitation_filter, 0);
    }

    private void resetFilterCategoryView() {
        mFilterCategoryParams.clear();
        mFilterParams.setInvitationCategoryId(null);
        //全部设为未选中
        for (InvitationCategory bean : mInvitationCategoryList) {
            bean.setChecked(false);
        }
//        mInvitationCategoryList.get(0).setChecked(true);
        mFilterCategoryAdapter.notifyDataSetChanged();
    }

    private void resetFilterStatusView() {
        mFilterStatusParams.clear();
        mFilterParams.setStatus(null);
        //全部设为未选中
        for (InvitationStatus bean : mInvitationStatusList) {
            bean.setChecked(false);
        }
        mFilterStatusAdapter.notifyDataSetChanged();
    }

    private void setDateFilter(TextView textView) {
        int initYear = 0, initMonth = 0, initDay = 0;
        String dateString = textView.getText().toString();
        if (TextUtils.isEmpty(dateString)) {
            Calendar cal = Calendar.getInstance();
            initYear = cal.get(Calendar.YEAR);
            initMonth = cal.get(Calendar.MONTH);
            initDay = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            String[] date = dateString.split("-");
            initYear = Integer.valueOf(date[0]);
            initMonth = Integer.valueOf(date[1]) - 1;
            initDay = Integer.valueOf(date[2]);
        }
        final DatePickerDialog mDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, null,
                initYear, initMonth, initDay);
        //手动设置按钮
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.label_invitation_filter_ok), (dialog, which) -> {
            //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
            DatePicker datePicker = mDialog.getDatePicker();
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            StringBuilder sb = new StringBuilder();
            sb.append(year).append("-").append(month + 1).append("-").append(day);
            String date = sb.toString();
            if (textView == mInvitationStartDate) {
                mFilterParams.setStartDate(date);
                textView.setText(date);
                textView.setSelected(true);
            } else if (textView == mInvitationEndDate) {
                if (TextUtils.isEmpty(mFilterParams.getStartDate()) || date.compareTo(mFilterParams.getStartDate()) > 0) {
                    mFilterParams.setEndDate(date);
                    textView.setText(date);
                    textView.setSelected(true);
                } else {
                    Utils.showToast(InvitationActivity.this, getString(R.string.label_invitation_filter_date_error));
                }
            }
        });

        //取消按钮，如果不需要直接不设置即可
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.label_invitation_filter_cancel), (dialog, which) -> {

        });
        mDialog.show();
    }

    private void initFilterDateView() {
        TextView tvTitle = (TextView) mInvitationDate.findViewById(R.id.tv_common_filter_title);
        tvTitle.setText(R.string.label_invitation_filter_date);
        mInvitationStartDate.setOnClickListener(view -> setDateFilter(mInvitationStartDate));
        mInvitationEndDate.setOnClickListener(view -> setDateFilter(mInvitationEndDate));
    }

    private void resetFilterDateView() {
        mFilterParams.setStartDate(null);
        mFilterParams.setEndDate(null);
        mInvitationStartDate.setText(null);
        mInvitationEndDate.setText(null);
        mInvitationStartDate.setSelected(false);
        mInvitationEndDate.setSelected(false);
    }
}
