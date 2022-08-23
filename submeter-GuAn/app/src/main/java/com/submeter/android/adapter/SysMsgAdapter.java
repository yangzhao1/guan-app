package com.submeter.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.entity.Notice;

import java.util.List;

public class SysMsgAdapter extends RecyclerView.Adapter<SysMsgAdapter.ViewHolder> {
	static String Tag = SysMsgAdapter.class.getSimpleName();

    public static final int UNTREATED_VIEW = 0;
    public static final int PROCESSED_VIEW = 1;

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private LayoutInflater mLayoutInflater;

    public List mData;

    public SysMsgAdapter(Context context, List data) {
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
        view = mLayoutInflater.inflate(R.layout.item_msg, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notice dataSet = (Notice) mData.get(position);
        if (dataSet == null) {
            return;
        }

        if (!dataSet.getCreateTime().isEmpty()){
            setViewText(holder.timeText,dataSet.getCreateTime().split(" ")[0]);
        }else {
            setViewText(holder.timeText,"");
        }
        setViewText(holder.titleText,dataSet.getTitle());
        Spanned content = Html.fromHtml(dataSet.getContent()+"");
        setViewText(holder.contentText,content);

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
        public TextView timeText;
        public TextView titleText;
        public TextView contentText;
        public ViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;
            timeText = (TextView) itemView.findViewById(R.id.timeText);
            titleText = (TextView) itemView.findViewById(R.id.titleText);
            contentText = (TextView) itemView.findViewById(R.id.contentText);
        }
    }
}