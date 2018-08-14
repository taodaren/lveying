package net.lueying.s_image.ui.auth;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import net.lueying.s_image.R;
import net.lueying.s_image.adapter.LoginFragmentAda;
import net.lueying.s_image.base.BaseActivity;
import net.lueying.s_image.core.App;
import net.lueying.s_image.core.AppManager;
import net.lueying.s_image.ui.fragment.TabLoginFragment;
import net.lueying.s_image.ui.fragment.TabRegistFragment;
import net.lueying.s_image.ui.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录注册页面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.view_login)
    View view_login;
    @BindView(R.id.view_regist)
    View view_regist;
    @BindView(R.id.tv_regist)
    TextView tv_regist;
    @BindView(R.id.tv_login)
    TextView tv_login;


    private ArrayList<Fragment> fragments;

    @Override
    protected int layoutViewId() {
        //需要判断登陆状态确定是否进入主页面
        if (App.getApplication().isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        super.initView();
        fragments = new ArrayList<>();
        fragments.add(new TabRegistFragment());
        fragments.add(new TabLoginFragment());
        FragmentManager manager = getSupportFragmentManager();
        LoginFragmentAda ada = new LoginFragmentAda(manager, fragments);
        viewpager.setAdapter(ada);
    }

    @Override
    public void initListener() {
        super.initListener();

    }

    @OnClick({R.id.ll_login, R.id.ll_regist})
    public void swichFragment(View view) {
        switch (view.getId()) {
            case R.id.ll_regist:
                switchFragment(1);
                break;
            case R.id.ll_login:
                switchFragment(2);
                break;
        }
    }

    /**
     * 注册和登录页面的标记,1:注册;2:登录,修改标记切换页面
     *
     * @param flag
     */
    public void switchFragment(int flag) {
        if (flag >= 0) {
            switch (flag) {
                case 1:
                    view_regist.setVisibility(View.VISIBLE);
                    view_login.setVisibility(View.GONE);
                    viewpager.setCurrentItem(0, true);
                    tv_regist.setTextColor(getResources().getColor(R.color.colorBlack_333));
                    tv_login.setTextColor(getResources().getColor(R.color.colorGray_999));
                    viewpager.setCurrentItem(0);
                    break;
                case 2:
                    view_regist.setVisibility(View.GONE);
                    view_login.setVisibility(View.VISIBLE);
                    viewpager.setCurrentItem(1, true);
                    tv_regist.setTextColor(getResources().getColor(R.color.colorGray_999));
                    tv_login.setTextColor(getResources().getColor(R.color.colorBlack_333));
                    viewpager.setCurrentItem(1);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void initData() {
        super.initData();
    }
}
