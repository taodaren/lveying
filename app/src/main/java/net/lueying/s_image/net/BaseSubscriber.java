package net.lueying.s_image.net;


import net.lueying.s_image.utils.NetworkUtil;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/15.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onStart() {
        if (!NetworkUtil.hasInternet()) {
            onFailed(new RuntimeException("网络连接失败，请检查网络设置！"));
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (!NetworkUtil.hasInternet()) {
            onFailed(new RuntimeException("网络连接失败，请检查网络设置！"));
        } else {
            onFailed(e);
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    public abstract void onFailed(Throwable e);

}
