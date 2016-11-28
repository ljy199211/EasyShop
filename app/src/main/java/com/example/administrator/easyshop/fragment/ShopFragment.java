package com.example.administrator.easyshop.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.easyshop.R;
import com.example.administrator.easyshop.commons.ActivityUtils;
import com.example.administrator.easyshop.model.GoodsInfo;
import com.example.administrator.easyshop.shop.ShopAdapter;
import com.example.administrator.easyshop.shop.ShopPresenter;
import com.example.administrator.easyshop.shop.ShopView;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends MvpFragment<ShopView,ShopPresenter> implements ShopView {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout refreshLayout;
    @BindView(R.id.tv_load_error)
    TextView tvLoadError;
    private ActivityUtils activityUtils;
    private ShopAdapter adapter;

    private String pageType = "";

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        adapter = new ShopAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        adapter.setListener(new ShopAdapter.OnItemClickedListener() {
            @Override
            public void clickPhotoClicked(GoodsInfo goodsInfo) {
                //跳转到详情页面
            }
        });

        recyclerView.setAdapter(adapter);
        //初始化refreshLayout
        //使用本对象作为key，用来记录上一次刷新时间
        refreshLayout.setLastUpdateTimeRelateObject(this);
        refreshLayout.setBackgroundResource(R.color.recycler_bg);

        //关闭header所耗时间
        refreshLayout.setDurationToCloseHeader(1500);
        //加载，刷新回调
        refreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                //调用业务类
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                    presenter.refreshData(pageType);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter.getItemCount() == 0){
            refreshLayout.autoRefresh();
        }
    }
    @OnClick(R.id.tv_load_error)
    public void OnClick(){
        refreshLayout.autoRefresh();
    }

    @Override
    public ShopPresenter createPresenter() {
        return new ShopPresenter();
    }

    @Override
    public void showRefresh() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        refreshLayout.refreshComplete();//停止刷新
        if (adapter.getItemCount()>0){
            activityUtils.showToast(msg);
            return;
        }
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        activityUtils.showToast(getResources().getString(R.string.refresh_more_end));
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideRefresh() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void showLoadMoreLoading() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        refreshLayout.refreshComplete();
        if (adapter.getItemCount()>0){
            activityUtils.showToast(msg);
            return;
        }
        tvLoadError.setVisibility(View.VISIBLE);

    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast(getResources().getString(R.string.load_more_end));
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        adapter.addAllData(data);
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        adapter.clear();
        if (data!=null){
            adapter.addAllData(data);
        }
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
