package com.submeter.android.activity.companyList.view;

import com.submeter.android.entity.AreaBean;
import com.submeter.android.entity.CompanyDataBean;
import com.submeter.android.interfacce.IBaseView;

import java.util.List;

/**
 * Created by yangzhao on 2019/4/13.
 */

public interface ICompanyView extends IBaseView{
    void updateView(CompanyDataBean bean);

    void updateAreaList(List<AreaBean.AreaBeanN> list);
}
