package com.submeter.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.MarkerOptions;
import com.submeter.android.R;

import java.util.ArrayList;

public class PopCompanyListAdapter extends RecyclerView.Adapter<PopCompanyListAdapter.ViewHolder> {
	static String Tag = PopCompanyListAdapter.class.getSimpleName();

    public static final int UNTREATED_VIEW = 0;

    public static final int PROCESSED_VIEW = 1;

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private LayoutInflater mLayoutInflater;

    public ArrayList<MarkerOptions> mData;

    public PopCompanyListAdapter(Context context, ArrayList<MarkerOptions> data) {
        mData = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = mLayoutInflater.inflate(R.layout.item_text, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MarkerOptions dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        holder.textView.setText(dataSet.getTitle());
        holder.itemRootView.setTag(position);

        holder.itemRootView.setOnClickListener(v -> {
            itemClickListener.setOnClickItemView(holder.itemRootView,position);
        });
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

    class ViewHolder extends RecyclerView.ViewHolder {
        public View itemRootView;
        public TextView textView;
        public LinearLayout lin;
        public ViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;
            textView = (TextView) itemView.findViewById(R.id.item_text);
            lin = (LinearLayout) itemView.findViewById(R.id.lin);

            WindowManager manager1 = ((Activity)mContext).getWindowManager();
            DisplayMetrics metrics = new DisplayMetrics();
            manager1.getDefaultDisplay().getMetrics(metrics);
            int widthPixels = metrics.widthPixels;
            lin.setLayoutParams(new ViewGroup.LayoutParams(widthPixels,ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }
}