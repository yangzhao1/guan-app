package com.submeter.android.activity.notice.view;

import com.submeter.android.entity.Notice;
import com.submeter.android.interfacce.IBaseView;

import java.util.List;

/**
 * Created by yangzhao on 2019/4/13.
 */

public interface INotiveView extends IBaseView{
    void updateView(List<Notice> noticeList);
}
