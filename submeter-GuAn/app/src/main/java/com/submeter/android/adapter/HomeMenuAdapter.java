package com.submeter.android.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.R;
import com.submeter.android.entity.HomeMenu;
import com.submeter.android.network.NetworkRequestTool;

import java.util.List;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.ViewHolder> {
	static String Tag = HomeMenuAdapter.class.getSimpleName();

    public static final int UNTREATED_VIEW = 0;

    public static final int PROCESSED_VIEW = 1;

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private LayoutInflater mLayoutInflater;

    public List<HomeMenu> mData;

    public HomeMenuAdapter(Context context, List<HomeMenu> data) {
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
        view = mLayoutInflater.inflate(R.layout.item_home, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeMenu dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }
        String menuName =dataSet.getMenuName() ;
        holder.itemText.setText(menuName);
        Drawable drawable = mContext.getResources().getDrawable(dataSet.getImageUrl());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        holder.itemText.setCompoundDrawables(null,drawable,null,null);

//        holder.getOutInfo.setOnClickListener((view)->{
//            Intent intent = new Intent(mContext, CompanyGetOutInfoActivity.class);
//            intent.putExtra("id",dataSet.getEnId());
//            mContext.startActivity(intent);
//        });

        holder.itemRootView.setOnClickListener(v -> {
            itemClickListener.setOnClickItemView(v,position);
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

        public TextView itemText;

        public ViewHolder(View itemView) {
            super(itemView);
            itemRootView = itemView;

            itemText = (TextView) itemView.findViewById(R.id.item_text);
        }
    }
}