package com.submeter.android.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zwx on 17-4-7
 * RecyclerView 以Grid方式垂直排列，每一项item都是一个小卡片（cardview）
 * 用于在item之间产生间距
 */
public class CategorySpaceItemDecoration extends RecyclerView.ItemDecoration {

	private static final int DEFAULT_COLUMN = Integer.MAX_VALUE;
	private int space;
	private int column;

	private CategoryBrandAdapter categoryBrandAdapter;

	public CategorySpaceItemDecoration(CategoryBrandAdapter categoryBrandAdapter, int space) {
		this(categoryBrandAdapter, space, DEFAULT_COLUMN);
	}

	public CategorySpaceItemDecoration(CategoryBrandAdapter categoryBrandAdapter, int space, int column) {
		this.categoryBrandAdapter = categoryBrandAdapter;
		this.space = space;
		this.column = column;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);

		int position = parent.getChildLayoutPosition(view);
		int realIndex = categoryBrandAdapter.getRealGridItemPosition(position);
		if(realIndex >= 0) {
			outRect.bottom = space;
			if (realIndex % column == 0){  //parent.getChildLayoutPosition(view) 获取view的下标
				outRect.left = 0;
			} else {
				outRect.left = space;
			}
		} else {
			outRect.bottom = 0;
		}
	}

	boolean isFirstRow(int pos) {
		return pos < column;
	}

	boolean isLastRow(int pos, int total) {
		return total - pos <= column;
	}

	boolean isFirstColumn(int pos) {
		return pos % column == 0;
	}

	boolean isSecondColumn(int pos) {
		return isFirstColumn(pos - 1);
	}

	boolean isEndColumn(int pos) {
		return isFirstColumn(pos + 1);
	}

	boolean isNearEndColumn(int pos) {
		return isEndColumn(pos + 1);
	}

}