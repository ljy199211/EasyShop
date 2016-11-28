package com.example.administrator.easyshop.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.easyshop.MainActivity;
import com.example.administrator.easyshop.R;
import com.example.administrator.easyshop.commons.ActivityUtils;
import com.example.administrator.easyshop.commons.LogUtils;
import com.example.administrator.easyshop.components.ProgressDialogFragment;
import com.example.administrator.easyshop.model.CachePreferences;
import com.example.administrator.easyshop.user.Login.LoginPresenter;
import com.example.administrator.easyshop.user.Login.LoginView;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private Unbinder unbinder;

    private ActivityUtils activityUtils;
    private ProgressDialogFragment dialogFragment;
    private String userName;
    private String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        LogUtils.d("onCreate");
        activityUtils = new ActivityUtils(this);
        init();
    }

    private void init() {
        etUsername.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);

        if (dialogFragment == null) {
            dialogFragment = new ProgressDialogFragment();
        }
        setSupportActionBar(toolbar);
        //给左上角加一个返回图标,需要重写菜单点击事件，否则点击无效
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //给左上角加一个返回图标,需要重写菜单点击事件，否则点击无效

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        //这里的s表示改变之前的内容，通常start和count组合，可以在s中读取本次改变字段中被改变的内容。
        //而after表示改变后新的内容的数量。
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        //这里的s表示改变之后的内容，通常start和count组合，可以在s中读取本次改变字段中新的内容。
        //而before表示被改变的内容的数量。
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        //表示最终内容
        @Override
        public void afterTextChanged(Editable s) {
            userName = etUsername.getText().toString();
            passWord = etPwd.getText().toString();
            //判断用户名和密码是否为空
            boolean canLogin = !(TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord));
            btnLogin.setEnabled(canLogin);
        }


    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @OnClick({R.id.tv_register, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                activityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.btn_login:
                //Users users = new Users(userName, passWord);
                presenter.login(userName,passWord);
                break;
        }
    }


    @Override
    public void showPrb() {
        activityUtils.hideSoftKeyboard();
        if (dialogFragment == null) dialogFragment = new ProgressDialogFragment();
        if (dialogFragment.isVisible()) return;
        dialogFragment.show(getSupportFragmentManager(), "progress_dialog_fragment");
    }

    @Override
    public void hidePrb() {
        dialogFragment.dismiss();
    }

    @Override
    public void loginFailed() {
        etUsername.setText("");
    }

    @Override
    public void loginSuccess() {
        activityUtils.startActivity(MainActivity.class);
        activityUtils.showToast(CachePreferences.getUser().getName());
        finish();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }


}
