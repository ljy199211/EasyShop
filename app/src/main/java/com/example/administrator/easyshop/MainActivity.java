package com.example.administrator.easyshop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.easyshop.commons.ActivityUtils;
import com.example.administrator.easyshop.fragment.MeFragment;
import com.example.administrator.easyshop.fragment.ShopFragment;
import com.example.administrator.easyshop.fragment.UnLoginFragment;
import com.example.administrator.easyshop.model.CachePreferences;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_title_tv)
    TextView mainTitleTv;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_vp)
    ViewPager mainVp;
    @BindViews({R.id.tv_shopping,R.id.tv_message,R.id.tv_mail_list,R.id.tv_me})
    TextView[] textViews;

    @BindView(R.id.ll_tv)
    LinearLayout llTv;

    //点击2次返回，退出程序
    private boolean isExit = false;
    private ActivityUtils activityUtils;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("");
        init();

    }

    private void init() {
        //刚进来默认市场
        textViews[0].setSelected(true);
        if (CachePreferences.getUser().getName() == null){
            mainVp.setAdapter(unLoginAdapter);
            mainVp.setCurrentItem(0);
        }else{
            mainVp.setAdapter(loginAdapter);
            mainVp.setCurrentItem(0);
        }
        mainVp.addOnPageChangeListener(pageChangeListener);
    }
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (TextView textView:textViews
                 ) {
                textView.setSelected(false);
            }
            mainTitleTv.setText(textViews[position].getText());
            textViews[position].setSelected(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private FragmentStatePagerAdapter unLoginAdapter = new
            FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position){
                        //市场
                        case 0:
                            return new ShopFragment();
                        //消息
                        case 1:
                            return new UnLoginFragment();
                        //通讯录
                        case 2:
                            return new UnLoginFragment();
                        //我的
                        case 3:
                            return new MeFragment();
                    }
                    return null;
                }
                @Override
                public int getCount() {
                    return 4;
                }
            };
    private FragmentStatePagerAdapter loginAdapter = new
            FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position){
                        //市场
                        case 0:

                            return new ShopFragment();
                        //消息

                        case 1:
                            //  环信消息fragment
                            return new UnLoginFragment();
                        //通讯录
                        case 2:
                            //  环信通讯录fragment
                            return new UnLoginFragment();
                        //我的
                        case 3:
                            return new MeFragment();
                    }
                    return null;
                }
                @Override
                public int getCount() {
                    return 4;
                }
            };
    //textview点击事件
    @OnClick({R.id.tv_shopping,R.id.tv_message,R.id.tv_mail_list,R.id.tv_me})
    public void onClick(TextView view){
        for(int i = 0;i<textViews.length;i++){
            textViews[i].setSelected(false);
            textViews[i].setTag(i);
        }
        //设置选择效果
        view.setSelected(true);
        //参数false代表瞬间切换，而不是平滑过渡
        mainVp.setCurrentItem((Integer) view.getTag(),false);
        //设置一下toolbar的title
        mainTitleTv.setText(textViews[(Integer) view.getTag()].getText());
    }

    //点击2次返回，退出程序
    @Override
    public void onBackPressed() {
        if (!isExit){
            isExit = true;
            activityUtils.showToast("再摁一次退出程序");
            //两秒内再次点击返回则退出
            mainVp.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            },2000);
        }else {
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
