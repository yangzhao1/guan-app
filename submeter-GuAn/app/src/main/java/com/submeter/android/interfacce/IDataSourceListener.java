package com.submeter.android.interfacce;

public interface IDataSourceListener<T> {

	public void onLoadFinish(T data);

	public void onLoadFail(int errorCode, String errorMessage);
}