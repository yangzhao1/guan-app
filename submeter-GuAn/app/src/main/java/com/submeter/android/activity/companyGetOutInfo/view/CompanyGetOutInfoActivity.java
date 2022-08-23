package com.submeter.android.activity.companyGetOutInfo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.companyGetOutInfo.presenter.IVioPresenter;
import com.submeter.android.activity.companyGetOutInfo.presenter.VioPresenter;
import com.submeter.android.activity.outDisposeInfo.view.OutDisposeInfoActivity;
import com.submeter.android.activity.processing.view.ProcessingActivity;
import com.submeter.android.adapter.ViolatorInfoAdapter;
import com.submeter.android.entity.Violator;
import com.submeter.android.entity.ViolatorData;
import com.submeter.android.eventbus.MessageEvent;
import com.submeter.android.tools.ShareUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yangzhao on 2019/3/28.
 */

public class CompanyGetOutInfoActivity extends BaseActivity implements IVioView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.info_lin)
    LinearLayout infoLin;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.navigation)
    TextView navigation;
    @BindView(R.id.week)
    TextView week;
    @BindView(R.id.month)
    TextView month;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.rel)
    RelativeLayout rel;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.lin)
    LinearLayout lin;

    private String titleStr = "";
    private String nameStr = "";
    private String phoneStr = "";

    private ViolatorInfoAdapter adapter = null;
    private IVioPresenter presenter;
    private String id = "";
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_getoutinfo, R.string.company_getOut_info, R.drawable.ic_back, "", false);
        unbinder = ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        title.setText(titleStr);
        name.setText(nameStr);
        phone.setText(phoneStr);

        presenter = new VioPresenter(this);
        presenter.loadData(id);
    }

//    @OnClick(R.id.submit)
//    public void onViewClicked() {
//        Intent intent = new Intent(getApplicationContext(), ProcessingActivity.class);
//        intent.putExtra("title", titleStr);
//        intent.putExtra("name", nameStr);
//        intent.putExtra("phone", phoneStr);
//        startActivity(intent);
//    }

    @Override
    public void updateView(ViolatorData data) {
        if (data != null) {
            name.setText(getString(R.string.linkman)+data.getPerson());
            phone.setText(getString(R.string.phone) + data.getPhone());
            title.setText(data.getEnname());
            address.setText(data.getAddress());
            week.setText(getString(R.string.week_vio)+data.getWeekNum()+"次");
            month.setText(getString(R.string.month_vio)+data.getMonthNum()+"次");
            List<Violator> list = data.getList();

            navigation.setOnClickListener(v -> {
                ShareUtil.showSelectMapPOP(this,lin,Double.parseDouble(data.getAreaY()),
                        Double.parseDouble(data.getAreaX()),data.getAddress());
            });

            if (list!=null&&!list.isEmpty()){
                if (adapter!=null){
                    adapter.updateItemsData(list);
                }else {
                    LinearLayoutManager manager = new LinearLayoutManager(this){
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    recycler.setLayoutManager(manager);
                    adapter = new ViolatorInfoAdapter(this, list);
                    recycler.setAdapter(adapter);
                }
                adapter.setOnItemClickListener((view, pos) -> {
                    Intent intent = new Intent(getApplicationContext(),CompanyInfoFacilityActivity.class);
                    intent.putExtra("date", list.get(pos).getVioTime());
                    intent.putExtra("id",id);
                    startActivity(intent);
                });

                adapter.setTextClickListener(((view, pos) -> {
                    String y= list.get(pos).getStatus();
                    if (y.equals("0")){  //待处理
                        Intent intent = new Intent(getApplicationContext(),ProcessingActivity.class);
                        intent.putExtra("violator", data);
                        intent.putExtra("id", list.get(pos).getId());
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getApplicationContext(),OutDisposeInfoActivity.class);
                        intent.putExtra("violator", data);
                        intent.putExtra("id", list.get(pos).getId());
                        String s ;
                        if (y.equals("1")){
                            s="违规";
                        }else if (y.equals("2")){
                            s="未违规";
                        }else {
                            s="已处理";
                        }
                        intent.putExtra("state", s);
                        startActivity(intent);
                    }
                }));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (unbinder!=null){
        }else {
            unbinder = ButterKnife.bind(this);
        }
        if(messageEvent.getCode() == MessageEvent.SHOW_MAIN_OUTINFO){
            presenter.loadData(id);
        }else {

        }
    }
}
