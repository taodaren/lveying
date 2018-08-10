package net.lueying.s_image.logic;

import net.lueying.s_image.entity.DeviceIndex;
import net.lueying.s_image.net.ApiClient;
import net.lueying.s_image.net.ApiResult;
import net.lueying.s_image.net.HttpResult;
import net.lueying.s_image.net.RxUtil;

import java.util.Map;

import rx.Observable;

/**
 * 设备部分
 */
public class DeviceLogic {

    //设备首页
    public static Observable<DeviceIndex> deviceIndex(Map map) {
        return ApiClient.getInstance().getApiService().deviceIndex(map)
                .map(new ApiResult<DeviceIndex>(DeviceIndex.class))
                .compose(RxUtil.<DeviceIndex>rxSchedulerHelper());
    }

}
