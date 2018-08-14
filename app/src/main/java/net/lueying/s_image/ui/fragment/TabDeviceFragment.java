package net.lueying.s_image.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import net.lueying.s_image.R;
import net.lueying.s_image.base.BaseFragment;
import net.lueying.s_image.constant.CommonConstant;
import net.lueying.s_image.core.App;
import net.lueying.s_image.entity.DeviceIndex;
import net.lueying.s_image.entity.MessageEvent;
import net.lueying.s_image.logic.DeviceLogic;
import net.lueying.s_image.net.BaseSubscriber;
import net.lueying.s_image.net.WebScocketHelper;
import net.lueying.s_image.ui.common.QuickMarkActivity;
import net.lueying.s_image.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备模块
 */

public class TabDeviceFragment extends BaseFragment {
    private static final int REQUESTCODE = 101;
    @BindView(R.id.ll_device)
    LinearLayout ll_device;
    @BindView(R.id.ll_group)
    LinearLayout ll_group;
    @BindView(R.id.iv_adddevice)
    ImageView iv_adddevice;

    public static TabDeviceFragment newInstance() {
        return new TabDeviceFragment();
    }

    public TabDeviceFragment() {
    }

    @Override
    protected int layoutViewId() {
        return R.layout.fragment_tab_device;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void initData() {
        super.initData();
        requestData();
    }

    private void requestData() {
        Map map = new HashMap();
        String token = App.getApplication().getEncryptConfig(CommonConstant.TOKEN);
        map.put("token", token);
        mCompositeSubscription.add(DeviceLogic.deviceIndex(map).subscribe(new BaseSubscriber<DeviceIndex>() {
            @Override
            public void onSuccess(DeviceIndex httpResult) {
                processData(httpResult);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.showShort(context, e.getMessage());
            }
        }));
    }

    private void processData(DeviceIndex result) {

        addDeviceList(result.getDev());
        addGroupList(result.getGrp());

    }

    /**
     * 添加设备群信息,固定3条,最后一个添加按钮
     *
     * @param grp
     */
    private void addGroupList(List<DeviceIndex.GrpBean> grp) {
        if (grp != null && grp.size() > 0) {
            for (int i = 0; i < 3; i++) {
                DeviceIndex.GrpBean grpBean = grp.get(i);
                View view = View.inflate(context, R.layout.item_group, null);
                ImageView ivGroup = view.findViewById(R.id.iv_group);
                ImageView ivIsusable = view.findViewById(R.id.iv_isusable);
                ImageView tvGroupname = view.findViewById(R.id.tv_groupname);
                ll_group.addView(view);
            }
        }

    }

    /**
     * 添加设备信息,固定2条
     *
     * @param dev
     */
    private void addDeviceList(List<DeviceIndex.DevBean> dev) {
        for (int i = 0; i < 2; i++) {
            View view = View.inflate(context, R.layout.item_device, null);
            ll_device.addView(view);
        }
    }

    @OnClick({R.id.iv_adddevice})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_adddevice:
                startActivityForResult(new Intent(context, QuickMarkActivity.class), REQUESTCODE);
                break;
        }
    }

    /**
     * 接受二维码扫描的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && data != null) {
            String qrcode = data.getStringExtra(CommonConstant.QRCODE);
            if (!TextUtils.isEmpty(qrcode)) {
                Map map = new HashMap<String, String>();
                map.put("Event", "AppBindDevice");
                map.put("Device", qrcode);
                map.put("Port", "App");
                WebScocketHelper.getInstance().sendMsg(map);
            }
        }
    }
}
