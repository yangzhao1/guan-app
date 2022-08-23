package com.submeter.android.activity.search.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.search.model.ISearchModel;
import com.submeter.android.activity.search.presenter.SearchPresenter;
import com.submeter.android.activity.search.presenter.ISearchPresenter;
import com.submeter.android.view.SliderPageTransformer;
import com.submeter.android.view.ViewPagerSlide;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements ISearchView {

    private ISearchPresenter searchPresenter;

    //当前选中的tab view
    private int currentTabView = -1;

    @BindColor(R.color.blue_color)
    int selectedTextColor;

    @BindColor(R.color.black_color)
    int unselectedTextColor;

    @BindView(R.id.good_view)
    TextView goodTabView;

    @BindView(R.id.brand_view)
    TextView brandTabView;

    @BindView(R.id.good_indicator)
    View goodIndicatorView;

    @BindView(R.id.brand_indicator)
    View brandIndicatorView;

    @BindView(R.id.title_search)
    EditText searchView;

    @BindView(R.id.viewPages)
    ViewPagerSlide mViewPager;

    private ISearchFragment[] childFragments = new ISearchFragment[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        darkStatusBar = false;
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mViewPager.setSlide(false);
        mViewPager.setAdapter(new MyPagerAdpter(getSupportFragmentManager()));
        mViewPager.setPageTransformer(true, new SliderPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switchView(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }


            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int key, KeyEvent keyEvent) {
                if(key == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = searchView.getEditableText().toString().trim();
                    if(currentTabView == COMMODITY_TAB) {
                        searchPresenter.search(ISearchModel.SEARCH_COMMODITY, keyword);
                    } else if(currentTabView == BRAND_TAB) {
                        searchPresenter.search(ISearchModel.SEARCH_BRAND, keyword);
                    }
                }
                return false;
            }
        });

        searchPresenter = new SearchPresenter(this);
        switchView(COMMODITY_TAB);
    }

    private void switchView(int view) {
        if(view == currentTabView) {
            return;
        }

        this.currentTabView = view;
        if(view == COMMODITY_TAB) {
            goodTabView.setTextColor(selectedTextColor);
            goodIndicatorView.setVisibility(View.VISIBLE);

            brandTabView.setTextColor(unselectedTextColor);
            brandIndicatorView.setVisibility(View.GONE);
        } else if(view == BRAND_TAB) {
            brandTabView.setTextColor(selectedTextColor);
            brandIndicatorView.setVisibility(View.VISIBLE);

            goodTabView.setTextColor(unselectedTextColor);
            goodIndicatorView.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.left_image, R.id.good_view, R.id.brand_view})
    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.left_image: {
                finish();
                break;
            }

            case R.id.good_view: {
                if (mViewPager.getCurrentItem() != COMMODITY_TAB) {
                    mViewPager.setCurrentItem(COMMODITY_TAB);
                }
                break;
            }

            case R.id.brand_view: {
                if (mViewPager.getCurrentItem() != BRAND_TAB) {
                    mViewPager.setCurrentItem(BRAND_TAB);
                }
                break;
            }

            default:
                break;
        }
    }

    public class MyPagerAdpter extends FragmentPagerAdapter {

        public MyPagerAdpter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (null == childFragments[position]) {
                if (position == 0) {
                    SearchFragment fragment = new SearchFragment();
                    fragment.setPresenter(searchPresenter);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Key_Type", ISearchModel.SEARCH_COMMODITY);
                    fragment.setArguments(bundle);
                    childFragments[position] = fragment;
                } else if (position == 1) {
                    SearchFragment fragment = new SearchFragment();
                    fragment.setPresenter(searchPresenter);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Key_Type", ISearchModel.SEARCH_BRAND);
                    fragment.setArguments(bundle);
                    childFragments[position] = fragment;
                }
            }

            return childFragments[position].getFragment();
        }

        @Override
        public int getCount() {
            return childFragments.length;
        }
    }

    @Override
    public void updateView(int tabView, List<String> listData) {
        childFragments[tabView].updateView(listData);
    }
}
