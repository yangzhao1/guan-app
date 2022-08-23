package com.submeter.android.view;

public interface OnRefreshListener {
	public static final int RELOAD = 0;

	public static final int LOAD_MORE = 1;

	public void onRefresh();

	public void onLoadMore();

	public boolean isRefresh();
}