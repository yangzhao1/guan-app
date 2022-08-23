package com.submeter.android.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryContentAdapter extends BaseAdapter {
	static String Tag = CategoryContentAdapter.class.getSimpleName();

    public static final int GRID_VIEW = 0;

    public static final int LABEL_VIEW = 1;

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    public ArrayList<HashMap<String, Object>> mData;

    //private HomeGridAdapter

    public CategoryContentAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
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
            view = mLayoutInflater.inflate(R.layout.item_category_good_grid, null);
            viewHolder.gridView = (NoScrollGridView) view.findViewById(R.id.grid_view);
            view.setTag(viewHolder);
        } else if(viewType == LABEL_VIEW) {
            view = mLayoutInflater.inflate(R.layout.item_category_good_label, null);
            viewHolder.labelName = (TextView)view.findViewById(R.id.label_view);
            view.setTag(viewHolder);
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
            viewHolder.gridView.setHorizontalSpacing(Integer.parseInt(dataSet.get("Key_HSpace").toString()));
            viewHolder.gridView.setVerticalSpacing(Integer.parseInt(dataSet.get("Key_VSpace").toString()));

            CategoryGridAdapter gridAdapter = (CategoryGridAdapter) viewHolder.gridView.getAdapter();
            ArrayList<HashMap<String, Object>> gridData = (ArrayList<HashMap<String, Object>>)dataSet.get("Key_Grid");
            //if(gridAdapter == null) {
                gridAdapter = new CategoryGridAdapter(mContext, gridData);
                gridAdapter.setImageWidthAndHeight(Integer.parseInt(dataSet.get("Key_ImageWidth").toString()), Integer.parseInt(dataSet.get("Key_ImageHeight").toString()));
                viewHolder.gridView.setAdapter(gridAdapter);
            /*} else {
                gridAdapter.refreshData(gridData);
                viewHolder.gridView.invalidate();
            }*/
        } else if(viewType == LABEL_VIEW) {
            valueObj = dataSet.get("Key_Label");
            setViewText(viewHolder.labelName, valueObj);
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
        //for label's name
        TextView labelName;

        //for grid
        NoScrollGridView gridView;
    }
}