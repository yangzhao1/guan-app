package com.submeter.android.activity.handlingProblem.fragment.processed.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.companyGetOutInfo.view.CompanyGetOutInfoActivity;
import com.submeter.android.activity.handlingProblem.fragment.untreated.presenter.IUnPresenter;
import com.submeter.android.activity.handlingProblem.fragment.untreated.presenter.UnPresenter;
import com.submeter.android.activity.handlingProblem.fragment.untreated.view.IUnView;
import com.submeter.android.adapter.ProcessedAdapter;
import com.submeter.android.adapter.SpaceItemDecoration;
import com.submeter.android.entity.GetOutUn;
import com.submeter.android.interfacce.IChildFragment;
import com.submeter.android.tools.ShareUtil;
import com.submeter.android.tools.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yangzhao on 2019/3/28.
 */

public class ProcessedFragment extends Fragment implements IChildFragment,IUnView{


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.rel)
    RelativeLayout rel;
    @BindView(R.id.nodata)
    TextView nodata;
    @BindView(R.id.goSearch)
    TextView goSearch;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.time_lin)
    LinearLayout timeLin;
    @BindView(R.id.startTime)
    TextView startTime;
    @BindView(R.id.endTime)
    TextView endTime;

    private View contentView;

    private BaseActivity parentActivity;

    private Unbinder unbinder;

    private ProcessedAdapter adapter = null;

    private boolean isFirst = true;
    private boolean isHidden = true;

    private IUnPresenter presenter;
    private int type=0;
    private int isHandle=4;
    private String startTimeStr="";
    private String endTimeStr="";
    private TimePickerView pvTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = Utils.inflateView(parentActivity, inflater, R.layout.fragment_processed,
                    container, false);
        } else {
            ViewGroup p = (ViewGroup) contentView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }

        unbinder = ButterKnife.bind(this, contentView);
        initData();
        initEvent();
        return contentView;
    }

    private void initData() {
        initTimePicker();
        presenter = new UnPresenter(this);
        type = getArguments().getInt("type");
        isHandle = getArguments().getInt("handle");

        if (type==1){//今日违规
            timeLin.setVisibility(View.GONE);
            presenter.loadData("0",content.getText().toString(),isHandle,1);
        }else {
            presenter.loadData("1",content.getText().toString(),isHandle,0);
        }
        recycler.setBackgroundResource(R.color.gray_d8d8d8_color);
    }
    private void initEvent() {
        goSearch.setOnClickListener(v -> {
//            if (!content.getText().toString().isEmpty()){
            if (type==1){//今日违规
                presenter.onSearch("0",content.getText().toString(),isHandle,1,startTimeStr,endTimeStr);
            }else {
                if (!startTime.getText().toString().contains("开始")){
                    startTimeStr=startTime.getText().toString();
                }
                if (!endTime.getText().toString().contains("结束")){
                    endTimeStr=endTime.getText().toString();
                }
                if (startTimeStr.equals("")&&!endTimeStr.equals("")){
                    Toast.makeText(parentActivity,"请选择开始时间",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!startTimeStr.equals("")&&endTimeStr.equals("")){
                    Toast.makeText(parentActivity,"请选择结束时间",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!startTimeStr.equals("")&&!endTimeStr.equals("")){
                    boolean flag = Utils.endTimeIsMoreBig(startTimeStr,endTimeStr);
                    if (!flag){
                        Toast.makeText(parentActivity,"结束时间必须大于开始时间",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                presenter.onSearch("1",content.getText().toString(),isHandle,0,
                        startTimeStr,endTimeStr);
            }//            }else {
//                parentActivity.showToast("请输入企业关键字");
//            }
        });
        startTime.setOnClickListener(v -> {
            pvTime.show(startTime);
        });
        endTime.setOnClickListener(v -> {
            pvTime.show(endTime);
        });
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> presenter.onRefresh());
        mSmartRefreshLayout.setOnLoadMoreListener(refreshLayout -> presenter.onLoadMore());
    }

    /**
     * 处理加载后的视图刷新
     */
    private void handleRefreshView() {
        if (presenter.isRefresh()) {
            mSmartRefreshLayout.finishRefresh();
            mSmartRefreshLayout.setNoMoreData(false);
        } else {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public BaseActivity getBaseActivity() {
        return parentActivity;
    }

    @Override
    public void updateView(List list) {
        handleRefreshView();
        if (list!=null&&list.size()!=0){
            isFirst=false;
            nodata.setVisibility(View.GONE);
            if (adapter!=null){
                adapter.notifyDataSetChanged();
            }else {
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recycler.setLayoutManager(manager);
                recycler.addItemDecoration(new SpaceItemDecoration(5));
                adapter = new ProcessedAdapter(parentActivity, list ,isHandle);
                recycler.setAdapter(adapter);

                adapter.setOnItemClickListener((view, pos) -> {
                    Intent intent = new Intent(parentActivity, CompanyGetOutInfoActivity.class);
                    GetOutUn g = (GetOutUn) list.get(pos);
                    intent.putExtra("id",g.getEnId());
                    startActivity(intent);
                });

                adapter.setNavigationOnClickListener((v, pos) -> {
                    GetOutUn g = (GetOutUn) list.get(pos);
                    ShareUtil.showSelectMapPOP(parentActivity,lin,Double.parseDouble(g.getAreaY()),
                            Double.parseDouble(g.getAreaX()),g.getEnname());
                });
            }
        }else {
            if (presenter.isRefresh()||isFirst||presenter.isSearch()){
                if (adapter!=null){
                    adapter.notifyDataSetChanged();
                }
                if (isFirst){
                    isFirst = false;
                }
                nodata.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void pageShow() {
        if (!isHidden) {
            return;
        }

        isHidden = false;
        if (isFirst) {
            initData();
        }
        isFirst = false;
    }

    @Override
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

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(getBaseActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(getBaseActivity(), getTime(date), Toast.LENGTH_SHORT).show();
                Log.i("pvTime", "onTimeSelect");
                String currDate = getTime(date);
                if (v.getId()==R.id.startTime){
                    startTime.setText(currDate);
                }else {
                    endTime.setText(currDate);
                }
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
