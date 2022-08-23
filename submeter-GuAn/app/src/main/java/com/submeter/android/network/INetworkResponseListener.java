package com.submeter.android.network;

import com.android.volley.VolleyError;

public interface INetworkResponseListener {

	public void onResponse(String result);

	public void onErrorResponse(VolleyError error);
}