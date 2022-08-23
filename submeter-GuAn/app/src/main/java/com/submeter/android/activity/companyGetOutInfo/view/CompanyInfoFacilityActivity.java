package com.submeter.android.activity.companyGetOutInfo.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.submeter.android.R;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.adapter.FacilityAdapter1;
import com.submeter.android.entity.FacilityListBean;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.ProblemAction;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangzhao on 2019/3/28.
 */

public class CompanyInfoFacilityActivity extends BaseActivity implements INetworkResponseListener {


    @BindView(R.id.statusbar_view)
    View statusbarView;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.nodata)
    TextView nodata;
    private String id = "";
    private String date = "";
    private NetworkResponseListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_main, R.string.company_facility_info, R.drawable.ic_back, "", false);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        date = getIntent().getStringExtra("date");
        if (date != null) {
            date = date.replace("-", "/");
            listener = new NetworkResponseListener(this);
            getBaseActivity().showLoadingView();
            ProblemAction.getFacilityList(id, date, SubmeterApp.getInstance().getUserToken(), listener);
        }
    }

    public void updateView(FacilityListBean bean) {
        if (bean.getEnterpriseDtos()==null||bean.getEnterpriseDtos().size()==0){
            nodata.setVisibility(View.VISIBLE);
        }else {
            LinearLayoutManager manager = new LinearLayoutManager(getBaseActivity());
            recycler.setLayoutManager(manager);
            FacilityAdapter1 adapter1 = new FacilityAdapter1(getBaseActivity(), bean.getEnterpriseDtos());
            recycler.setAdapter(adapter1);
        }
    }

    @Override
    public void onResponse(String result) {
        getBaseActivity().hideLoadingView();
        if (!listener.handleInnerError(result)) {
            FacilityListBean bean = new Gson().fromJson(result, FacilityListBean.class);
            updateView(bean);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        getBaseActivity().hideLoadingView();

    }
}
