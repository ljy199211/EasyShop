package com.example.administrator.easyshop.person;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.easyshop.R;
import com.example.administrator.easyshop.model.ItemShow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${ljy} on 2016/11/24.
 */

public class PersonAdapter extends BaseAdapter {
    private List<ItemShow> list = new ArrayList<>();

    public PersonAdapter(List<ItemShow> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_info, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvItemName.setText(list.get(position).getItem_title());
        holder.tvPerson.setText(list.get(position).getItem_content());
        return convertView;
    }



    class ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_person)
        TextView tvPerson;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
