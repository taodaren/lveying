package net.lueying.s_image.fragment;

import android.view.View;

import net.lueying.s_image.base.BaseFragment;
import net.lueying.s_image.R;

/**
 * 我的模块
 */

public class TabMineFragment extends BaseFragment {

    public static TabMineFragment newInstance() {
        return new TabMineFragment();
    }

    public TabMineFragment() {
    }

    @Override
    protected int layoutViewId() {
        return R.layout.fragment_tab_mine;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void initToolbar() {
        setToolbar(R.id.main_toolbar, R.string.title_home, View.VISIBLE);
    }

}
