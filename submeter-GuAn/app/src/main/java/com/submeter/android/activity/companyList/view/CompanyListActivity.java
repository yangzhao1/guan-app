package com.submeter.android.activity.companyList.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.submeter.android.R;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.companyInfo.view.CompanyInfoActivity;
import com.submeter.android.activity.companyList.presenter.CompanyListPersenter;
import com.submeter.android.adapter.CompanyListAdapter;
import com.submeter.android.adapter.SpaceItemDecoration;
import com.submeter.android.entity.AreaBean;
import com.submeter.android.entity.Company;
import com.submeter.android.entity.CompanyDataBean;
import com.submeter.android.entity.HomeMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangzhao on 2019/4/13.
 */

public class CompanyListActivity extends BaseActivity implements ICompanyView {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.nodata)
    TextView nodata;
    @BindView(R.id.goSearch)
    TextView goSearch;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.search_lin)
    LinearLayout search_lin;
    @BindView(R.id.statusbar_view)
    View statusbarView;
    @BindView(R.id.product)
    TextView product;
    @BindView(R.id.productLin)
    LinearLayout productLin;
    @BindView(R.id.stopProduct)
    TextView stopProduct;
    @BindView(R.id.stopProductLin)
    LinearLayout stopProductLin;
    @BindView(R.id.notOpen)
    TextView notOpen;
    @BindView(R.id.notOpenLin)
    LinearLayout notOpenLin;
    @BindView(R.id.failTo)
    TextView failTo;
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.failToLin)
    LinearLayout failToLin;
    @BindView(R.id.srl_header)
    ClassicsHeader srlHeader;
    @BindView(R.id.rel)
    RelativeLayout rel;
    @BindView(R.id.srl_footer)
    ClassicsFooter srlFooter;
    @BindView(R.id.productText)
    TextView productText;
    @BindView(R.id.stopProductText)
    TextView stopProductText;
    @BindView(R.id.notOpenText)
    TextView notOpenText;
    @BindView(R.id.failToText)
    TextView failToText;
    @BindView(R.id.right_image)
    ImageView right_image;

    private BaseActivity parentActivity;
    private CompanyListPersenter presenter;

    private CompanyListAdapter adapter;
    private boolean isFirst = true;
    private String org = "";
    private int state = 1;
    private OptionsPickerView mHobbyPickerView;
    private List<AreaBean.AreaBeanN> listArea = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companylist, R.string.company_list,
                R.drawable.ic_back, R.mipmap.more, false);
        setStatusBar(true);
        ButterKnife.bind(this);
        parentActivity = this;

        initView();
    }

    private void initView() {
        //userType==2为管理员账户,可以看见下级乡镇数据
        if (SubmeterApp.getInstance().getUserType().equals("2")){
            right_image.setVisibility(View.VISIBLE);
        }else {
            right_image.setVisibility(View.INVISIBLE);
        }
        presenter = new CompanyListPersenter(this);
        presenter.loadData("", state, org);

        srlRefresh.setOnRefreshListener(refreshLayout -> presenter.onRefresh());
        srlRefresh.setOnLoadMoreListener(refreshLayout -> presenter.onLoadMore());
    }

    @Override
    public BaseActivity getBaseActivity() {
        return parentActivity;
    }

    @OnClick({R.id.right_image, R.id.goSearch, R.id.productLin,
            R.id.stopProductLin, R.id.notOpenLin, R.id.failToLin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.left_image:
//                finish();
//                break;
            case R.id.right_image://搜索
                if (listArea.size() != 0) {
                    updateAreaList(listArea);
                } else {
                    presenter.getAreaList();
                }
                break;
            case R.id.goSearch:
//                if (!content.getText().toString().isEmpty()){
                presenter.onSearch(content.getText().toString(), "", state, org);
//                }else {
//                    showToast("请输入企业关键字");
//                }

                break;
            case R.id.productLin:
                if (state!= 1) {
                    state = 1;
                    presenter.onSearch(content.getText().toString(), "", state, org);
                    setColorTexts(productText,product,state);
                }else {
                    state = 0;
                    presenter.onSearch(content.getText().toString(), "", state, org);
                    setColorTexts(productText,product,state);
                }
                break;
            case R.id.stopProductLin:
                if (state != 2) {
                    state = 2;
                    presenter.onSearch(content.getText().toString(), "", state, org);
                    setColorTexts(stopProduct,stopProductText,state);
                }else {
                    state = 0;
                    presenter.onSearch(content.getText().toString(), "", state, org);
                    setColorTexts(stopProduct,stopProductText,state);
                }
                break;
            case R.id.notOpenLin:
                if (state != 3) {
                    state = 3;
                    presenter.onSearch(content.getText().toString(), "", state, org);
                    setColorTexts(notOpenText,notOpen,state);
                }else {
                    state = 0;
                    presenter.onSearch(content.getText().toString(), "", state, org);
                    setColorTexts(notOpenText,notOpen,state);
                }
                break;
            case R.id.failToLin:
                if (state != 4) {
                    state = 4;
                    presenter.onSearch(content.getText().toString(), "", state, org);
                    setColorTexts(failTo,failToText,state);
                }else {
                    state = 0;
                    presenter.onSearch(content.getText().toString(), "", state, org);
                    setColorTexts(failTo,failToText,state);
                }
                break;
        }
    }

    private void setColorTexts(TextView text1, TextView text2,int state) {
        product.setTextColor(getResources().getColor(R.color.black_color));
        stopProduct.setTextColor(getResources().getColor(R.color.black_color));
        notOpen.setTextColor(getResources().getColor(R.color.black_color));
        failTo.setTextColor(getResources().getColor(R.color.black_color));
        productText.setTextColor(getResources().getColor(R.color.black_color));
        stopProductText.setTextColor(getResources().getColor(R.color.black_color));
        notOpenText.setTextColor(getResources().getColor(R.color.black_color));
        failToText.setTextColor(getResources().getColor(R.color.black_color));
        if (state!=0){
            text1.setTextColor(getResources().getColor(R.color.title_color));
            text2.setTextColor(getResources().getColor(R.color.title_color));
        }
    }

    //初始化爱好选择器
    private void initHobbyOptionPicker(List<String> mHobbyNameList) {
        mHobbyPickerView = new OptionsPickerBuilder(parentActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mHobbyNameList.get(options1);
                text_title.setText(tx);
                org = listArea.get(options1).getZonCode();
                presenter.onSearch(content.getText().toString(), "", state, org);
            }
        })
//                .setDecorView((RelativeLayout)findViewById(R.id.activity_rootview))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                .setTitleText("选择区域")//标题文字
                .setTitleSize(20)//标题文字大小
                .setTitleColor(getResources().getColor(R.color.black_color))//标题文字颜色
                .setCancelText("取消")//取消按钮文字
                .setCancelColor(getResources().getColor(R.color.title_color))//取消按钮文字颜色
                .setSubmitText("确定")//确认按钮文字
                .setSubmitColor(getResources().getColor(R.color.title_color))//确定按钮文字颜色
                .setContentTextSize(20)//滚轮文字大小
                .setTextColorCenter(getResources().getColor(R.color.black_color))//设置选中文本的颜色值
                .setLineSpacingMultiplier(1.8f)//行间距
                .setDividerColor(getResources().getColor(R.color.gray_view_page_background))//设置分割线的颜色
                .setSelectOptions(0)//设置选择的值
                .build();

        mHobbyPickerView.setPicker(mHobbyNameList);//添加数据
    }

    @Override
    public void updateView(CompanyDataBean dataBean) {
        handleRefreshView();
        if (dataBean != null) {
            List<Company> list = dataBean.getList();
            product.setText(dataBean.getProductNum());
            stopProduct.setText(dataBean.getStopProductNum());
            notOpen.setText(dataBean.getNotStartNum());
            failTo.setText(dataBean.getNotFoundNum());
            if (list != null && list.size() != 0) {
                isFirst = false;
                nodata.setVisibility(View.GONE);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                } else {
                    LinearLayoutManager manager = new LinearLayoutManager(getBaseActivity());
                    recycler.setLayoutManager(manager);
                    recycler.addItemDecoration(new SpaceItemDecoration(5));
                    adapter = new CompanyListAdapter(parentActivity, list);
                    recycler.setAdapter(adapter);
                    adapter.setOnItemClickListener((view, pos) -> {
                        Intent intent = new Intent(getBaseActivity(), CompanyInfoActivity.class);
                        intent.putExtra("company", list.get(pos));
                        startActivity(intent);
                    });
                    adapter.setOnPhoneClickListener((view, pos) -> {
                        String phone = list.get(pos).getPhone();
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                if (presenter.isRefresh() || isFirst || presenter.isSearch()) {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    if (isFirst) {
                        isFirst = false;
                    }
                    nodata.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void updateAreaList(List<AreaBean.AreaBeanN> list) {
        if (listArea.size() == 0) {
            listArea.addAll(list);
        }
        List<String> stringList = new ArrayList<>();
        for (AreaBean.AreaBeanN bean : list) {
            stringList.add(bean.getName());
        }
        initHobbyOptionPicker(stringList);
        mHobbyPickerView.show();
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
}
