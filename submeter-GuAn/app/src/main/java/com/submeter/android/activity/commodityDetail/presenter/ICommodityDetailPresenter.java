package com.submeter.android.activity.commodityDetail.presenter;

/**
 * Created by 赵勃 on 2018/11/20.
 */

public interface ICommodityDetailPresenter {

    public void loadCommodity(int id);

    public void goBack();

    public void gotoCS();

    public void gotoShop();

    public void follow();

    public void addShoppingcart();

    public void gotoBuy();

    public void call(String phone);

    public void webLoadFinish();

    public boolean handleHook(String url);
}
