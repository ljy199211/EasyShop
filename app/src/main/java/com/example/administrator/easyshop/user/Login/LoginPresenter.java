package com.example.administrator.easyshop.user.Login;

import com.example.administrator.easyshop.model.CachePreferences;
import com.example.administrator.easyshop.model.User;
import com.example.administrator.easyshop.model.UserResult;
import com.example.administrator.easyshop.network.EasyShopClient;
import com.example.administrator.easyshop.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by ${ljy} on 2016/11/19.
 */

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView>{
    private Call call;
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null){
            call.cancel();
        }
    }
    public void login(String userName,String passWord){
        getView().showPrb();
         call = EasyShopClient.getInstance().login(userName, passWord);
        call.enqueue(callback);
    }
    UICallBack callback = new UICallBack() {
        @Override
        public void onFailureUI(Call call, IOException e) {
            getView().hidePrb();
            getView().showMsg(e.getMessage());
        }

        @Override
        public void onResponseUI(Call call, String body) {
            UserResult userResult = new Gson().fromJson(body, UserResult.class);
            if (userResult.getCode() == 1) {
                getView().showMsg("登录成功");
                User user = userResult.getData();
                CachePreferences.setUser(user);

                getView().loginSuccess();

                // TODO: 2016/11/21 0021 环信未实现
            } else if (userResult.getCode() == 2) {
                getView().hidePrb();
                getView().showMsg(userResult.getMessage());
                getView().loginFailed();
            } else {
                getView().hidePrb();
                getView().showMsg("未知错误！");
            }

        }
    };


}
