package com.example.administrator.easyshop.shop;

import com.example.administrator.easyshop.model.GoodsResult;
import com.example.administrator.easyshop.network.EasyShopClient;
import com.example.administrator.easyshop.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by ${ljy} on 2016/11/26.
 */

public class ShopPresenter extends MvpNullObjectBasePresenter<ShopView> {
    private Call call;
    private int pageInt = 1;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call !=null){
            call.cancel();
        }
    }
    //刷新数据
    public void refreshData(String type){
        getView().showRefresh();
        call = EasyShopClient.getInstance().getGoods(1, type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body,GoodsResult.class);
                switch (goodsResult.getCode()){
                    case 1:
                        if (goodsResult.getData().size() == 0){
                            getView().addRefreshData(goodsResult.getData());
                            getView().showRefreshEnd();
                        }else{
                            getView().addRefreshData(goodsResult.getData());
                            getView().showRefreshEnd();
                        }
                        pageInt = 2;
                        default:
                            getView().showRefreshError(goodsResult.getMessage());
                }
            }
        });
    }
    //加载数据
    public void loadData(String type){
        getView().showLoadMoreLoading();
        call = EasyShopClient.getInstance().getGoods(pageInt, type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body,GoodsResult.class);
                switch (goodsResult.getCode()){
                    case 1:
                        if (goodsResult.getData().size() == 0){
                            getView().showLoadMoreEnd();
                        }else {
                            getView().addMoreData(goodsResult.getData());
                            getView().hideLoadMore();
                        }
                        pageInt++;
                        break;
                    default:
                        getView().showLoadMoreError(goodsResult.getMessage());
                }
            }
        });
    }
}
