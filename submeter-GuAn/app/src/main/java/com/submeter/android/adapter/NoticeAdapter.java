package com.submeter.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.entity.Notice;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
	static String Tag = NoticeAdapter.class.getSimpleName();

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private LayoutInflater mLayoutInflater;

    public List<Notice> mData;

    public NoticeAdapter(Context context, List<Notice> data) {
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
        view = mLayoutInflater.inflate(R.layout.item_notice, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notice dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        holder.title.setText(dataSet.getTitle());
        holder.content.setText(dataSet.getContent());
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

        public TextView title;
        public TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}