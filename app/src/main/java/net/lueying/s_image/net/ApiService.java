package net.lueying.s_image.net;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 接口管理类
 */

public interface ApiService {

    String BASE_URL = "http://47.92.143.215/api/";//内网地址

    //注册
    @POST("register")
    @FormUrlEncoded
    Observable<HttpResult> register(@FieldMap Map<String, String> map);

    //发送短信
    @POST("sendMsg")
    @FormUrlEncoded
    Observable<HttpResult> sendMsg(@FieldMap Map<String, String> map);

    //登录
    @POST("login")
    @FormUrlEncoded
    Observable<HttpResult> login(@FieldMap Map<String, String> map);
}
