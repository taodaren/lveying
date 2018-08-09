package com.gongyujia.app.api;


import com.alibaba.fastjson.JSON;

import rx.functions.Func1;

/**
 * Created by jt on 2016/10/26.
 */
public class ApiResult<T> implements Func1<HttpResult, T> {

    private Class<T> clazz;
    private boolean isMsg = false;

    public ApiResult(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ApiResult(Class<T> clazz, boolean isMsg) {
        this.clazz = clazz;
        this.isMsg = isMsg;
    }

    @Override
    public T call(HttpResult httpResult) {
        if (httpResult.getCode() == 200) {
            if (clazz == String.class) {
                if(isMsg){
                    return (T) httpResult.getMsg();
                }
                return (T) httpResult.getData();
            } else {
                return JSON.parseObject(httpResult.getData(), clazz);
            }
        } else {
            throw new ApiException(httpResult.getMsg());
        }
    }

}
