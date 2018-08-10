package net.lueying.s_image.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.lueying.s_image.R;
import net.lueying.s_image.base.BaseFragment;
import net.lueying.s_image.constant.UserConstant;
import net.lueying.s_image.core.App;
import net.lueying.s_image.entity.DeviceIndex;
import net.lueying.s_image.logic.DeviceLogic;
import net.lueying.s_image.net.BaseSubscriber;
import net.lueying.s_image.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 设备模块
 */

public class TabDeviceFragment extends BaseFragment {
    @BindView(R.id.ll_device)
    LinearLayout ll_device;
    @BindView(R.id.ll_group)
    LinearLayout ll_group;

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
        String token = App.getApplication().getEncryptConfig(UserConstant.TOKEN);
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

}
