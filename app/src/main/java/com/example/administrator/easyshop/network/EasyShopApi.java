package com.example.administrator.easyshop.network;

/**
 * Created by ${ljy} on 2016/11/17.
 */

public class EasyShopApi {
    //服务器地址
    static final String BASE_URL = "http://wx.feicuiedu.com:9094/yitao/";
    //图片的根路径
    public static final String IMAGE_URL = "http://wx.feicuiedu.com:9094/";

    //注册接口
    public static final String REGISTER = "UserWeb?method=register";

    //登录接口
    public static final String LOGIN = "UserWeb?method=login";

    //更新接口（更新昵称，更新头像）
    static final String UPDATA = "UserWeb?method=update";

    //获取商品
    static final String GETGOODS = "GoodsServlet?method=getAll";
}
