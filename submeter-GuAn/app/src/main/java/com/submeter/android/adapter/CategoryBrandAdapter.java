package com.submeter.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.R;
import com.submeter.android.network.NetworkRequestTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

public class CategoryBrandAdapter extends RecyclerView.Adapter<CategoryBrandAdapter.ViewHolder> {
	static String Tag = CategoryBrandAdapter.class.getSimpleName();

    public static final int GRID_VIEW = 0;

    public static final int LABEL_VIEW = 1;

    private Context mContext;

    private View.OnClickListener itemClickListener;

    private LayoutInflater mLayoutInflater;

    public ArrayList<HashMap<String, Object>> mData;

    public CategoryBrandAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        mData = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public int getItemViewType(int position) {
        if(null != mData && mData.size() > position) {
            return Integer.parseInt(mData.get(position).get("Key_Type").toString());
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == GRID_VIEW) {
            view = mLayoutInflater.inflate(R.layout.item_category_brand, null);
            view.setOnClickListener(itemClickListener);
        } else if(viewType == LABEL_VIEW) {
            view = mLayoutInflater.inflate(R.layout.item_category_good_label, null);
            view.setOnClickListener(itemClickListener);
        }

        ViewHolder holder = new ViewHolder(view, viewType);
        return holder;
    }

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        int viewType = Integer.parseInt(dataSet.get("Key_Type").toString());

        Object valueObj;
        if(viewType == GRID_VIEW) {
            holder.itemRootView.setTag(position);

            valueObj = dataSet.get("Key_Image");
            holder.imageView.getLayoutParams().width = Integer.parseInt(dataSet.get("Key_ImageWidth").toString());
            holder.imageView.getLayoutParams().height = Integer.parseInt(dataSet.get("Key_ImageHeight").toString());

            setImageView(holder.imageView, null == valueObj ? null : Uri.parse(valueObj.toString()));

            valueObj = dataSet.get("Key_Name");
            setViewText(holder.nameView, valueObj);
        } else if(viewType == LABEL_VIEW) {
            holder.itemRootView.setTag(position);

            valueObj = dataSet.get("Key_Label");
            setViewText(holder.nameView, valueObj);
        }
    }

    private void setImageView(SimpleDraweeView v, Uri value) {
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

    private void loadImage(Uri url, SimpleDraweeView imageView) {
        NetworkRequestTool.getInstance().loadImage(url, imageView, true);
        imageView.setTag(url);
    }

    public void setViewText(TextView v, Object text) {
        if(null == v) {
            return;
        }

        if (null == text) {
            v.setVisibility(View.GONE);
        } else if(text instanceof SpannableString) {
            v.setText((SpannableString)text);
            v.setVisibility(View.VISIBLE);
        } else if(text instanceof CharSequence || text instanceof String) {
            v.setText(text.toString());
            v.setVisibility(View.VISIBLE);
        }
    }

    public int getRealGridItemPosition(int position) {
        Object indexObj = mData.get(position).get("Key_Index");
        if(indexObj == null) {
            return -1;
        } else {
            return Integer.parseInt(indexObj.toString());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public View itemRootView;

        public SimpleDraweeView imageView;

        public TextView nameView;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            itemRootView = itemView;

            if(type == GRID_VIEW) {
                imageView = (SimpleDraweeView) itemView.findViewById(R.id.image_view);
                nameView = (TextView) itemView.findViewById(R.id.name_view);
            } else if(type == LABEL_VIEW) {
                nameView = (TextView) itemView.findViewById(R.id.label_view);
            }
        }
    }
}