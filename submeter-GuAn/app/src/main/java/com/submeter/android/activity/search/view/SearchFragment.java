package com.submeter.android.activity.search.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.search.model.ISearchModel;
import com.submeter.android.activity.search.presenter.ISearchPresenter;
import com.submeter.android.tools.Utils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchFragment extends Fragment implements ISearchFragment, TagFlowLayout.OnTagClickListener {

    private int searchType = -1;

    private ISearchPresenter searchPresenter;

    private View contentView;

    private BaseActivity parentActivity;

    private Unbinder unbinder;

    @BindView(R.id.delete_btn)
    View deleteView;

    @BindView(R.id.search_history)
    TagFlowLayout searchHistory;

    @BindView(R.id.empty_view)
    View emptyView;

    private LayoutInflater layoutInflater;

    private List<String> historyData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (BaseActivity) getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = Utils.inflateView(parentActivity, inflater, R.layout.activity_search_fragment, container, false);
            unbinder = ButterKnife.bind(this, contentView);
            initView();
        } else {
            ViewGroup p = (ViewGroup) contentView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
            unbinder = ButterKnife.bind(this, contentView);
        }

        return contentView;
    }

    private void initView() {
        layoutInflater = LayoutInflater.from(parentActivity);
        searchHistory.setOnTagClickListener(this);

        Bundle bundle = getArguments();
        searchType = bundle.getInt("Key_Type", ISearchModel.SEARCH_COMMODITY);
        searchPresenter.loadSearchHistory(searchType);
    }

    @OnClick({R.id.delete_btn})
    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.delete_btn: {
                searchPresenter.deleteHistory(searchType);
                break;
            }

            default:
                break;
        }
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void updateView(List<String> histroy) {
        if(histroy == null || histroy.isEmpty()) {
            deleteView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            searchHistory.setVisibility(View.GONE);
        } else {
            deleteView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            searchHistory.setVisibility(View.VISIBLE);

            historyData = histroy;
            searchHistory.setAdapter(new TagAdapter<String>(histroy) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) layoutInflater.inflate(R.layout.item_search_history, searchHistory, false);
                    tv.setText(s);
                    return tv;
                }
            });
        }
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        searchPresenter.search(searchType, historyData.get(position));
        return true;
    }

    @Override
    public void onDestroyView() {
        if(unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

    @Override
    public void setPresenter(ISearchPresenter searchPresenter) {
        this.searchPresenter = searchPresenter;
    }
}
