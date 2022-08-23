package com.submeter.android.activity.produceIntroduce;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.login.view.LoginActivity;
import com.submeter.android.constants.DBConstant;
import com.submeter.android.db.ShareStoreProcess;
import com.submeter.android.tools.Utils;
import com.submeter.android.view.DepthPageTransformer;

import java.util.ArrayList;

import butterknife.BindView;

public class ProduceIntroductionActivity extends BaseActivity {

    public static final String TAG = "ProduceIntroductionActivity";

    @BindView(R.id.viewPages)
    ViewPager viewPager;

    @BindView(R.id.layout_viewpager)
    View viewPagerLayout;

    @BindView(R.id.viewGroup)
    ViewGroup viewGroup;

    private int pageIndex = 0;

    private LayoutInflater layoutInflater;

    private ArrayList<View> pageViews = new ArrayList<View>();

    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();

    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar();

        super.onCreate(savedInstanceState);
        swipeBackEnable = false;
        setContentView(R.layout.item_viewpage);

        initView();
    }

    private void initView() {
        layoutInflater = (LayoutInflater) (getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View view = getView(R.mipmap.intro1, false);
        pageViews.add(view);

        view = getView(R.mipmap.intro2, false);
        pageViews.add(view);

        view = getView(R.mipmap.intro3, true);
        pageViews.add(view);

        int padding = Utils.dip2px(this, 10);
        int pageSize = pageViews.size();
        for (int i = 0; i < pageSize && pageSize > 1; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            if (i == pageSize - 1) {
                imageView.setPadding(padding, padding, padding, padding);
            } else {
                imageView.setPadding(padding, padding, 0, padding);
            }

            imageView.setImageResource(i == pageIndex ? R.drawable.select_banner_dot : R.drawable.white_trans_dot);

            imageViews.add(imageView);
            viewGroup.addView(imageView);
        }

        ((RelativeLayout.LayoutParams) viewPager.getLayoutParams()).bottomMargin = 0;
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    private View getView(int resId, boolean isEndPage) {
        View pageView = layoutInflater.inflate(R.layout.item_introduction, null);

        SimpleDraweeView imageView = (SimpleDraweeView) pageView.findViewById(R.id.image_view);
        imageView.setImageURI(Uri.parse(Utils.getResUriPrefix(this) + resId));

        if (isEndPage) {
            Button goBtn = (Button) pageView.findViewById(R.id.go);
            goBtn.setOnClickListener(this);
            goBtn.setVisibility(View.VISIBLE);
        }

        return pageView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.go) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.SHOW_PRODUCT_INTRODUCE, "false");
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup viewGroup, int arg1, Object arg2) {
            // TODO Auto-generated method stub
            viewGroup.removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int arg1) {
            // TODO Auto-generated method stub
            viewGroup.addView(pageViews.get(arg1));
            return pageViews.get(arg1);
        }
    }

    class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int newPage) {
            int count = imageViews.size();
            for (int i = 0; i < count; i++) {
                if (newPage == i) {
                    imageViews.get(i).setImageResource(R.drawable.select_banner_dot);
                } else {
                    imageViews.get(i).setImageResource(R.drawable.white_trans_dot);
                }
            }

            viewGroup.setVisibility(newPage == count - 1 ? View.GONE : View.VISIBLE);

            pageIndex = newPage;
        }
    }
}