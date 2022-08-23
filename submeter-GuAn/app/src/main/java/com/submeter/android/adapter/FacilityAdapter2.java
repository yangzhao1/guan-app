package com.submeter.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.entity.FacilityListBean;

import java.util.List;

public class FacilityAdapter2 extends RecyclerView.Adapter<FacilityAdapter2.ViewHolder> {
	static String Tag = FacilityAdapter2.class.getSimpleName();

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private LayoutInflater mLayoutInflater;

    public List mData;

    public FacilityAdapter2(Context context, List data) {
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
        view = mLayoutInflater.inflate(R.layout.item_facility_2, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FacilityListBean.EnterpriseDtosBean.EleHistryEntitiesBean dataSet = (FacilityListBean.EnterpriseDtosBean.EleHistryEntitiesBean) mData.get(position);
        if (dataSet == null) {
            return;
        }

        setViewText(holder.power,"设备功率: "+dataSet.getPower());
        setViewText(holder.time,"使用时间: "+dataSet.getCreateTime());

//        holder.itemRootView.setOnClickListener(v -> {
//            itemClickListener.setOnClickItemView(holder.itemRootView,position);
//        });
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
        public TextView power;
        public TextView time;
        public ViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;
            power = (TextView) itemView.findViewById(R.id.power);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}