package com.submeter.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.tools.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

public class ListAdapter extends BaseAdapter {
	static String Tag = ListAdapter.class.getSimpleName();
	
	private int mResource;

	private int[] mTo;
	
	private String[] mFrom;

	private Context mContext;

	private LayoutInflater mLayoutInflater;
	
	public ArrayList<HashMap<String, Object>> mData;

	public ListAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
		mContext = context;
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	private void loadImage(Uri url, SimpleDraweeView imageView) {
		NetworkRequestTool.getInstance().loadImage(url, imageView, true);
        imageView.setTag(url);
	}
	
	public void clearMemory() {
		if (mData != null) {
			mData.clear();
		}

		mLayoutInflater = null;
	}

	/**
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	/**
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		if (position < 0 || mData == null || position > mData.size()) {
			return null;
		} else {
			return mData.get(position);
		}
	}

	/**
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		try {
			if (convertView == null) {
				view = inflaterView();
			} else {
				view = convertView;
			}

			bindView(position, (HashMap<Integer, View>)view.getTag());

			return view;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return null;
	}

	protected View inflaterView() {
		View view = Utils.inflateView(mContext, mLayoutInflater, mResource, null);

		int[] to = mTo;
		HashMap<Integer, View> viewHolder = new HashMap<>();
		for (int i : to) {
			viewHolder.put(i, view.findViewById(i));
		}
		view.setTag(viewHolder);

		return view;
	}

	private void bindView(int position, HashMap<Integer, View> holder) {
		final Map dataSet = mData.get(position);
		if (dataSet == null) {
			return;
		}

		String[] from = mFrom;
		int[] to = mTo;
		View v = null;
		for (int i = 0; i < from.length && i < to.length; i++) {
			Object data = dataSet.get(from[i]);
			v = holder.get(to[i]);

			if (v instanceof Checkable && data instanceof Boolean) {
				((Checkable) v).setChecked((Boolean) data);
			} else if (v instanceof TextView) {
				setViewText((TextView) v, data);
			} else if (v instanceof SimpleDraweeView) {
                if(null == data) {
                    v.setVisibility(View.GONE);
                } else {
                    setViewImage((SimpleDraweeView) v, Uri.parse(data.toString()));
                }
			} else if (v instanceof ImageView) {
                if(data instanceof Integer) {
                    setViewImage((ImageView) v, (Integer) data);
                } else {
                    v.setVisibility(View.GONE);
                }
            } else if (v instanceof ViewGroup) {
				if(null == data) {
					v.setVisibility(View.GONE);
				} else {
					v.setBackgroundResource((Integer) data);
					v.setVisibility(View.VISIBLE);
				}
			} else if (v instanceof RadioButton && data instanceof Integer) {
				setRadioButton((RadioButton) v, (Integer) data);
			}
		}
	}

    private void setViewImage(ImageView v, int value) {
        v.setTag(value);
        if (value != -1) {
            v.setImageResource(value);
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

	private void setViewImage(SimpleDraweeView v, Uri value) {
		if (value == null) {
            v.setTag(value);
			v.setVisibility(View.GONE);
		} else {
			try {
				v.setVisibility(View.VISIBLE);
                loadImage(value, v);
			} catch (RejectedExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private void setViewText(TextView v, Object text) {
		do {
			v.setText("");
			if (null == text) {
				v.setVisibility(View.GONE);
				break;
			}

			if(text instanceof SpannableString) {
				v.setText((SpannableString)text);
				v.setVisibility(View.VISIBLE);
				break;
			}

			if (TextUtils.isEmpty(text.toString())) {
				break;
			}
			v.setVisibility(View.VISIBLE);

			if (!text.toString().contains("href") && !text.toString().contains("</font>")) {
				v.setText(text.toString());
				break;
			}

			v.setText(Html.fromHtml(text.toString()));
			if (text.toString().contains("href")) {
				v.setFocusable(true);
				v.setMovementMethod(LinkMovementMethod.getInstance());
			}
			v.setText(v.getText());
		} while (false);
	}

	private void setRadioButton(RadioButton v, int resId) {
		if (resId < 0) {
			v.setVisibility(View.GONE);
		} else {
			v.setVisibility(View.VISIBLE);
			v.setButtonDrawable(resId);
		}
	}
}