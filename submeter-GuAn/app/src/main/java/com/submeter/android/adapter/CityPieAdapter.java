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

import java.util.List;
import java.util.Map;

public class CityPieAdapter extends RecyclerView.Adapter<CityPieAdapter.ViewHolder> {
	static String Tag = CityPieAdapter.class.getSimpleName();

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private LayoutInflater mLayoutInflater;

    public List<?> mData;

    public CityPieAdapter(Context context, List<?> data) {
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
        view = mLayoutInflater.inflate(R.layout.item_citypie, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map dataSet = (Map) mData.get(position);
        if (dataSet == null) {
            return;
        }

        holder.itemRootView.setTag(position);
        String name = (String) dataSet.get("name");
        holder.name.setText(name);
        int c = (int) dataSet.get("color");
        holder.color.setBackgroundColor(c);

//        holder.getOutInfo.setOnClickListener((view)->{
//            Intent intent = new Intent(mContext, CompanyGetOutInfoActivity.class);
//            intent.putExtra("id",dataSet.getEnId());
//            mContext.startActivity(intent);
//        });

        holder.itemRootView.setOnClickListener(v -> {
//            itemClickListener.setOnClickItemView(v,position);
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

        public TextView color;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;

            color = (TextView) itemView.findViewById(R.id.color);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}