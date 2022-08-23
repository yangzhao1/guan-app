package com.submeter.android.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.entity.FacilityListBean;

import java.util.List;

public class FacilityAdapter1 extends RecyclerView.Adapter<FacilityAdapter1.ViewHolder> {
	static String Tag = FacilityAdapter1.class.getSimpleName();

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private LayoutInflater mLayoutInflater;

    public List mData;

    public FacilityAdapter1(Context context, List data) {
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
        view = mLayoutInflater.inflate(R.layout.item_facility_1, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FacilityListBean.EnterpriseDtosBean dataSet = (FacilityListBean.EnterpriseDtosBean) mData.get(position);
        if (dataSet == null) {
            return;
        }

        setViewText(holder.name,"设备名称: "+dataSet.getEquipmentName());
//        setViewText(holder.code,"设备编号: "+dataSet.getEquipmentid());
        LinearLayoutManager manager = new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        holder.recyclerView.setLayoutManager(manager);
        FacilityAdapter2 adapter2 = new FacilityAdapter2(mContext,dataSet.getEleHistryEntities());
        holder.recyclerView.setAdapter(adapter2);
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
        public TextView name;
        public TextView code;
        public RecyclerView recyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
            code = (TextView) itemView.findViewById(R.id.code);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler);
        }
    }
}