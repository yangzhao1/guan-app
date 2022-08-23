package com.submeter.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.network.NetworkRequestTool;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 赵勃 on 2018/11/25.
 */

public class BannerView extends FrameLayout implements View.OnClickListener {
    private final int DEFAULT_INTERVAL = 2000;

    private boolean autoPlay = false;

    private int bannerWidth = 1080;

    private int bannerHeight = 800;

    private Integer indicatorNormalRes = 0;

    private Integer indicatorCheckedRes = 0;

    private int interval = DEFAULT_INTERVAL;

    private int pageIndex = 10000;

    private Context context;

    private Handler handler;

    @BindView(R.id.viewPages)
    ViewPager viewPager;

    @BindView(R.id.viewGroup)
    ViewGroup indicatorViewGroup;

    private MyPagerAdapter pageAdapter;

    private BannerClickListener bannerClickListener;

    private ArrayList<View> pageViews = new ArrayList<>();

    private ArrayList<ImageView> indicatorViews = new ArrayList<>();

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerView, defStyleAttr, 0);

        autoPlay = a.getBoolean(R.styleable.BannerView_auto_play, false);
        interval = a.getInt(R.styleable.BannerView_interval, DEFAULT_INTERVAL);
        indicatorNormalRes = a.getResourceId(R.styleable.BannerView_indicator_normal, R.drawable.unselect_banner_dot);
        indicatorCheckedRes = a.getResourceId(R.styleable.BannerView_indicator_checked, R.drawable.select_banner_dot);

        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpage, this, true);
        ButterKnife.bind(this, view);

        if(autoPlay) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // TODO Auto-generated method stub
                    super.handleMessage(msg);

                    int totalcount = pageAdapter.getCount();
                    int currentItem = viewPager.getCurrentItem();

                    int toItem = currentItem + 1 == totalcount ? 0 : currentItem + 1;

                    viewPager.setCurrentItem(toItem, true);
                    sendEmptyMessageDelayed(1, interval);
                }
            };
        }

        initViewPager();
    }

    private void initViewPager() {
        pageAdapter = new MyPagerAdapter();
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    public void setBannerWidthAndHeight(int width, int height) {
        this.bannerWidth = width;
        this.bannerHeight = height;
    }

    public void fillViewPager(ArrayList<String> urls) {
        try {
            pageIndex = 0;
            if(handler != null) {
                handler.removeMessages(1);
            }
            clearViewPagerCache();
            pageViews.clear();
            pageAdapter.notifyDataSetChanged();
            indicatorViews.clear();
            indicatorViewGroup.removeAllViews();

            int viewSize = initBannerViews(urls);
            initPageIndicator(viewSize);

            int size = pageViews.size();
            if (size > 1) {
                viewPager.setCurrentItem(pageIndex);
                if(handler != null) {
                    handler.sendEmptyMessageDelayed(1, interval);
                }
            } else if(size == 1) {
                viewPager.setCurrentItem(0);
            }

            pageAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearViewPagerCache() {
        ViewGroup childView;
        ImageView imageView;
        int cacheSize = pageViews.size();

        for (int i = 0; i < cacheSize; ++i) {
            childView = (ViewGroup) pageViews.get(i);
            childView.removeAllViews();
            viewPager.removeAllViews();
        }
    }

    private int initBannerViews(ArrayList<String> urls) {
        SimpleDraweeView photoView;
        int size = urls.size();
        int length;
        if (size > 1 && size < 4) {
            length = size << 1;
        } else {
            length = size;
        }

        String url;
        for (int i = 0; i < length; ++i) {
            url = urls.get(i % size);
            photoView = new SimpleDraweeView(context);
            photoView.setTag(i);
            photoView.getHierarchy().setPlaceholderImage(context.getResources().getDrawable(R.mipmap.banner_default), ScalingUtils.ScaleType.CENTER_CROP);
            photoView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
            photoView.setOnClickListener(this);
            NetworkRequestTool.getInstance().loadImage(Uri.parse(SubmeterApp.getInstance().getCustomSizeImageStyle(url, bannerWidth, bannerHeight)), photoView);
            pageViews.add(photoView);
            pageAdapter.notifyDataSetChanged();
        }

        return size;
    }

    private void initPageIndicator(int size) {
        for (int i = 0; i < size && size > 1; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (i == size - 1) {
                imageView.setPadding(10, 10, 10, 10);
            } else {
                imageView.setPadding(10, 10, 0, 10);
            }
            imageView.setImageResource(i == pageIndex ? indicatorCheckedRes : indicatorNormalRes);
            indicatorViews.add(imageView);
            indicatorViewGroup.addView(imageView);
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        private int childCount = 0;

        @Override
        public int getCount() {
            if (pageViews.size() < 2) {
                return pageViews.size();
            } else {
                return Integer.MAX_VALUE;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void notifyDataSetChanged() {
            childCount = pageViews.size();

            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (childCount > 0) {
                --childCount;
                return POSITION_NONE;
            }

            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
            int index = position % pageViews.size();
            view.removeView(pageViews.get(index));
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int arg1) {
            if (pageViews.size() == 0) {
                return null;
            }

            int index = arg1 % pageViews.size();
            View childView = pageViews.get(index);
            ViewGroup parantView = (ViewGroup) childView.getParent();
            if (null != parantView) {
                parantView.removeAllViews();
            }
            viewGroup.addView(childView, 0);
            return pageViews.get(index);
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
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
            int count = indicatorViews.size();
            for (int i = 0; i < count; i++) {
                indicatorViews.get(i).setImageResource(indicatorNormalRes);
                if (newPage % count == i) {
                    indicatorViews.get(i).setImageResource(indicatorCheckedRes);
                }
            }

            pageIndex = newPage;
        }
    }

    @Override
    public void onClick(View view) {
        if(bannerClickListener == null) {
            return;
        }

        Object tagObj = view.getTag();
        if(null != tagObj) {
            int index = Integer.parseInt(tagObj.toString());
            bannerClickListener.bannerClicked(index);
        }
    }

    public int getBannerHeight() {
        return bannerHeight;
    }

    public void setBannerClickListener(BannerClickListener bannerClickListener) {
        this.bannerClickListener = bannerClickListener;
    }

    public interface BannerClickListener {
        public void bannerClicked(int index);
    }
}
