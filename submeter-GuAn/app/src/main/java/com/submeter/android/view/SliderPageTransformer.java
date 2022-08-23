package com.submeter.android.view;

import android.support.v4.view.ViewPager;
import android.view.View;

public class SliderPageTransformer implements ViewPager.PageTransformer {
    /**
     * 实现动画主要方法，每一个Fragment在滑动过程中都会调用这个方法。
     *
     * @param page：每一个Page；
     * @param position：当前Page所在位置，0时表示当前Page正在显示。
     */
    @Override
    public void transformPage(View page, float position) {
        int width = page.getWidth();
        if (position < -1) {
            page.setScrollX((int) (width * 0.75 * -1));
        } else if (position <= 1) {
            if (position < 0) {
                page.setScrollX((int) (width * 0.75 * position));
            } else {
                page.setScrollX((int) (width * 0.75 * position));
            }
        } else {
            page.setScrollX((int) (width * 0.75));
        }
    }
}