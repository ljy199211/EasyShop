package com.example.administrator.easyshop.user.Login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by ${ljy} on 2016/11/19.
 */

public interface LoginView extends MvpView{
    void showPrb();

    void hidePrb();

    //注册失败
    void loginFailed();
    //注册成功
    void loginSuccess();

    void showMsg(String msg);


}
