package com.submeter.android.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.entity.Sort;
import com.submeter.android.entity.params.CommonQueryParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品列表排序控件
 *
 * @author thm
 * @date 2018/12/13
 */
public class CommonFilterLayout extends LinearLayout implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View mView;
    private TextView mTvComposite;
    private TextView mTvSales;
    private TextView mTvPrice;
    private ImageView mIvChange;
    private TextView mTvFilter;

    /**
     * 管理排序控件
     */
    private List<TextView> mSortView = new ArrayList<>();

    /**
     * 排序请求参数
     */
    private CommonQueryParam mFilterParams;
    /**
     * 点击切换布局的回调函数
     */
    private OnChangeLayoutListener mOnChangeLayoutListener;

    /**
     * 点击过滤按钮的回调函数
     */
    private OnFilterClickListener mOnFilterClickListener;
    /**
     * 点击排序后的回调函数
     */
    private OnSortListener mOnSortListener;

    /**
     * 是否是网格布局
     */
    private boolean isGridView;

    public CommonFilterLayout(Context context) {
        this(context, null);
    }

    public CommonFilterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonFilterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initSortView();
    }

    private void initView() {

        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.item_common_filter_layout, null);

            mTvComposite = (TextView) mView.findViewById(R.id.tv_common_composite);
            mTvSales = (TextView) mView.findViewById(R.id.tv_common_sales);
            mTvPrice = (TextView) mView.findViewById(R.id.tv_common_price);
            mIvChange = (ImageView) mView.findViewById(R.id.iv_common_change);
            mTvFilter = (TextView) mView.findViewById(R.id.tv_common_filter);

            mTvComposite.setOnClickListener(this);
            mTvSales.setOnClickListener(this);
            mTvPrice.setOnClickListener(this);
            mIvChange.setOnClickListener(this);
            mTvFilter.setOnClickListener(this);

            Toolbar.LayoutParams lp = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT);

            addView(mView, lp);
        }
    }

    private void initSortView() {
        mSortView.add(mTvComposite);
        mSortView.add(mTvSales);
        mSortView.add(mTvPrice);
    }

    private void setDataAndResetOther(View view) {
        String sort = (String) view.getTag();
        Sort currentSortEntity = null;
        if (TextUtils.isEmpty(sort)) {
            currentSortEntity = new Sort();
        } else {
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
        currentSortEntity.setSortIcon(view);
        if (mFilterParams != null) {
            mFilterParams.setIntegratedSort(null);
            mFilterParams.setPriceSort(null);
            mFilterParams.setSalesSort(null);
            if (view == mTvComposite) {
                mFilterParams.setIntegratedSort(currentSort);
            } else if (view == mTvPrice) {
                mFilterParams.setPriceSort(currentSort);
            } else if (view == mTvSales) {
                mFilterParams.setSalesSort(currentSort);
            }
        }
        if(mOnSortListener != null) {
            mOnSortListener.onSortFinish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mIvChange) {
            changeLayout();
        } else if (view == mTvFilter) {
            filter();
        } else {
            setDataAndResetOther(view);
        }
    }

    private void changeLayout() {
        if(isGridView) {
            mIvChange.setImageResource(R.drawable.ic_common_gridview);
        }else{
            mIvChange.setImageResource(R.drawable.ic_common_listview);
        }
        isGridView = !isGridView;
        if (mOnChangeLayoutListener != null) {
            mOnChangeLayoutListener.onChangeLayout(isGridView);
        }

    }

    private void filter() {
        selectedFilterTitle();
        if (mOnFilterClickListener != null) {
            mOnFilterClickListener.onFilterClick();
        }
    }

    private void selectedFilterTitle() {
        mTvFilter.setSelected(true);
        mTvFilter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invitation_filter_selected, 0);
    }

    public void resetFilterTitle() {
        mTvFilter.setSelected(false);
        mTvFilter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invitation_filter, 0);
    }

    public void setOnChangeLayoutListener(OnChangeLayoutListener onChangeLayoutListener) {
        this.mOnChangeLayoutListener = onChangeLayoutListener;
    }

    public void setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.mOnFilterClickListener = onFilterClickListener;
    }

    public void setOnSortListener(OnSortListener onSortListener) {
        this.mOnSortListener = onSortListener;
    }

    public void setFilterParams(CommonQueryParam filterParams) {
        this.mFilterParams = filterParams;
    }

    public interface OnChangeLayoutListener {
        void onChangeLayout(boolean isGridView);
    }

    public interface OnFilterClickListener {
        void onFilterClick();
    }

    public interface OnSortListener {
        void onSortFinish();
    }
}
