package net.lueying.s_image.fragment;

import android.view.View;

import net.lueying.s_image.base.BaseFragment;
import net.lueying.s_image.R;

/**
 * 首页模块
 */

public class TabHomeFragment extends BaseFragment {

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }

    public TabHomeFragment() {
    }

    @Override
    protected int layoutViewId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void initToolbar() {
        setToolbar(R.id.main_toolbar, R.string.title_home, View.VISIBLE);
    }

}
