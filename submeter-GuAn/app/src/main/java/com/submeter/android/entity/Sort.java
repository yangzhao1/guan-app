package com.submeter.android.entity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.submeter.android.R;

/**
 * @author thm
 * @date 2018/12/13
 */
public class Sort {
    /**
     * 升序
     */
    private static final String ASC_SORT = "asc";
    /**
     * 降序
     */
    private static final String DESC_SORT = "desc";
    /**
     * 当前排序方式
     */
    private String currentSort = ASC_SORT;
    /**
     * 下一次的排序方式
     */
    private String nextSort;

    public Sort() {

    }

    public Sort(String currentSort) {
        this.currentSort = currentSort;
    }

    public String getCurrentSort() {
        return currentSort;
    }

    /**
     * 调用一次切换一次排序方式
     *
     * @return
     */
    public String getNextSort() {
        if (ASC_SORT.equalsIgnoreCase(currentSort)) {
            this.nextSort = DESC_SORT;
        } else if (DESC_SORT.equalsIgnoreCase(currentSort)) {
            this.nextSort = ASC_SORT;
        } else {
            this.nextSort = ASC_SORT;
        }
        return nextSort;
    }

    /**
     * 设置排序图标
     *
     * @param view
     */
    public void setSortIcon(View view) {
        if (view != null && view instanceof TextView) {
            TextView textView = (TextView) view;
            if (TextUtils.equals(currentSort, ASC_SORT)) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_sort_asc, 0);
            } else if (TextUtils.equals(currentSort, DESC_SORT)) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_sort_desc, 0);
            }
        }
    }
}
