package com.example.administrator.easyshop.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.easyshop.MainActivity;
import com.example.administrator.easyshop.R;
import com.example.administrator.easyshop.commons.ActivityUtils;
import com.example.administrator.easyshop.commons.RegexUtils;
import com.example.administrator.easyshop.components.AlertDialogFragment;
import com.example.administrator.easyshop.components.ProgressDialogFragment;
import com.example.administrator.easyshop.user.register.RegisterPresenter;
import com.example.administrator.easyshop.user.register.RegisterView;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.administrator.easyshop.R.id.btn_register;
import static com.example.administrator.easyshop.R.id.et_pwd;
import static com.example.administrator.easyshop.R.id.et_pwdAgain;

public class RegisterActivity extends MvpActivity<RegisterView, RegisterPresenter> implements RegisterView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(et_pwd)
    EditText etPwd;
    @BindView(et_pwdAgain)
    EditText etPwdAgain;
    @BindView(btn_register)
    Button btnRegister;
    private Unbinder unbinder;

    private ActivityUtils activityUtils;
    private String userName;
    private String passWord;
    private String againPassWord;
    private ProgressDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        init();
    }

    private void init() {
        etUsername.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);
        etPwdAgain.addTextChangedListener(textWatcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            userName = etUsername.getText().toString();
            passWord = etPwd.getText().toString();
            againPassWord = etPwdAgain.getText().toString();
            boolean canRegister = !(TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord) || TextUtils.isEmpty(againPassWord));
            btnRegister.setEnabled(canRegister);
        }
    };

    @Override
    protected void onDestroy() {
        Log.d("RegisterActivity", "onDestroy: ");
        super.onDestroy();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @OnClick(btn_register)
    public void onClick() {
        if (RegexUtils.verifyUsername(userName) != RegexUtils.VERIFY_SUCCESS) {
            String msg = getString(R.string.username_rules);
            showUserPasswordError(msg);
            return;
        } else if (RegexUtils.verifyPassword(passWord) != RegexUtils.VERIFY_SUCCESS) {
            String msg = getString(R.string.password_rules);
            showUserPasswordError(msg);
            return;
        } else if (!TextUtils.equals(passWord, againPassWord)) {
            String msg = getString(R.string.username_equal_pwd);
            showUserPasswordError(msg);
            return;
        }
        /*Users users = new Users(userName,passWord);*/
        presenter.register(userName,passWord);

    }

    @Override
    public void showPrb() {
        activityUtils.hideSoftKeyboard();
        if (dialogFragment == null) {
            dialogFragment = new ProgressDialogFragment();
        }
        if (dialogFragment.isVisible()) {
            return;
        }
        dialogFragment.show(getSupportFragmentManager(), "progress_dialog_fragment");

    }

    @Override
    public void hidePrb() {
        dialogFragment.dismiss();
    }

    @Override
    public void registerFailed() {
        etUsername.setText("");
    }

    @Override
    public void registerSuccess() {
        //成功跳转到主页
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);

    }

    @Override
    public void showUserPasswordError(String msg) {
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(msg);
        fragment.show(getSupportFragmentManager(), getString(R.string.username_pwd_rule));
    }


}
