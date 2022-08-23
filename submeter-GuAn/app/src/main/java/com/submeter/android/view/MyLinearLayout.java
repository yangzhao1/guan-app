package com.submeter.android.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.LinearLayout;

/**
 * 底图缩放，浮层不变
 *
 * @author yanglonghui
 */
public class MyLinearLayout extends LinearLayout {

    private int[] mInsets = new int[4];

    private ISizeChangeListener sizeChangeListener;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSizeChangeListener(ISizeChangeListener sizeChangeListener) {
        this.sizeChangeListener = sizeChangeListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (null != sizeChangeListener) {
            sizeChangeListener.onSizeChanged(w, h, oldw, oldh);
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    /*
     * 此方法以过期，当应用最低API支持为20后，可以重写以下方法
     */
    @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.getSystemWindowInsetLeft();
            mInsets[1] = insets.getSystemWindowInsetTop();
            mInsets[2] = insets.getSystemWindowInsetRight();
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;
            return super.fitSystemWindows(insets);
        } else {
            return super.fitSystemWindows(insets);
        }
    }
}
