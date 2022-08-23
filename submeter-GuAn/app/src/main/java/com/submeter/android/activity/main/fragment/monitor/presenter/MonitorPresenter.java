package com.submeter.android.activity.main.fragment.monitor.presenter;

import com.submeter.android.activity.main.fragment.monitor.model.IMonitorModel;
import com.submeter.android.activity.main.fragment.monitor.view.IMonitorView;

/**
 * Created by 赵勃 on 2018/12/1.
 */

public class MonitorPresenter implements IMonitorPresenter {
    private IMonitorView categoryView;

    private IMonitorModel categoryModel;

    public MonitorPresenter(IMonitorView categoryView) {
        this.categoryView = categoryView;
    }

    @Override
    public void loadData() {
        //categoryView.updateView();
    }
}
