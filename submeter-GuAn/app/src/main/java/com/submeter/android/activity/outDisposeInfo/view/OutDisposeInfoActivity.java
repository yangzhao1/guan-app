package com.submeter.android.activity.outDisposeInfo.view;

import android.os.Bundle;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.outDisposeInfo.presenter.IOutDInfoPresenter;
import com.submeter.android.activity.outDisposeInfo.presenter.OutDisposeInfoPresenter;
import com.submeter.android.entity.OutDisposeData;
import com.submeter.android.entity.ViolatorData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangzhao on 2019/6/15.
 */

public class OutDisposeInfoActivity extends BaseActivity implements IODInfoView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.cause)
    TextView cause;
    @BindView(R.id.measure)
    TextView measure;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.content)
    TextView content;
    private IOutDInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdispose, R.string.dispose, R.drawable.ic_back, "", false);
        ButterKnife.bind(this);
        String id = getIntent().getStringExtra("id");
        String s = getIntent().getStringExtra("state");
        ViolatorData data = getIntent().getParcelableExtra("violator");
        name.setText(getString(R.string.linkman) + data.getPerson());
        phone.setText(getString(R.string.phone) + data.getPhone());
        title.setText(data.getEnname());
        address.setText(data.getAddress());
        state.setText("违规处理状态: " + s);
        presenter = new OutDisposeInfoPresenter(this);
        presenter.loadData(id);
    }

    @Override
    public void updateView(OutDisposeData data) {
        cause.setText("处理原因: " +data.getOperReason() );
        measure.setText("处理措施: "+data.getOperMeasures());
        score.setText("扣除分数: "+data.getScore());
        content.setText("处理详情: "+data.getOperResult());
    }
}
