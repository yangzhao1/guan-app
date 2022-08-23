package com.submeter.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.R;
import com.submeter.android.entity.Company;
import com.submeter.android.network.NetworkRequestTool;

import java.util.List;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder> {
	static String Tag = CompanyListAdapter.class.getSimpleName();

    public static final int UNTREATED_VIEW = 0;

    public static final int PROCESSED_VIEW = 1;

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private OnGoPhoneClickListener onPhoneClickListener;

    private LayoutInflater mLayoutInflater;

    public List<Company> mData;

    public CompanyListAdapter(Context context, List<Company> data) {
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
        view = mLayoutInflater.inflate(R.layout.item_companylist, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    public void setOnPhoneClickListener(OnGoPhoneClickListener clickListener){
        this.onPhoneClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Company dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        String person;
        String title =dataSet.getName() ;
        String phone = dataSet.getPhone();
        holder.itemRootView.setTag(position);
        person = dataSet.getPerson();
        if (person==null){
            person="暂无";
        }
        if (title==null){
            title="暂无";
        }
        if (phone==null){
            phone="暂无";
        }
        holder.name.setText("联系人: " +  person);
        holder.title.setText(title);
        holder.phone.setText("联系电话: " +  phone);
        String sumValue = dataSet.getSumvalue();
        if (sumValue!=null&&!sumValue.equals("0")){
            setViewText(holder.titleType,"生产企业");
            holder.titleType.setBackgroundResource(R.drawable.blue_corner_10_bg2);
        }else {
            setViewText(holder.titleType,"停产企业");
            holder.titleType.setBackgroundResource(R.drawable.red_corner_10_bg2);
        }
        String hour = dataSet.getLasthour();
        String yestoday = dataSet.getYestoday();
        String month = dataSet.getMonthAve();
        setViewText(holder.hourPow,hour==null?"0kw·h":hour+"kw·h");
        setViewText(holder.yestodayPow,yestoday==null?"0kw·h":yestoday+"kw·h");
        setViewText(holder.monthPow,month==null?"0kw·h":month+"kw·h");
//        holder.getOutInfo.setOnClickListener((view)->{
//            Intent intent = new Intent(mContext, CompanyGetOutInfoActivity.class);
//            intent.putExtra("id",dataSet.getEnId());
//            mContext.startActivity(intent);
//        });

        holder.itemRootView.setOnClickListener(v -> {
            itemClickListener.setOnClickItemView(v,position);
        });
        holder.goPhone.setOnClickListener(v -> {
            onPhoneClickListener.setOnPhoneClickItemView(v,position);
        });
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

    class ViewHolder extends RecyclerView.ViewHolder {
        public View itemRootView;

        public TextView title;
        public TextView name;
        public TextView phone;
        public ImageView goPhone;
        public TextView titleType;
        public TextView hourPow;
        public TextView yestodayPow;
        public TextView monthPow;

        public ViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;

            title = (TextView) itemView.findViewById(R.id.title);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            goPhone = (ImageView) itemView.findViewById(R.id.goPhone);
            titleType = (TextView) itemView.findViewById(R.id.titleType);
            hourPow = (TextView) itemView.findViewById(R.id.hourPow);
            yestodayPow = (TextView) itemView.findViewById(R.id.yestodayPow);
            monthPow = (TextView) itemView.findViewById(R.id.monthPow);
        }
    }
}