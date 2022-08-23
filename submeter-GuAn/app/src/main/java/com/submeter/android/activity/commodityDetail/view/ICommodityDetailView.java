package com.submeter.android.activity.commodityDetail.view;

import com.submeter.android.entity.Commodity;
import com.submeter.android.interfacce.IBaseView;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface ICommodityDetailView extends IBaseView {

    public void updateView(Commodity commodity);

    public void updateFollowStatus(boolean following);
}
