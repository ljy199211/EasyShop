package com.example.administrator.easyshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.easyshop.R;
import com.example.administrator.easyshop.commons.ActivityUtils;
import com.example.administrator.easyshop.commons.LogUtils;
import com.example.administrator.easyshop.components.AvatarLoadOptions;
import com.example.administrator.easyshop.model.CachePreferences;
import com.example.administrator.easyshop.network.EasyShopApi;
import com.example.administrator.easyshop.person.PersonActivity;
import com.example.administrator.easyshop.user.LoginActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MeFragment extends Fragment {


    @BindView(R.id.iv_user_head)
    CircularImageView ivUserHead;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    private View view;
    private ActivityUtils activityUtils;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onStart() {
        LogUtils.d("onStart");
        super.onStart();
        //  判断用户是否登录，更改用户头像并且显示用户名
        if (CachePreferences.getUser().getName() == null){return;}
        if (CachePreferences.getUser().getNick_name() == null){
            tvLogin.setText("请输入昵称");
        }else{
            tvLogin.setText(CachePreferences.getUser().getNick_name());
        }
        ImageLoader.getInstance().displayImage(EasyShopApi.IMAGE_URL+CachePreferences.getUser().getHead_Image(),ivUserHead, AvatarLoadOptions.build());

    }
    @OnClick({R.id.iv_user_head, R.id.tv_person_info, R.id.tv_login, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onClick(View view) {
        // TODO: 2016/11/16 判断用户是否登录，来确定跳转位置
        activityUtils.showToast(CachePreferences.getUser().getName());
        if (CachePreferences.getUser().getName() == null){
            activityUtils.startActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()){
            case R.id.iv_user_head:
            case R.id.tv_login:
            case R.id.tv_person_info:
                activityUtils.startActivity(PersonActivity.class);
                break;
            case R.id.tv_person_goods:
                break;
            case  R.id.tv_goods_upload:
                break;
        }

    }

}
