package net.lueying.s_image.logic;

import net.lueying.s_image.entity.Register;
import net.lueying.s_image.net.ApiClient;
import net.lueying.s_image.net.ApiResult;
import net.lueying.s_image.net.HttpResult;
import net.lueying.s_image.net.RxUtil;

import java.util.Map;

import rx.Observable;

public class UserLogic {
    //发送验证码(没有返回值)
    public static Observable<HttpResult> sendMsg(Map map) {
        return ApiClient.getInstance().getApiService().sendMsg(map)
                .map(new ApiResult<HttpResult>(HttpResult.class))
                .compose(RxUtil.<HttpResult>rxSchedulerHelper());
    }

    //用户注册
    public static Observable<Register> register(Map map) {
        return ApiClient.getInstance().getApiService().register(map)
                .map(new ApiResult<Register>(Register.class))
                .compose(RxUtil.<Register>rxSchedulerHelper());
    }
    //用户登录
    public static Observable<Register> login(Map map) {
        return ApiClient.getInstance().getApiService().login(map)
                .map(new ApiResult<Register>(Register.class))
                .compose(RxUtil.<Register>rxSchedulerHelper());
    }
}
