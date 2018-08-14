package net.lueying.s_image.net;

import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import net.lueying.s_image.constant.CommonConstant;
import net.lueying.s_image.core.App;
import net.lueying.s_image.entity.MessageEvent;
import net.lueying.s_image.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class WebScocketHelper {
    private static String SOCKET_URL = "ws://47.92.143.215:22222";
    private static String PROTOCOL = "222222";
    private static final AsyncHttpClient client = AsyncHttpClient.getDefaultInstance();
    private static WebScocketHelper wh = new WebScocketHelper();
    private Subscription subscribe_auto;
    private boolean isConnect = false;//当前连接状态
    private Handler handler;
    private Map<String, String> datamap;

    private WebScocketHelper() {

    }

    public static WebScocketHelper getInstance() {
        return wh;
    }

    public void sendMsg(Map<String, String> map) {
        this.datamap = map;
        handler = new Handler();
        //第一步先建立连接
        if (isConnect) {
            //向服务器发送命令
            senMessage(map, false);
        } else {
            openSocket();
        }


    }

    /**
     * app请求连接服务器
     */
    private void openSocket() {
        AsyncHttpClient.WebSocketConnectCallback callback = new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    LogUtil.e("socket连接失败" + ex.getMessage());
                    ex.printStackTrace();
                    return;
                }
                Map map = new HashMap<String, String>();
                map.put("Event", "CreateUserAndSocketMapping");
                map.put("Port", "App");
                map.put("Token", App.getApplication().getEncryptConfig(CommonConstant.TOKEN));
                String string = JSON.toJSONString(map);
                LogUtil.e("json:" + string);
                webSocket.send(string);
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
//                        //连接成功需要没30秒向服务器发送HeartLink包,超时需要重新建立连接
                        isConnect = true;//设置连接状态
                        LogUtil.i("连接服务器成功:" + s);
                        if (datamap != null && datamap.size() > 0) {
                            senMessage(datamap, false);
                        }
                        openTimer();
                    }

                });
            }

        };
        client.websocket(SOCKET_URL, PROTOCOL, callback);
    }

    /**
     * 开启一个轮询器发送HeartLink包
     */
    private void openTimer() {
        //延时 ，每间隔30秒(暂定)，时间单位
        subscribe_auto = Observable.interval(3000, 30000, TimeUnit.MILLISECONDS)
                //延时3000 ，每间隔3000，时间单位
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Map map = new HashMap<String, String>();
                        map.put("Event", "HeartLink");
                        isConnect = false;
                        senMessage(map, true);
                        try {
                            Thread.sleep(3000);
                            if (!isConnect) {
                                LogUtil.e("socket重连");
//                                subscribe_auto.unsubscribe();//关闭轮询器
                                datamap.clear();//清空数据,防止重连重复发送数据
                                openSocket();//重新建立连接
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 向服务器发送命令
     *
     * @param map
     * @param isHeart 是否是心跳包
     */
    private void senMessage(Map<String, String> map, boolean isHeart) {
        if (map != null && map.size() > 0) {
            AsyncHttpClient.WebSocketConnectCallback callback = new AsyncHttpClient.WebSocketConnectCallback() {
                @Override
                public void onCompleted(Exception ex, WebSocket webSocket) {
                    if (ex != null) {
                        ex.printStackTrace();
                        return;
                    }
                    String string = JSON.toJSONString(map);
                    LogUtil.e("json:" + string);
                    webSocket.send(string);
                    webSocket.setStringCallback(new WebSocket.StringCallback() {
                        @Override
                        public void onStringAvailable(String s) {
                            if (!TextUtils.isEmpty(s)) {
                                //发送命令成功
                                if (!isHeart) {
                                    //发送成功全局通知处理
                                    LogUtil.i("发送命令回执:" + s);
                                    JSONObject object = JSON.parseObject(s);
                                    LogUtil.i("obj:" + object.toString());
                                    EventBus.getDefault().post(new MessageEvent(object));
                                } else {
                                    //轮询消息收到回复修改连接状态为已连接
                                    LogUtil.i("收到服务器心跳包回执信息:" + s);
                                    isConnect = true;
                                }
                            }
                        }
                    });
                    webSocket.setEndCallback(new CompletedCallback() {
                        @Override
                        public void onCompleted(Exception ex) {
                            LogUtil.e("断开了" + ex.getMessage());
                        }
                    });
                }
            };
            client.websocket(SOCKET_URL, PROTOCOL, callback);
        }
    }

}
