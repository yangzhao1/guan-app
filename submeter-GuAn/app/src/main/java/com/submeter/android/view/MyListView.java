package com.submeter.android.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.tools.Utils;

public class MyListView extends ListView implements OnScrollListener {
    public final static int SCROLL_UP = 0;
    public final static int SCROLL_DOWN = 1;
    private final static int RELEASE_To_REFRESH = 0;// 松开刷新
    private final static int PULL_To_REFRESH = 1;// 下拉刷新
    private final static int REFRESHING = 2;// 正在刷新
    private final static int DONE = 3;
    private final static int LOADING = 4;
    // 实际的padding的距离与界面上偏移距离的比例
    private final static double RATIO = 2;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecored;
    private boolean isRefreshing = false;
    private boolean isAutoLoading = false;
    private boolean isScrolling = false;
    private boolean refreshable = false;

    private boolean hasMoreData = false;

    private int state = DONE;
    private int startY;
    private int scrollOrientation = -1;//0: up; 1: down
    private int headContentHeight;
    private int firstItemIndex;

    private long lastTime = System.currentTimeMillis();

    private Context mContext;

    private LayoutInflater inflater;
    private LinearLayout headView;
    //private ProgressBar progressBar;
    private ImageView progressView;
    private TextView lastUpdatedTextView;
    private OnScrollListener selfScrollListener;

    private AnimationDrawable animationDrawable;

    private OnRefreshListener onRefreshListener = null;

    private SparseArray<ItemRecod> recordSp = new SparseArray(0);
    private int lastTouchY = -1;
    private float xDistance, yDistance, lastX, lastY;

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void setRefreshable(boolean refreshable) {
        this.refreshable = refreshable;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    private void init(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        headView = (LinearLayout) Utils.inflateView(context, inflater, R.layout.item_refresh, null);

        progressView = (ImageView) headView.findViewById(R.id.head_progressBar);
        animationDrawable = (AnimationDrawable) progressView.getDrawable();
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.lastUpdatedTextView);

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();

        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();
        addHeaderView(headView, null, false);
        setOnScrollListener(this);
        //setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true, this));
    }

    public void setAdapter(BaseAdapter adapter) {
        lastUpdatedTextView.setText(String.format(mContext.getString(R.string.refresh_last), Utils.timeToShow(mContext, lastTime)));
        lastTime = System.currentTimeMillis();
        super.setAdapter(adapter);
    }

    public void setSelfScrollListener(OnScrollListener scrollListener) {
        selfScrollListener = scrollListener;
    }

    public void onScroll(AbsListView view, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
        /*if (firstVisiableItem == 0) {
            setFastScrollEnabled(false);
		} else {
			setFastScrollEnabled(true);
		}*/
        firstItemIndex = firstVisiableItem;
        if (selfScrollListener != null) {
            ItemRecod itemRecord;
            View firstView = view.getChildAt(0);
            if (null != firstView) {
                itemRecord = recordSp.get(firstVisiableItem);
                if (null == itemRecord) {
                    itemRecord = new ItemRecod();
                }
                itemRecord.height = firstView.getHeight();
                itemRecord.top = firstView.getTop();
                recordSp.append(firstVisiableItem, itemRecord);
            }

            selfScrollListener.onScroll(view, firstVisiableItem, visibleItemCount, totalItemCount);
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (selfScrollListener != null) {
            selfScrollListener.onScrollStateChanged(view, scrollState);
        }

        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            isScrolling = false;
            scrollOrientation = -1;

            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() >= view.getCount() - 2) {
                if (hasMoreData && !isRefreshing && !isAutoLoading && onRefreshListener != null) {
                    isAutoLoading = true;
                    onRefreshListener.onLoadMore();
                }
            }
        } else {
            isScrolling = true;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored) {
                        isRecored = true;
                        startY = (int) event.getY();
                    }

                    lastTouchY = (int) event.getY();
                    scrollOrientation = -1;
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (state != REFRESHING && state != LOADING) {
                        if (state == DONE) {
                            // 什么都不做
                        }
                        if (refreshable) {
                            if (state == PULL_To_REFRESH) {
                                state = DONE;
                                changeHeaderViewByState();
                            }
                            if (state == RELEASE_To_REFRESH) {
                                state = REFRESHING;
                                changeHeaderViewByState();
                                onRefresh();
                            }
                        }
                    }

                    isRecored = false;
                    lastTouchY = -1;
                    break;

                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();
                    if (lastTouchY == -1) {
                        lastTouchY = tempY;
                    }
                    if (tempY - lastTouchY > 0) {
                        scrollOrientation = SCROLL_DOWN;
                    } else if (tempY - lastTouchY < 0) {
                        scrollOrientation = SCROLL_UP;
                    }
                    lastTouchY = tempY;
                    if (refreshable) {
                        if (!isRecored && firstItemIndex == 0) {
                            lastUpdatedTextView.setText(String.format(mContext.getString(R.string.refresh_last), Utils.timeToShow(mContext, lastTime)));
                            isRecored = true;
                            startY = tempY;
                        }

                        if (state != REFRESHING && isRecored && state != LOADING) {
                            // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
                            // 可以松手去刷新了
                            if (state == RELEASE_To_REFRESH) {
                                setSelection(0);
                                // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                                if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {
                                    state = PULL_To_REFRESH;
                                    changeHeaderViewByState();
                                }
                                // 一下子推到顶了
                                else if (tempY - startY <= 0) {
                                    state = DONE;
                                    changeHeaderViewByState();
                                }
                                // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                                else {
                                    // 不用进行特别的操作，只用更新paddingTop的值就行了
                                }
                            }
                            // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                            if (state == PULL_To_REFRESH) {
                                setSelection(0);
                                // 下拉到可以进入RELEASE_TO_REFRESH的状态
                                if ((tempY - startY) / RATIO >= headContentHeight) {
                                    state = RELEASE_To_REFRESH;
                                    changeHeaderViewByState();
                                }
                                // 上推到顶了
                                else if (tempY - startY <= 0) {
                                    state = DONE;
                                    changeHeaderViewByState();
                                }
                            }
                            // done状态下
                            if (state == DONE) {
                                if (tempY - startY > 0) {
                                    state = PULL_To_REFRESH;
                                    changeHeaderViewByState();
                                }
                            }
                            // 更新headView的size
                            if (state == PULL_To_REFRESH) {
                                headView.setPadding(0, -1 * headContentHeight + (int) ((tempY - startY) / RATIO), 0, 0);
                            }

                            // 更新headView的paddingTop
                            if (state == RELEASE_To_REFRESH) {
                                headView.setPadding(0, (int) ((tempY - startY) / RATIO) - headContentHeight, 0, 0);
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(event);
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        if (!refreshable) {
            return;
        }
        switch (state) {
            case RELEASE_To_REFRESH: {
                progressView.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
            }

            case PULL_To_REFRESH: {
                animationDrawable.start();
                progressView.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
            }

            case REFRESHING: {
                progressView.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);

                break;
            }

            case DONE: {
                headView.setPadding(0, -1 * headContentHeight, 0, 0);

                progressView.setVisibility(View.GONE);
                animationDrawable.stop();
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
            }

            default:
                break;
        }
    }

    public void onRefreshComplete() {
        try {
            state = DONE;
            isRefreshing = false;
            lastUpdatedTextView.setText(String.format(mContext.getString(R.string.refresh_last), Utils.timeToShow(mContext, lastTime)));
            lastTime = System.currentTimeMillis();
            changeHeaderViewByState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onRefresh() {
        if (refreshable && !isRefreshing && !isAutoLoading && onRefreshListener != null) {
            isRefreshing = true;
            onRefreshListener.onRefresh();
        }
    }

    public void onRefreshAndShowRefreshView() {
        if (refreshable && !isRefreshing && !isAutoLoading && onRefreshListener != null) {
            isRefreshing = true;
            state = PULL_To_REFRESH;
            headView.setPadding(0, headContentHeight, 0, 0);
            changeHeaderViewByState();
            onRefreshListener.onRefresh();
        }
    }

    // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void onLoadingFinish() {
        isAutoLoading = false;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if (xDistance > yDistance)
                    return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public boolean isScrolling() {
        return isScrolling;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public boolean isAutoLoading() {
        return isAutoLoading;
    }

    public int getScrollOrientation() {
        return scrollOrientation;
    }

    public void setScrollOrientation(int orientation) {
        this.scrollOrientation = orientation;
    }

    public int getMyScrollY() {
        int height = 0;
        int firstVisibleItem = getFirstVisiblePosition();
        for (int i = 0; i < firstVisibleItem; i++) {
            ItemRecod itemRecod = recordSp.get(i);
            if (null != itemRecod) {
                height += itemRecod.height;
            }
        }
        ItemRecod itemRecod = recordSp.get(firstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }

    @Override
    public void addFooterView(View footerView) {
        super.addFooterView(footerView);

        Object tag = footerView.getTag();
        if (tag != null && SystemConstant.LOAD_MORE_TAG.equals(tag.toString())) {
            hasMoreData = true;
        } else {
            hasMoreData = false;
        }
    }

    @Override
    public void addFooterView(View footerView, Object data, boolean isSelectable) {
        super.addFooterView(footerView, data, isSelectable);

        Object tag = footerView.getTag();
        if (tag != null && SystemConstant.LOAD_MORE_TAG.equals(tag.toString())) {
            hasMoreData = true;
        } else {
            hasMoreData = false;
        }
    }

    @Override
    public boolean removeFooterView(View footerView) {
        Object tag = footerView.getTag();
        if (tag != null && SystemConstant.LOAD_MORE_TAG.equals(tag.toString())) {
            hasMoreData = false;
        }

        return super.removeFooterView(footerView);
    }

    class ItemRecod {
        int height = 0;
        int top = 0;
    }
}