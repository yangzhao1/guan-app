package com.submeter.android.activity.commodityList.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.commodityDetail.view.CommodityDetailActivity;
import com.submeter.android.activity.commodityList.params.CommodityListParam;
import com.submeter.android.activity.commodityList.presenter.CommodityListPresenter;
import com.submeter.android.activity.commodityList.presenter.ICommodityListPresenter;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.entity.Banner;
import com.submeter.android.entity.Commodity;
import com.submeter.android.entity.ProductFrontCategory;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.tools.DeviceUtils;
import com.submeter.android.tools.Utils;
import com.submeter.android.view.BannerView;
import com.submeter.android.view.CommonFilterLayout;
import com.submeter.android.view.MarginDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 商品列表
 *
 * @author thm
 * @date 2018/12/13
 */
public class CommodityListActivity extends BaseActivity implements ICommodityListView {

    @BindView(R.id.banner_view)
    BannerView bannerView;

    @BindView(R.id.srl_refresh)
    SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R.id.rcv_content)
    RecyclerView mRecyclerView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    LinearLayout mNavigationView;

    @BindView(R.id.ll_commodity_price)
    LinearLayout mLlCommodityListPrice;

    @BindView(R.id.ll_commodity_categroy)
    LinearLayout mLlCommodityListCategroy;

    @BindView(R.id.et_common_item_min_price)
    EditText mEtMinPrice;

    @BindView(R.id.et_common_item_max_price)
    EditText mEtMaxPrice;

    @BindView(R.id.commonFilterLayout)
    CommonFilterLayout mCommonFilterLayout;

    private ICommodityListPresenter mCommodityListPresenter;

    private BaseQuickAdapter<Commodity, BaseViewHolder> mCommodityListAdapter;

    /**
     * 商品列表列表
     */
    private List<Commodity> mCommodities;

    /**
     * 前台分类列表
     */
    private List<ProductFrontCategory> mProductFrontCategories;

    /**
     * 分类筛选条件
     */
    private List<String> mFilterCategoryParams = new ArrayList<>();
    /**
     * 前台分类视图
     */
    private BaseQuickAdapter<ProductFrontCategory, BaseViewHolder> mFilterCategoryAdapter;
    /**
     * 带条件的请求参数
     */
    private CommodityListParam mFilterParams = new CommodityListParam();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_list, -1, R.drawable.ic_back, R.drawable.ic_more,true);

        initData();

        initView();

        addEvent();

    }

    private void initData() {
        mCommodities = new ArrayList<>();
        mCommodityListPresenter = new CommodityListPresenter(this);
        loadData();
        mCommodityListPresenter.getFilterCategoryList();
        mCommodityListPresenter.getBannerList();
    }

    private void loadData() {
        //todo 默认的测试数据
        //mFilterParams.setProductFrontCategoryId("1273");

        Intent intent = getIntent();
        if(intent != null) {
            String keyWords = intent.getStringExtra(SystemConstant.COMMODITYLIST_KEYWORDS_KEY);
            String categoryId = intent.getStringExtra(SystemConstant.COMMODITYLIST_CATEGORYID_KEY);
            String brandId = intent.getStringExtra(SystemConstant.COMMODITYLIST_BRANDID_KEY);
            if(!TextUtils.isEmpty(keyWords)) {
                mFilterParams.setKey(keyWords);
                setTitleSearchText(keyWords);
            }
            if(!TextUtils.isEmpty(categoryId)) {
                mFilterParams.setProductFrontCategoryId(categoryId);
            }
            if(!TextUtils.isEmpty(brandId)) {
                mFilterParams.setBrandId(brandId);
            }
        }
        mCommodityListPresenter.loadData(mFilterParams);
        mSmartRefreshLayout.setNoMoreData(false);
    }

    private void initView() {
        initRecyclerView(false);
        initNavigationView();
    }

    private void initNavigationView() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        mDrawerLayout.setStatusBarBackgroundColor(Color.WHITE);
        initFilterView();
    }

    private void initRecyclerView(boolean isGridView) {
        int resId = -1;
        if (isGridView) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            resId = R.layout.item_commoditylist_grid_list;
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            resId = R.layout.item_commoditylist_vertical_list;
        }
        mCommodityListAdapter = new BaseQuickAdapter<Commodity, BaseViewHolder>(resId, mCommodities) {
            @Override
            protected void convert(BaseViewHolder holder, Commodity bean) {
                boolean isLogin = SubmeterApp.getInstance().isVisitor();
                String price = isLogin ? getString(R.string.label_commodity_list_unlogin_price) : String.format(getString(R.string.label_commodity_list_login_price), String.valueOf(bean.getPrice()));
                holder.setText(R.id.tv_commodity_list_price, price);
                holder.setText(R.id.tv_commodity_list_title, bean.getName());
                holder.setText(R.id.tv_commodity_list_type_store, Utils.getCommodityType(mContext, bean.getTypeStore()));
                holder.setText(R.id.tv_commodity_list_sales, String.format(getString(R.string.label_commodity_list_sales), String.valueOf(bean.getSales())));
                SimpleDraweeView simpleDraweeView = holder.getView(R.id.iv_commodity_list_icon);
                NetworkRequestTool.getInstance().loadImage(Uri.parse(bean.getImage()), simpleDraweeView, true);
            }
        };
        mRecyclerView.setAdapter(mCommodityListAdapter);
    }

    public void closeMenu() {
        mDrawerLayout.closeDrawer(GravityCompat.END);
    }

    public void openMenu() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    private void addEvent() {
        mCommodityListAdapter.bindToRecyclerView(mRecyclerView);

        mCommodityListAdapter.setOnItemClickListener((adapter, view, position) -> {
//            Invitation invitation = (Invitation) adapter.getData().get(position);
//            mCommodityListPresenter.gotoDetails(String.valueOf(invitation.getId()));
        });

        mSmartRefreshLayout.setOnRefreshListener(refreshlayout -> refresh());

        mSmartRefreshLayout.setOnLoadMoreListener(refreshlayout -> loadMore());

        mCommonFilterLayout.setFilterParams(mFilterParams);
        mCommonFilterLayout.setOnSortListener(() -> loadData());
        mCommonFilterLayout.setOnFilterClickListener(() -> openMenu());
        mCommonFilterLayout.setOnChangeLayoutListener(isGridView -> initRecyclerView(isGridView));
    }

    /**
     * 重新刷新
     */
    private void refresh() {
        mCommodityListPresenter.refreshData();
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        mCommodityListPresenter.loadMore();
    }

    /**
     * 处理加载后的视图刷新
     */
    private void handleRefreshView() {
        if (mCommodityListPresenter.isRefresh()) {
            mSmartRefreshLayout.finishRefresh();
            mSmartRefreshLayout.setNoMoreData(false);
        } else {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void updateView(List<Commodity> commodities) {
        handleRefreshView();
//        if (commodities == null || commodities.isEmpty()) {
//            return;
//        }
        this.mCommodities = commodities;
        mCommodityListAdapter.setNewData(mCommodities);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void notifyNoData() {
        mSmartRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
    }


    @Override
    public void updateFilterCategoryView(List<ProductFrontCategory> productFrontCategoryList) {
        if (productFrontCategoryList != null && !productFrontCategoryList.isEmpty()) {
            this.mProductFrontCategories = productFrontCategoryList;
            initFilterCategoryListView();
        }
    }

    @Override
    public void updateBannerView(List<Banner> bannerArrayList) {
        bannerView.setBannerWidthAndHeight(DeviceUtils.getScreenWidth(this), Utils.dip2px(this, 170));
        if(null != bannerArrayList && !bannerArrayList.isEmpty()) {
            ArrayList<String> bannerList = new ArrayList<>(bannerArrayList.size());
            for (Banner banner : bannerArrayList) {
                bannerList.add(banner.getPath());
            }
            bannerView.fillViewPager(bannerList);
            bannerView.setBannerClickListener(index -> {
                Banner banner = bannerArrayList.get(index);
                if (!TextUtils.isEmpty(banner.getProductId())) {
                    Intent intent = new Intent(this, CommodityDetailActivity.class);
                    intent.putExtra("id", banner.getProductId());
                    startActivity(intent);
                } else if (!TextUtils.isEmpty(banner.getStoreId())) {
                    //show store detail
                }
            });
        }
    }

    private void initFilterCategoryListView() {
        TextView tvTitle = (TextView) mLlCommodityListCategroy.findViewById(R.id.tv_common_filter_title);
        tvTitle.setText(R.string.label_commodity_filter_category);
        RecyclerView recyclerView = (RecyclerView) mLlCommodityListCategroy.findViewById(R.id.rcv_common_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new MarginDecoration(Utils.dip2px(5)));
        mFilterCategoryAdapter = new BaseQuickAdapter<ProductFrontCategory, BaseViewHolder>(R.layout.common_filter_item_layout, mProductFrontCategories) {

            @Override
            protected void convert(BaseViewHolder holder, ProductFrontCategory productFrontCategory) {
                holder.setOnCheckedChangeListener(R.id.cb_common_item, (view, isChecked) -> {
                    String item = String.valueOf(productFrontCategory.getId());
                    if (isChecked) {
                        mFilterCategoryParams.clear();
                        mFilterCategoryParams.add(item);
                    }
                });
                holder.setOnClickListener(R.id.cb_common_item, view -> {
                    //全部设为未选中
                    for (ProductFrontCategory bean : mProductFrontCategories) {
                        bean.setChecked(false);
                    }
                    //点击的设为选中
                    productFrontCategory.setChecked(true);
                    notifyDataSetChanged();
                });
                holder.setChecked(R.id.cb_common_item, productFrontCategory.isChecked());
                holder.setText(R.id.cb_common_item, productFrontCategory.getName());
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
        handleCategory();
        handlePrice();
        loadData();
        closeMenu();
    }
    
    private void handleCategory(){
        if (mFilterCategoryParams != null && !mFilterCategoryParams.isEmpty()) {
            mFilterParams.setProductFrontCategoryId(setupSelectStr(mFilterCategoryParams));
        }
    }

    private void handlePrice() {
        String minPrice = mEtMinPrice.getText().toString().trim();
        String maxPrice = mEtMaxPrice.getText().toString().trim();
        if(!TextUtils.isEmpty(minPrice)) {
            mFilterParams.setLowMum(minPrice);
        }
        if(!TextUtils.isEmpty(maxPrice)) {
            if(TextUtils.isEmpty(minPrice) || maxPrice.compareTo(minPrice) > 0) {
                mFilterParams.setTopMum(maxPrice);
            }else{
                mEtMinPrice.setText(maxPrice);
                mEtMaxPrice.setText(minPrice);
                mFilterParams.setTopMum(minPrice);
                mFilterParams.setLowMum(maxPrice);
            }
        }
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

    private void resetFilter() {
        resetFilterCategoryView();
        resetFilterDataView();
        mCommonFilterLayout.resetFilterTitle();
        loadData();
    }

    private void resetFilterCategoryView() {
        mFilterCategoryParams.clear();
        mFilterParams.setProductFrontCategoryId(null);
        //全部设为未选中
        for (ProductFrontCategory bean : mProductFrontCategories) {
            bean.setChecked(false);
        }
//        mProductFrontCategories.get(0).setChecked(true);
        mFilterCategoryAdapter.notifyDataSetChanged();
    }

    private void initFilterView() {
        TextView tvTitle = (TextView) mLlCommodityListPrice.findViewById(R.id.tv_common_filter_title);
        tvTitle.setText(R.string.label_commodity_filter_price);
        mEtMinPrice.setOnClickListener(view -> view.setSelected(true));
        mEtMaxPrice.setOnClickListener(view -> view.setSelected(true));
    }

    private void resetFilterDataView() {
        mFilterParams.setLowMum(null);
        mFilterParams.setTopMum(null);
        mEtMinPrice.setText(null);
        mEtMaxPrice.setText(null);
        mEtMinPrice.setSelected(false);
        mEtMaxPrice.setSelected(false);
    }
}
