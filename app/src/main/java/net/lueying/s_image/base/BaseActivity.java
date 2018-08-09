package net.lueying.s_image.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import net.lueying.s_image.R;
import net.lueying.s_image.core.AppManager;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * BaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected CompositeSubscription mCompositeSubscription;
    protected Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        init();
        // 子类不再需要设置布局 ID，也不再需要使用 ButterKnife.BindView()
        setContentView(layoutViewId());
        ButterKnife.bind(this);
        context = this;
        mCompositeSubscription = new CompositeSubscription();
        initToolbar();
        setStatusBar();
        initData();
        initView();
        initListener();
    }

    /**
     * 在 setContentView() 调用之前调用，可以设置 WindowFeature (如：this.requestWindowFeature(Window.FEATURE_NO_TITLE);)
     */
    public void init() {
    }

    /**
     * 沉浸式状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentDiff(this);
    }

    public void initToolbar() {
    }

    public void initData() {
    }

    public void initView() {
    }

    public void initListener() {
    }

    /**
     * 由子类实现
     *
     * @return 当前界面的布局文件 id
     */
    protected abstract int layoutViewId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 设置 Toolbar
     *
     * @param title           标题
     * @param titleVisibility 标题控件是否显示
     */
    public void setToolbar(String title, int titleVisibility) {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 隐藏 Toolbar 左侧导航按钮
            actionBar.setDisplayHomeAsUpEnabled(false);
            // 隐藏 Toolbar 自带标题栏
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // 设置标题
        TextView textTitle = findViewById(R.id.tv_title_toolbar);
        textTitle.setVisibility(titleVisibility);
        textTitle.setText(title);
        // 设置返回按钮
        ImageView imgTitleBack = findViewById(R.id.img_title_back);
        imgTitleBack.setVisibility(View.VISIBLE);
        imgTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
