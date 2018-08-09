package net.lueying.s_image.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lueying.s_image.R;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * BaseFragment
 */

public abstract class BaseFragment extends Fragment {
    private View mRootView;
    protected CompositeSubscription mCompositeSubscription;
    protected Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 子类不再需要设置布局 ID，也不再需要使用 ButterKnife.BindView()
        mRootView = inflater.inflate(layoutViewId(), container, false);
        mCompositeSubscription = new CompositeSubscription();
        ButterKnife.bind(this, mRootView);
        initView(mRootView);
        return mRootView;
    }

    public void initView(View rootView) {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolbar();
        initData();
        initListener();
    }

    public void initToolbar() {
        // Fragment 中必须在 onActivityCreated 方法中初始化 Toolbar
    }

    public void initData() {
    }

    public void initListener() {
    }

    /**
     * 由子类实现
     *
     * @return 得到当前界面的布局文件 id（）
     */
    protected abstract int layoutViewId();

    /**
     * 设置 Toolbar
     *
     * @param toolbarId       menu_toolbar ID
     * @param title           标题
     * @param titleVisibility 标题控件是否显示
     */
    public void setToolbar(int toolbarId, int title, int titleVisibility) {
        // Fragment 中使用 Toolbar
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        Toolbar toolbar = mRootView.findViewById(toolbarId);
        assert appCompatActivity != null;
        appCompatActivity.setSupportActionBar(toolbar);

        // 设置标题
        TextView textTitle = mRootView.findViewById(R.id.tv_title_toolbar);
        textTitle.setVisibility(titleVisibility);
        textTitle.setText(title);

        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            // 隐藏 Toolbar 自带标题栏
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

}
