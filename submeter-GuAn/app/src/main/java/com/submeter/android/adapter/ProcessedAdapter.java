package com.submeter.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.entity.GetOutUn;

import java.util.List;

public class ProcessedAdapter extends RecyclerView.Adapter<ProcessedAdapter.ViewHolder> {
	static String Tag = ProcessedAdapter.class.getSimpleName();

    public static final int UNTREATED_VIEW = 0;

    public static final int PROCESSED_VIEW = 1;

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private LayoutInflater mLayoutInflater;

    public List<GetOutUn> mData;

    private int type = UNTREATED_VIEW;

    public ProcessedAdapter(Context context, List<GetOutUn> data,int viewType) {
        mData = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.type = viewType;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = mLayoutInflater.inflate(R.layout.item_processed, parent,false);
        ViewHolder holder = new ViewHolder(view, type);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetOutUn dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        Object valueObj;
        holder.itemRootView.setTag(position);
        valueObj = dataSet.getPerson();
        setViewText(holder.name,"联系人: " +  valueObj);
        setViewText(holder.title,dataSet.getEnname());
        setViewText(holder.phone,"联系电话: " + dataSet.getPhone());
        String sumValue = dataSet.getSumvalue();
        if (sumValue!=null&&!sumValue.equals("0")){
            setViewText(holder.titleType,"生产企业");
            holder.titleType.setBackgroundResource(R.drawable.blue_corner_10_bg2);
        }else {
            setViewText(holder.titleType,"停产企业");
            holder.titleType.setBackgroundResource(R.drawable.red_corner_10_bg2);
        }
        if (type==1){
            setViewText(holder.processed,"已经处理");
        }else {
            setViewText(holder.processed,"违规详情");
        }
        holder.itemRootView.setOnClickListener(v -> {
            itemClickListener.setOnClickItemView(v,position);
        });

        holder.navigation.setOnClickListener(v -> {
            navigationOnClickListener.setNavigationOnClick(v,position);
        });
    }

    public interface NavigationOnClickListener{
        void setNavigationOnClick(View v,int pos);
    }

    private NavigationOnClickListener navigationOnClickListener;

    public void setNavigationOnClickListener(NavigationOnClickListener navigationOnClickListener) {
        this.navigationOnClickListener = navigationOnClickListener;
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
        public TextView name;
        public TextView phone;
        public TextView processed;
        public TextView navigation;
        public TextView titleType;

        public LinearLayout processedLin;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            itemRootView = itemView;

            title = (TextView) itemView.findViewById(R.id.title);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            processed = (TextView) itemView.findViewById(R.id.processed);
            processedLin = (LinearLayout) itemView.findViewById(R.id.processedLin);
            navigation = (TextView) itemView.findViewById(R.id.navigation);
            titleType = (TextView) itemView.findViewById(R.id.titleType);

        }
    }
}