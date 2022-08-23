package com.submeter.android.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeAdapter extends BaseAdapter {
	static String Tag = HomeAdapter.class.getSimpleName();

    public static final int GRID_VIEW = 0;

    public static final int LABEL_VIEW = 1;

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    protected ArrayList<HashMap<String, Object>> mData;

//    private HomeFragment.HomeItemClickListener itemClickListener;

    public HomeAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        mData = data;
        mContext = context;
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
        if(viewType == GRID_VIEW) {
            view = mLayoutInflater.inflate(R.layout.item_home_grid, null);
            viewHolder.gridView = (NoScrollGridView) view.findViewById(R.id.grid_view);
            viewHolder.gridView.setOnItemClickListener(gridItemClickListener);
            view.setTag(viewHolder);
        } else if(viewType == LABEL_VIEW) {
            view = mLayoutInflater.inflate(R.layout.item_home_label, null);
            viewHolder.labelLayout = view.findViewById(R.id.label_layout);
            viewHolder.labelName = (TextView)view.findViewById(R.id.label_view);
            viewHolder.moreView = (TextView) view.findViewById(R.id.more_view);
            view.setTag(viewHolder);
            viewHolder.moreView.setOnClickListener(clickListener);
        }

        return view;
    }

    private void bindView(int position, ViewHolder viewHolder, int viewType) {
        final HashMap<String, Object> dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        Object valueObj;
        if(viewType == GRID_VIEW) {
            int column = Integer.parseInt(dataSet.get("Key_Column").toString());
            viewHolder.gridView.setNumColumns(column);
            viewHolder.gridView.setBackgroundColor(Integer.parseInt(dataSet.get("Key_Background").toString()));
            viewHolder.gridView.setHorizontalSpacing(Integer.parseInt(dataSet.get("Key_HSpace").toString()));
            viewHolder.gridView.setVerticalSpacing(Integer.parseInt(dataSet.get("Key_VSpace").toString()));

            HomeGridAdapter gridAdapter = (HomeGridAdapter)viewHolder.gridView.getAdapter();
            ArrayList<HashMap<String, Object>> gridData = (ArrayList<HashMap<String, Object>>)dataSet.get("Key_Grid");
            if(gridAdapter == null) {
                gridAdapter = new HomeGridAdapter(mContext, gridData);
                gridAdapter.setImageWidthAndHeight(Integer.parseInt(dataSet.get("Key_ImageWidth").toString()), Integer.parseInt(dataSet.get("Key_ImageHeight").toString()));
                viewHolder.gridView.setAdapter(gridAdapter);
            } else {
                gridAdapter.refreshData(gridData);
            }
        } else if(viewType == LABEL_VIEW) {
            viewHolder.labelLayout.setBackgroundColor(Integer.parseInt(dataSet.get("Key_Background").toString()));

            valueObj = dataSet.get("Key_Label");
            setViewText(viewHolder.labelName, valueObj);

            valueObj = dataSet.get("Key_More");
            setViewText(viewHolder.moreView, valueObj);
            viewHolder.moreView.setTag(dataSet.get("Key_Category"));
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
        //for label layout
        View labelLayout;

        //for label's name
        TextView labelName;

        //for label's more
        TextView moreView;

        //for grid
        NoScrollGridView gridView;
    }

//    public void setItemClickListener(HomeFragment.HomeItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if(itemClickListener == null) {
//                return;
//            }
//
//            Object tagObj = view.getTag();
//            if(tagObj != null && RECOMMEND_BRAND_ITEM_VIEW == Integer.parseInt(tagObj.toString())) {
//                itemClickListener.onItemClick(RECOMMEND_BRAND_ITEM_VIEW, Integer.MAX_VALUE);
//            }
        }
    };

    private AdapterView.OnItemClickListener gridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            HomeGridAdapter homeGridAdapter = (HomeGridAdapter) adapterView.getAdapter();
            HashMap<String, Object> itemMap = homeGridAdapter.mData.get(i);
            Object typeObj = itemMap.get("Key_Type");
            Object idObj = itemMap.get("Key_Id");

//            if(itemClickListener != null) {
//                itemClickListener.onItemClick(Integer.parseInt(typeObj.toString()), Integer.parseInt(idObj.toString()));
//            }
        }
    };
}