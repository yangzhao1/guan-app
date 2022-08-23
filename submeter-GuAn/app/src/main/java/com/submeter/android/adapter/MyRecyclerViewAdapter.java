package com.submeter.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.R;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.tools.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{
	static String Tag = MyRecyclerViewAdapter.class.getSimpleName();

	public static final int LOAD_MORE_TAG = 1000;

	public static final int END_TAG = 1001;

	protected int mResource;

	private int viewGapWidth;

	private int viewGapHeight;

	protected int[] mTo;

	private String[] mFrom;

	protected Context mContext;

	protected ArrayList<HashMap<String, Object>> mData;

	protected LayoutInflater layoutInflater;

	public MyRecyclerViewAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
		mContext = context;
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
		layoutInflater = LayoutInflater.from(context);

		setHasStableIds(true);
	}
	
	private void loadImage(Uri url, SimpleDraweeView imageView) {
		NetworkRequestTool.getInstance().loadImage(url, imageView, true);
        imageView.setTag(url);
	}

	@Override
	public int getItemViewType(int position) {
		if(position >= 0 && position < mData.size()) {
			Object typeObj = mData.get(position).get("Key_ViewType");
			if(null != typeObj) {
				return Integer.parseInt(typeObj.toString());
			}
		}
		return 0;
	}

	/*
	* create item view and fill viewholder
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View view = null;
		if(viewType == LOAD_MORE_TAG) {
			view = Utils.inflateView(mContext, layoutInflater, R.layout.item_loading, viewGroup, false);
		} else if(viewType == END_TAG) {
			view = Utils.inflateView(mContext, layoutInflater, R.layout.item_end, viewGroup, false);
		} else {
			view = Utils.inflateView(mContext, layoutInflater, mResource, viewGroup, false);
		}
		ViewHolder holder = new ViewHolder(view, mTo);
		return holder;
	}

	/*
	* bind view
	 */
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int index) {
		Map dataSet = mData.get(index);
		if (dataSet == null) {
			return;
		}

		String[] from = mFrom;
		int[] to = mTo;
		View v = null;
		HashMap<Integer, View> childViews = viewHolder.getChildViews();
		for (int i = 0; i < from.length && i < to.length; i++) {
			Object data = dataSet.get(from[i]);
			v = childViews.get(to[i]);
			if(null == v) {
				continue;
			}

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
				v.setBackgroundResource((Integer) data);
			} else if (v instanceof ProgressBar) {
				((ProgressBar) v).setProgress((Integer) data);
			}
		}
	}

	@Override
	public int getItemCount() {
		return mData == null ? 0 : mData.size();
	}

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    public void setViewImage(ImageView v, int value) {
        v.setTag(value);
        if (value != -1) {
            v.setImageResource(value);
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

	public void setViewImage(SimpleDraweeView v, Uri value) {
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

	public static class ViewHolder extends RecyclerView.ViewHolder{
		private HashMap<Integer, View> childViews = new HashMap<Integer, View>();

		public ViewHolder(View itemView, int[] viewIds) {
			super(itemView);

			if(null != viewIds && viewIds.length > 0) {
				for(int viewId : viewIds) {
					childViews.put(viewId, itemView.findViewById(viewId));
				}
			}
		}

		public HashMap<Integer, View> getChildViews() {
			return childViews;
		}

		public View getView(int id) {
			if(null != childViews && childViews.containsKey(id)) {
				return childViews.get(id);
			} else {
				return null;
			}
		}
	}

	public ArrayList<HashMap<String, Object>> getData() {
    	return mData;
	}

	public void setAdditionalViewGap(int gapWidth, int gapHeight) {
    	this.viewGapWidth = gapWidth;
    	this.viewGapHeight = gapHeight;
	}
}