package net.lueying.s_image;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import net.lueying.s_image.base.BaseActivity;
import net.lueying.s_image.fragment.TabAttentionFragment;
import net.lueying.s_image.fragment.TabDeviceFragment;
import net.lueying.s_image.fragment.TabHomeFragment;
import net.lueying.s_image.fragment.TabMineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private List<Fragment> mFragments;
    private Fragment currentFragment;
    private BottomNavigationViewEx mBottomNav;

    @Override
    protected int layoutViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        mFragments = getFragments();
        setDefFragment();
    }

    @Override
    public void initView() {
        initBtnNavBar();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 勿删，防止重叠
//        super.onSaveInstanceState(outState);
    }

    /**
     * 设置底部导航
     */
    private void initBtnNavBar() {
        mBottomNav = findViewById(R.id.main_bottom_nav);

        // 禁止所有动画效果
        mBottomNav.enableAnimation(false);
        mBottomNav.enableShiftingMode(false);
        mBottomNav.enableItemShiftingMode(false);

        // 设置中间录制模块
        mBottomNav.setIconSizeAt(2, 36, 36);
        mBottomNav.setIconTintList(2,
                getResources().getColorStateList(R.color.selector_item_gray_color));

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int beforePosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mBottomNav.getMenu().findItem(R.id.nav_record).setIcon(R.drawable.tab_recode);
                int position = 0;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        position = 0;
                        replaceFragment(mFragments.get(position));
                        break;
                    case R.id.nav_attention:
                        position = 1;
                        replaceFragment(mFragments.get(position));
                        break;
                    case R.id.nav_record:
                        Toast.makeText(MainActivity.this, "pop_window", Toast.LENGTH_SHORT).show();
                        return false;
                    case R.id.nav_device:
                        position = 2;
                        replaceFragment(mFragments.get(position));
                        break;
                    case R.id.nav_mine:
                        position = 3;
                        replaceFragment(mFragments.get(position));
                        break;
                }
                if (beforePosition != position) {
                    beforePosition = position;
                    Log.i("NAV", "-------position------- before item:" + mBottomNav.getCurrentItem()
                            + " current item:" + position + " ------------------");
                }
                return true;
            }
        });

        // 默认选中
        mBottomNav.getMenu().getItem(3).setChecked(true);
    }

    /**
     * 将 Fragment 加入 fragments 里面
     */
    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(TabHomeFragment.newInstance());
        list.add(TabAttentionFragment.newInstance());
        list.add(TabDeviceFragment.newInstance());
        list.add(TabMineFragment.newInstance());
        return list;
    }

    /**
     * 设置默认 fragment
     */
    private void setDefFragment() {
        Fragment defFragment = mFragments.get(0);
        if (!defFragment.isAdded()) {
            addFragment(R.id.main_layout_content, defFragment);
            currentFragment = defFragment;
        }
    }

    /**
     * 添加 Fragment 到 Activity 的布局
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 切换 fragment
     */
    @SuppressLint("CommitTransaction")
    private void replaceFragment(Fragment fragment) {
        // 添加或者显示 fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment == fragment)
            return;
        if (!fragment.isAdded()) {
            // 如果当前 fragment 未被添加，则添加到 Fragment 管理器中
            transaction.hide(currentFragment).add(R.id.main_layout_content, fragment).commit();
        } else {
            // 如果当前 fragment 已添加，则显示 Fragment 管理器中的 fragment
            transaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }

}
