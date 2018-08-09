package com.gongyujia.app.api;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 接口管理类
 */

public interface ApiService {
    
    //    String BASE_URL = "http://test.gongyujia.com/";//测试环境
    String BASE_URL = "http://api.gongyujia.com/";//正式环境
    
    //用户登录
    @POST("home/user/userlogin")
    @FormUrlEncoded
    Observable<HttpResult> login(@Field("mobile") String mobile, @Field("password") String password);
    
    //发送验证码
    @POST("home/user/sendregcode")
    @FormUrlEncoded
    Observable<HttpResult> sendCode(@Field("mobile") String mobile);
    
    //注册用户
    @POST("home/user/useradd")
    @FormUrlEncoded
    Observable<HttpResult> register(@Field("mobile") String mobile, @Field("password") String password, @Field("check_code") String check_code, @Field("nick_name") String user_nickname);
    
    //忘记密码
    @POST("home/user/forgetpwd")
    @FormUrlEncoded
    Observable<HttpResult> forgetpwd(@Field("mobile") String mobile, @Field("password") String password, @Field("check_code") String checkCode);
    
    //app配置接口
    @POST("home/index/config")
    @FormUrlEncoded
    Observable<HttpResult> getConfig(@Field("channel_id") String channel_id);
    
    //首页租房接口
    @POST("home/house/searchlist")
    @FormUrlEncoded
    Observable<HttpResult> searchlist(
            @Field("keywords") String keywords,
            @Field("distritct") int distritct,
            @Field("rent_date") int rent_date,
            @Field("layout_type") int layout_type,
            @Field("price") int price,
            @Field("p") int p,
            @Field("page_size") int page_size
    
    );
    
    // 房源详情
    @POST("home/house/detail200")
    @FormUrlEncoded
    Observable<HttpResult> getHouseDetail(@Field("house_id") int house_id);
    
    //在线预约
    @POST("home/house/reserve_house")
    @FormUrlEncoded
    Observable<HttpResult> houseAppoint(@Field("house_id") int houseId, @Field("real_name") String realName, @Field("mobile") String phone, @Field("select_date") String selectDate, @Field("time_type") int timeType);
    
    //投诉建议
    @POST("home/index/suggest")
    @FormUrlEncoded
    Observable<HttpResult> suggest(@Field("mobile") String mobile, @Field("content") String content);
    
    //房源举报
    @POST("home/house/report")
    @FormUrlEncoded
    Observable<HttpResult> houseReport(@Field("house_id") int house_id, @Field("reason") String reason, @Field("msg") String msg);
    
    // 添加收藏
    @POST("home/user/collectAdd")
    @FormUrlEncoded
    Observable<HttpResult> addCollect(@Field("type") int type, @Field("type_id") int type_id);
    
    // 取消收藏
    @POST("home/user/collectCancel")
    @FormUrlEncoded
    Observable<HttpResult> cancleCollect(@Field("type_id") int type_id);
    
    //我的收藏收藏的类型1 房子2 服务3 精选
    @POST("home/user/collect")
    @FormUrlEncoded
    Observable<HttpResult> getCollects(@Field("type") int type);
    
    //用户资料修改提交
    @POST("home/user/infoSubmit")
    @FormUrlEncoded
    Observable<HttpResult> userInfoSubmit(@Field("user_nickname") String user_name, @Field("sex") int sex);
    
    // 房租列表
    @POST("home/user/addressList")
    @FormUrlEncoded
    Observable<HttpResult> getAddressList(@Field("address_type") int address_type);
    
    // 房源详情界面
    @POST("home/user/rentList")
    @FormUrlEncoded
    Observable<HttpResult> getHousingDetails(@Field("address_id") int address_id);

    //房租支付接口
    @POST("home/user/rentPay")
    @FormUrlEncoded
    Observable<HttpResult> getRentPay(@Field("account_sn") String account_sn);

    //房租支付提交接口
    @POST("home/user/rentPaySubmit")
    @FormUrlEncoded
    Observable<HttpResult> rentPaySubmit(@Field("account_sn") String account_sn, @Field("pay_money") double pay_money, @Field("payment_id") String payment_id);
}
