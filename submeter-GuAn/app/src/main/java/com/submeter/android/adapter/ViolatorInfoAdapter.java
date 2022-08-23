package com.submeter.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.entity.Violator;

import java.util.List;

public class ViolatorInfoAdapter extends RecyclerView.Adapter<ViolatorInfoAdapter.ViewHolder> {
	static String Tag = ViolatorInfoAdapter.class.getSimpleName();

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private OnTextClickListener textClickListener;

    private LayoutInflater mLayoutInflater;

    public List<Violator> mData;

    public ViolatorInfoAdapter(Context context, List<Violator> data) {
        mData = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void updateItemsData(List<Violator> list){
        this.mData = list;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = mLayoutInflater.inflate(R.layout.item_vio, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    public void setTextClickListener(OnTextClickListener textClickListener) {
        this.textClickListener = textClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Violator dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        holder.number.setText((position+1)+"");
        holder.time.setText(dataSet.getVioTime());
        if (dataSet.getStatus().equals("0")){
            holder.state.setText("违规预警");
        }else if (dataSet.getStatus().equals("1")){
            holder.state.setText("违规");
        }else if (dataSet.getStatus().equals("2")){
            holder.state.setText("未违规");
        }else {
            holder.state.setText("已处理");
        }

        String type = dataSet.getType();
        if (type!=null){
            if (type.equals("1")){
                holder.content.setText("三天没有电力数据");
            }else if (type.equals("2")){
                holder.content.setText("设备十五分钟不在线");
            }else if (type.equals("3")){
                holder.content.setText("十五分钟超过总功");
            }else if (type.equals("4")){
                holder.content.setText("治污表未打开");
            }else if (type.equals("5")){
                holder.content.setText("未响应停产");
            }else{
                holder.content.setText("未响应停产");
            }
        }else {
            holder.content.setText("-");
        }

        holder.itemRootView.setTag(position);
        holder.itemRootView.setOnClickListener(v -> {
            itemClickListener.setOnClickItemView(holder.itemRootView,position);
        });

        holder.state.setOnClickListener(v -> {
            textClickListener.setOnClickItemView(holder.state,position);
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

        public TextView number;
        public TextView time;
        public TextView content;
        public TextView state;
        public ViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;
            number = (TextView) itemView.findViewById(R.id.number);
            time = (TextView) itemView.findViewById(R.id.time);
            content = (TextView) itemView.findViewById(R.id.content);
            state = (TextView) itemView.findViewById(R.id.state);
        }
    }
}