package net.lueying.s_image.fragment;

import android.view.View;

import net.lueying.s_image.base.BaseFragment;
import net.lueying.s_image.R;

/**
 * 关注模块
 */

public class TabAttentionFragment extends BaseFragment {

    public static TabAttentionFragment newInstance() {
        return new TabAttentionFragment();
    }

    public TabAttentionFragment() {
    }

    @Override
    protected int layoutViewId() {
        return R.layout.fragment_tab_attention;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void initToolbar() {
        setToolbar(R.id.main_toolbar, R.string.title_attention, View.VISIBLE);
    }

}
