package com.example.administrator.easyshop.shop;

import com.example.administrator.easyshop.model.GoodsInfo;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by ${ljy} on 2016/11/26.
 */

public interface ShopView extends MvpView {
    void showRefresh();

    void showRefreshError(String msg);

    void showRefreshEnd();

    void hideRefresh();

    void showLoadMoreLoading();

    void showLoadMoreError(String msg);

    void showLoadMoreEnd();


    void hideLoadMore();

    void addMoreData(List<GoodsInfo> data);

    void addRefreshData(List<GoodsInfo> data);

    void showMessage(String msg);
}
