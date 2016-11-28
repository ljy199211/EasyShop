package com.example.administrator.easyshop.person;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.administrator.easyshop.R;
import com.example.administrator.easyshop.commons.ActivityUtils;
import com.example.administrator.easyshop.commons.RegexUtils;
import com.example.administrator.easyshop.model.CachePreferences;
import com.example.administrator.easyshop.model.User;
import com.example.administrator.easyshop.model.UserResult;
import com.example.administrator.easyshop.network.EasyShopClient;
import com.example.administrator.easyshop.network.UICallBack;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class NickNameActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    private ActivityUtils activityUtils;
    private Unbinder unbinder;
    private String str_nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        unbinder = ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityUtils = new ActivityUtils(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_save)
    public void onClick() {
        str_nickName = etNickname.getText().toString();
        //昵称不符合规则
        if (RegexUtils.verifyNickname(str_nickName) != RegexUtils.VERIFY_SUCCESS) {
            String msg = getString(R.string.nickname_rules);
            activityUtils.showToast(msg);
            return;
        }
        init();
    }

    private void init() {
        User user = CachePreferences.getUser();
        user.setNick_name(str_nickName);
        Call call = EasyShopClient.getInstance().uploadUser(user);
        activityUtils.showToast(call+"");
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                activityUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                UserResult userResult = new Gson().fromJson(body, UserResult.class);
                if (userResult.getCode() != 1) {
                    activityUtils.showToast(userResult.getMessage());
                    return;
                }
                CachePreferences.setUser(userResult.getData());
                activityUtils.showToast("修改成功");
                //返回
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
