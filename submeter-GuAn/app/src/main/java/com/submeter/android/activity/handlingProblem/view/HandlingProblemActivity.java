package com.submeter.android.activity.handlingProblem.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.handlingProblem.fragment.processed.view.ProcessedFragment;
import com.submeter.android.activity.handlingProblem.fragment.untreated.view.UntreatedFragment;
import com.submeter.android.interfacce.IChildFragment;
import com.submeter.android.view.SliderPageTransformer;
import com.submeter.android.view.ViewPagerSlide;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangzhao on 2019/3/28.
 */

public class HandlingProblemActivity extends BaseActivity {

    @BindView(R.id.untreated)
    TextView untreated;
    @BindView(R.id.untreated_indicator)
    View untreatedIndicator;
    @BindView(R.id.processed)
    TextView processed;
    @BindView(R.id.processed_indicator)
    View processedIndicator;
    @BindView(R.id.sep_line)
    View sepLine;
    @BindView(R.id.viewPages)
    ViewPagerSlide viewPages;

    private ProcessedFragment processedFragment;

    private IChildFragment[] childFragments = new IChildFragment[2];

    private int UNTREATED = 0;
    private int PROCESSED = 1;
    private int mViewIndex = -1;
    @BindColor(R.color.black_color)
    int mTabNormalColor;

    @BindColor(R.color.blue_color)
    int mTabSecletColor;
    private int currIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handlingproblem, R.string.handling_problem, R.drawable.ic_back, "", false);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.untreated, R.id.processed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.untreated:
                switchView(UNTREATED);
                break;
            case R.id.processed:
                switchView(PROCESSED);
                break;
        }
    }

    private void initView() {
        currIndex = getIntent().getIntExtra("currIndex",0);
        if (currIndex==1){ //为今日违规
            untreated.setText("今日治污表未打开");
            processed.setText("今日未响应停产");
        }
        viewPages.setSlide(false);
        viewPages.setAdapter(new MyPagerAdpter(getSupportFragmentManager()));
        viewPages.setPageTransformer(true, new SliderPageTransformer());
        viewPages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        switchView(0);
    }

    private void switchView(int type) {
        if (type == mViewIndex) {
            return;
        }

        processed.setTextColor(mTabNormalColor);
        untreated.setTextColor(mTabNormalColor);
        untreatedIndicator.setVisibility(View.GONE);
        processedIndicator.setVisibility(View.GONE);

        if (type == UNTREATED) {
            untreated.setTextColor(mTabSecletColor);
            untreatedIndicator.setVisibility(View.VISIBLE);
        } else if (type == PROCESSED) {
            processed.setTextColor(mTabSecletColor);
            processedIndicator.setVisibility(View.VISIBLE);
        }  else {
            return;
        }

        mViewIndex = type;
        viewPages.setCurrentItem(type);
    }

    public class MyPagerAdpter extends FragmentPagerAdapter {

        public MyPagerAdpter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (null == childFragments[position]) {
                if (position == 0) {
                    UntreatedFragment untreatedFragment = new UntreatedFragment();
                    Bundle bundle = new Bundle();
                    if (currIndex==1){//今日违规
                        bundle.putInt("handle",4);
                        bundle.putInt("type",1);
                    }else {
                        bundle.putInt("handle",0);
                        bundle.putInt("type",0);
                    }
                    untreatedFragment.setArguments(bundle);
                    childFragments[position] = untreatedFragment;
                } else if (position == 1) {
                    ProcessedFragment processedFragment = new ProcessedFragment();
                    Bundle bundle = new Bundle();
                    if (currIndex==1){//今日违规
                        bundle.putInt("handle",5);
                        bundle.putInt("type",1);
                    }else {
                        bundle.putInt("handle",1);
                        bundle.putInt("type",0);
                    }
                    processedFragment.setArguments(bundle);
                    childFragments[position] = processedFragment;
                }
            }
            return childFragments[position].getFragment();
        }

        @Override
        public int getCount() {
            return childFragments.length;
        }
    }


}
