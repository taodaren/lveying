package net.lueying.s_image.fragment;

import android.view.View;

import net.lueying.s_image.base.BaseFragment;
import net.lueying.s_image.R;

/**
 * 设备模块
 */

public class TabDeviceFragment extends BaseFragment {

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
    public void initToolbar() {
        setToolbar(R.id.main_toolbar, R.string.title_device, View.VISIBLE);
    }

}
