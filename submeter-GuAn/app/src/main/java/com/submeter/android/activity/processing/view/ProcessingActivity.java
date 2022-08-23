package com.submeter.android.activity.processing.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.processing.presenter.IProPresenter;
import com.submeter.android.activity.processing.presenter.ProPresenter;
import com.submeter.android.entity.ViolatorData;
import com.submeter.android.eventbus.MessageEvent;
import com.submeter.android.tools.ShareUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangzhao on 2019/3/28.
 */

public class ProcessingActivity extends BaseActivity implements IProView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.info_lin)
    LinearLayout infoLin;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.navigation)
    TextView navigation;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.week)
    TextView week;
    @BindView(R.id.month)
    TextView month;
    @BindView(R.id.cause)
    EditText cause;
    @BindView(R.id.measure)
    EditText measure;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.score)
    EditText score;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.unOut)
    RadioButton unOut;
    @BindView(R.id.outed)
    RadioButton outed;
    @BindView(R.id.group)
    RadioGroup group;

    private IProPresenter presenter;
    private String id = "0";
    private String endID="";
    private String status="2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing, R.string.company_getOut_processing, R.drawable.ic_back, "", false);
        ButterKnife.bind(this);

        unOut.setChecked(true);
        group.setOnCheckedChangeListener((group1, checkedId) -> {
            if (checkedId==unOut.getId()){
                unOut.setChecked(true);
                status="2";
            }else if (checkedId==outed.getId()){
                outed.setChecked(true);
                status="1";
            }
        });
        ViolatorData data = getIntent().getParcelableExtra("violator");
        id = getIntent().getStringExtra("id");
        endID = data.getEnId();
        name.setText(getString(R.string.linkman) + data.getPerson());
        phone.setText(getString(R.string.phone) + data.getPhone());
        title.setText(data.getEnname());
        address.setText(data.getAddress());
        week.setText(getString(R.string.week_vio) + data.getWeekNum() + "次");
        month.setText(getString(R.string.month_vio) + data.getMonthNum() + "次");
        presenter = new ProPresenter(this);
        navigation.setOnClickListener(v -> {
            ShareUtil.showSelectMapPOP(this,lin,Double.parseDouble(data.getAreaX()),
                    Double.parseDouble(data.getAreaY()),data.getAddress());
        });
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        if (checked()){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("operMeasures",measure.getText().toString());
                jsonObject.put("enId",endID);
                jsonObject.put("vioId",id);
                jsonObject.put("operReason",cause.getText().toString());
                jsonObject.put("operResult",content.getText().toString());
                jsonObject.put("score",score.getText().toString());
                jsonObject.put("status",status);
                presenter.submit(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checked() {
        if (cause.getText().toString().isEmpty()){
            showToast("请输入处理原因");
            return false;
        }

        if (measure.getText().toString().isEmpty()){
            showToast("请输入处理措施");
            return false;
        }

        if (score.getText().toString().isEmpty()){
            showToast("请输入扣除分数");
            return false;
        }
        if (content.getText().toString().isEmpty()){
            showToast("请输入处理详情");
            return false;
        }
        return true;
    }

    @Override
    public void updateView() {
        showToast("处理完成");
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_MAIN_OUTINFO));
        finish();
    }
}
