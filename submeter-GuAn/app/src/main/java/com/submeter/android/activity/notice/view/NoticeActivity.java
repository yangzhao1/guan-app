package com.submeter.android.activity.notice.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.notice.presenter.NoticePersenter;
import com.submeter.android.adapter.NoticeAdapter;
import com.submeter.android.adapter.SpaceItemDecoration;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.entity.Notice;
import com.submeter.android.view.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangzhao on 2019/4/13.
 */

public class NoticeActivity extends BaseActivity implements INotiveView,OnRefreshListener {

    @BindView(R.id.statusbar_view)
    View statusbarView;
    @BindView(R.id.left_image)
    ImageView leftImage;
    @BindView(R.id.left_text)
    TextView leftText;
    @BindView(R.id.left_title_btn)
    LinearLayout leftTitleBtn;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.title_search)
    TextView titleSearch;
    @BindView(R.id.right_image)
    ImageView rightImage;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.right_title_btn)
    LinearLayout rightTitleBtn;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.rel)
    RelativeLayout rel;
    @BindView(R.id.srl_header)
    ClassicsHeader srlHeader;
    @BindView(R.id.srl_footer)
    ClassicsFooter srlFooter;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;

    private BaseActivity parentActivity;
    private NoticePersenter persenter;

    private int pageNum = 1;
    private int pageSize = NetworkResConstant.LIST_PAGE_SIZE;
    private NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice, R.string.notice,
                R.drawable.ic_back, R.mipmap.search, false);
        ButterKnife.bind(this);
        parentActivity = this;

        initView();
    }

    private void initView() {
        persenter = new NoticePersenter(this);
        persenter.loadData(pageNum,pageSize,"");
        srlRefresh.setOnRefreshListener(refreshLayout -> onRefresh());

        srlRefresh.setOnLoadMoreListener(refreshLayout -> onLoadMore());
    }

    @Override
    public BaseActivity getBaseActivity() {
        return parentActivity;
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showToast(String toast) {

    }

    @Override
    public void updateView(List<Notice> noticeList) {
        srlRefresh.finishRefresh();
        srlRefresh.finishLoadMore();
        if (pageNum==1){
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recycler.setLayoutManager(manager);
            recycler.addItemDecoration(new SpaceItemDecoration(1));
            adapter = new NoticeAdapter(getBaseActivity(),noticeList);
            recycler.setAdapter(adapter);

            adapter.setOnItemClickListener((view, pos) -> {

            });
        }else {
            adapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.left_image, R.id.right_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_image:
                finish();
                break;
            case R.id.right_image://搜索
                break;
        }
    }

    @Override
    public void onRefresh() {
        pageNum =1;
        persenter.loadData(pageNum,pageSize,"");
    }

    @Override
    public void onLoadMore() {
        pageSize++;
        persenter.loadData(pageNum,pageSize,"");
    }

    @Override
    public boolean isRefresh() {
        return false;
    }
}
