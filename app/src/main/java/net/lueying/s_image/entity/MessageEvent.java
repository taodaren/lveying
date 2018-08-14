package net.lueying.s_image.entity;

import com.alibaba.fastjson.JSONObject;

public class MessageEvent {
    private JSONObject obj;

    public MessageEvent(JSONObject obj) {
        this.obj = obj;
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }
}
