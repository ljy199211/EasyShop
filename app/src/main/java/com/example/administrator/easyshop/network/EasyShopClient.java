package com.example.administrator.easyshop.network;

import com.example.administrator.easyshop.model.CachePreferences;
import com.example.administrator.easyshop.model.User;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${ljy} on 2016/11/17.
 */

public class EasyShopClient {
    private static EasyShopClient easyShopClient;
    private OkHttpClient okHttpClient;
    private final Retrofit retrofit;
    private final Gson gson;

    private EasyShopClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        //Gson gson = new GsonBuilder().setLenient().create();
        gson = new Gson();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(EasyShopApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static EasyShopClient getInstance() {
        if (easyShopClient == null) {
            easyShopClient = new EasyShopClient();
        }
        return easyShopClient;
    }





    public Call register(String userName,String passWord){
        RequestBody body = new FormBody.Builder()
                .add("username",userName)
                .add("password",passWord)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL+EasyShopApi.REGISTER)
                .post(body)
                .build();
        return new OkHttpClient().newCall(request);
    }
    public Call login(String userName,String passWord){
        RequestBody loginBody = new FormBody.Builder()
                .add("username",userName)
                .add("password",passWord)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL+EasyShopApi.LOGIN)
                .post(loginBody)
                .build();
        return new OkHttpClient().newCall(request);
    }
    public Call uploadUser(User user){
        RequestBody uploadUserBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", gson.toJson(user))
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL+EasyShopApi.UPDATA)
                .post(uploadUserBody)
                .build();
        return okHttpClient.newCall(request);
    }

    public Call uploadAvatar(File file){
        //多部分形式构建请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", gson.toJson(CachePreferences.getUser()))
                .addFormDataPart("image",file.getName(),
                        RequestBody.create(MediaType.parse("image/png"),file))
                .build();

        //构建请求
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATA)
                .post(requestBody) //ctrl+p查看参数
                .build();

        return okHttpClient.newCall(request);
    }

    //获取商品
    //pageNo 商品分页,type商品类型
    public Call getGoods(int pageNo,String type){
        //多部分形式构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("pageNo",String.valueOf(pageNo))
                .add("type",type)
                .build();

        //构建请求
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.GETGOODS)
                .post(requestBody) //ctrl+p查看参数
                .build();

        return okHttpClient.newCall(request);
    }
}
