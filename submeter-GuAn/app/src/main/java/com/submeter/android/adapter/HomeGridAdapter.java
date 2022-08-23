package com.submeter.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.submeter.android.R;
import com.submeter.android.network.NetworkRequestTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeGridAdapter extends BaseAdapter {
	static String Tag = HomeGridAdapter.class.getSimpleName();

    public static final int COMMODITY_ITEM_VIEW = 0;

    public static final int RECOMMEND_BRAND_ITEM_VIEW = 1;

    private int viewGap = 15;

    private int imageWidth;

    private int imageHeight;

    private LayoutInflater mLayoutInflater;

    public ArrayList<HashMap<String, Object>> mData = new ArrayList<>();

    public HomeGridAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        mData.addAll(data);
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * @see android.widget.Adapter#getCount()
     */
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        if (position < 0 || mData == null || position > mData.size()) {
            return null;
        } else {
            return mData.get(position);
        }
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(null != mData && mData.size() > position) {
            return Integer.parseInt(mData.get(position).get("Key_Type").toString());
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        try {
            int viewType = getItemViewType(position);
            if (convertView == null) {
                v = inflaterView(viewType);
            } else {
                v = convertView;
            }
            bindView(position, (ViewHolder) v.getTag(), viewType);

            return v;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    private View inflaterView(int viewType) {
        View view = null;
        ViewHolder viewHolder = new ViewHolder();
        if(viewType == COMMODITY_ITEM_VIEW) {
            view = mLayoutInflater.inflate(R.layout.item_home_commodity, null);
            //view.findViewById(R.id.price_layout).getLayoutParams().width = imageWidth;

            viewHolder.imageView = (SimpleDraweeView) view.findViewById(R.id.image_view);
            viewHolder.imageView.getLayoutParams().width = imageWidth;
            viewHolder.imageView.getLayoutParams().height = imageHeight;

            viewHolder.nameView = (TextView)view.findViewById(R.id.name_view);
            viewHolder.promotionPriceView = (TextView)view.findViewById(R.id.price_view);
            viewHolder.contractPriceView = (TextView)view.findViewById(R.id.contract_price_view);
            viewHolder.tagView = (TextView)view.findViewById(R.id.tag_view);
            view.setTag(viewHolder);
        } else if(viewType == RECOMMEND_BRAND_ITEM_VIEW) {
            view = mLayoutInflater.inflate(R.layout.item_home_brand, null);
            viewHolder.imageView = (SimpleDraweeView)view.findViewById(R.id.image_view);
            viewHolder.imageView.getLayoutParams().width = imageWidth;
            viewHolder.imageView.getLayoutParams().height = imageHeight;
            view.setTag(viewHolder);
        }

        return view;
    }

    private void bindView(int position, ViewHolder viewHolder, int viewType) {
        final Map dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        Object valueObj;
        if(viewType == COMMODITY_ITEM_VIEW) {
            valueObj = dataSet.get("Key_Image");
            setImageView(viewHolder.imageView, null == valueObj ? null : valueObj.toString());

            valueObj = dataSet.get("Key_Name");
            setViewText(viewHolder.nameView, valueObj);

            valueObj = dataSet.get("Key_Price");
            setViewText(viewHolder.promotionPriceView, valueObj);

            valueObj = dataSet.get("Key_ContactPrice");
            setViewText(viewHolder.contractPriceView, valueObj);

            valueObj = dataSet.get("Key_Tag");
            setViewText(viewHolder.tagView, valueObj);
        } else if(viewType == RECOMMEND_BRAND_ITEM_VIEW) {
            valueObj = dataSet.get("Key_Image");
            setImageView(viewHolder.imageView, null == valueObj ? null : valueObj.toString());
        }
    }

    public void refreshData(ArrayList<HashMap<String, Object>> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    private void setImageView(SimpleDraweeView imageView, Object imageData) {
        if(null == imageView) {
            return;
        }

        if(null == imageData || "".equals(imageData.toString())) {
            imageView.setVisibility(View.GONE);
        } else {
            setViewImage(imageView, Uri.parse(imageData.toString()));
        }
    }

    public void setViewImage(SimpleDraweeView v, Uri value) {
        if (value == null) {
            v.setTag(value);
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
            NetworkRequestTool.getInstance().loadImage(value, v, true);
            v.setTag(value);
        }
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

    public class ViewHolder {
        //for activity, brand
        SimpleDraweeView imageView;

        //for activity's name,
        TextView nameView;

        //for activity item's promotion price
        TextView promotionPriceView;

        //for activity item's origin price
        TextView contractPriceView;

        //for activity item's tag view
        TextView tagView;
    }

    public void setImageWidthAndHeight(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
    }
}