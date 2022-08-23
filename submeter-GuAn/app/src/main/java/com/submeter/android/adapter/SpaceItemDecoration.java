package com.submeter.android.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zwx on 17-4-7
 * RecyclerView 以Grid方式垂直排列，每一项item都是一个小卡片（cardview）
 * 用于在item之间产生间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

	private static final int DEFAULT_COLUMN = Integer.MAX_VALUE;
	private int space;
	private int column;

	public SpaceItemDecoration(int space) {
		this(space, DEFAULT_COLUMN);
	}

	public SpaceItemDecoration(int space, int column) {
		this.space = space;
		this.column = column;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);
		outRect.bottom = space;
		if (parent.getChildLayoutPosition(view)%column == 0){  //parent.getChildLayoutPosition(view) 获取view的下标
			outRect.left = 0;
		} else {
			outRect.left = space;
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