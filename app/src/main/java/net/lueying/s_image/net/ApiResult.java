package net.lueying.s_image.net;


import com.alibaba.fastjson.JSON;

import rx.functions.Func1;

/**
 * 请求实体
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
        if (httpResult.getCode() == 1) {
            if (clazz == String.class) {
                if (isMsg) {
                    return (T) httpResult.getMessage();
                }
                return (T) httpResult.getData();
            } else {
                return JSON.parseObject(httpResult.getData(), clazz);
            }
        } else {
            throw new ApiException(httpResult.getCode(),httpResult.getMessage(), httpResult.getData());
        }
    }

}
