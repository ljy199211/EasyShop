package com.example.administrator.easyshop.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.easyshop.R;
import com.example.administrator.easyshop.components.AvatarLoadOptions;
import com.example.administrator.easyshop.model.GoodsInfo;
import com.example.administrator.easyshop.network.EasyShopApi;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${ljy} on 2016/11/26.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private List<GoodsInfo> list = new ArrayList<>();
    private Context context;

    //添加数据
    public void addAllData(List<GoodsInfo> datas) {
        list.addAll(datas);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        ShopViewHolder viewHolder = new ShopViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, final int position) {
        holder.tvItemName.setText(list.get(position).getName());

        String price = context.getString(R.string.goods_money,list.get(position).getPrice());
        holder.tvItemPrice.setText(price);
        //图片加载
        ImageLoader.getInstance().displayImage(EasyShopApi.IMAGE_URL+list.get(position).getPage(),holder.ivItemRecycler, AvatarLoadOptions.build_item());

        holder.ivItemRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.clickPhotoClicked(list.get(position));
                }
            }
        });

    }
    private OnItemClickedListener listener;

    public interface OnItemClickedListener{
        void clickPhotoClicked(GoodsInfo goodsInfo);
    }

    public void setListener(OnItemClickedListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_recycler)
        ImageView ivItemRecycler;
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_item_price)
        TextView tvItemPrice;
        public ShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
