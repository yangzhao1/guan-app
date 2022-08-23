package com.submeter.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.submeter.android.tools.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

public class GridAdapter extends BaseAdapter {
	static String Tag = GridAdapter.class.getSimpleName();

	private int mResource;
	private int[] mTo;

    private int itemWidth = 0;
    private int itemHeight = 0;

	private String[] mFrom;


	private Context mContext;

	private LayoutInflater mLayoutInflater;

	public ArrayList<HashMap<String, Object>> mData;

	public GridAdapter(Context context, ArrayList<HashMap<String, Object>> data,
					   int resource, String[] from, int[] to) {
		mContext = context;
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
		mLayoutInflater = LayoutInflater.from(context);
	}

    public void setItemWidthAndHeight(int width, int height) {
        itemWidth = width;
        itemHeight = height;
    }

	protected void loadImage(Uri uri, SimpleDraweeView imageView) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(itemWidth, itemWidth))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.getController())
                .setImageRequest(request)
                .build();
        imageView.setController(controller);
        imageView.setTag(uri);
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
				view = inflaterView(parent);
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

	private View inflaterView(ViewGroup parent) {
		//parent mustn't be null, otherwise view will hasn't parent, so view.getLayoutParams will return null
		View view = Utils.inflateView(mContext, mLayoutInflater, mResource, parent, false);
		if(itemWidth > 0) {
			view.getLayoutParams().width = itemWidth;
		}

		if(itemHeight > 0) {
			view.getLayoutParams().height = itemHeight;
		}

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
				setViewText((TextView) v, data == null ? null : data.toString());
			} else if (v instanceof SimpleDraweeView) {
                if(null == data) {
                    v.setVisibility(View.GONE);
                } else {
                    setViewImage((SimpleDraweeView) v, Uri.parse(data.toString()));
                }
			} else if (v instanceof ImageView) {
                if(null == data) {
                    v.setVisibility(View.GONE);
                } else if(data instanceof Integer) {
                    setViewImage((ImageView) v, (Integer) data);
                }
            } else if (v instanceof ViewGroup) {
				v.setBackgroundResource((Integer) data);
			} else if (v instanceof ProgressBar) {
				((ProgressBar) v).setProgress((Integer) data);
			}
		}
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

	public void setViewText(TextView v, CharSequence text) {
		do {
			v.setText("");
			if (null == text) {
				v.setVisibility(View.GONE);
				break;
			}

			if (TextUtils.isEmpty(text)) {
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
}