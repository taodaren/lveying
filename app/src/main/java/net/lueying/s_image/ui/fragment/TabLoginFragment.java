package net.lueying.s_image.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.lueying.s_image.R;
import net.lueying.s_image.base.BaseFragment;
import net.lueying.s_image.constant.UserConstant;
import net.lueying.s_image.core.App;
import net.lueying.s_image.core.AppManager;
import net.lueying.s_image.entity.Register;
import net.lueying.s_image.logic.UserLogic;
import net.lueying.s_image.net.BaseSubscriber;
import net.lueying.s_image.ui.MainActivity;
import net.lueying.s_image.ui.auth.ChangePswActivity;
import net.lueying.s_image.ui.auth.LoginActivity;
import net.lueying.s_image.utils.Encryption;
import net.lueying.s_image.utils.LogUtil;
import net.lueying.s_image.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.OnClick;

public class TabLoginFragment extends BaseFragment {
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_paseword)
    EditText et_paseword;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.tv_forget_psw)
    TextView tv_forget_psw;

    @Override
    protected int layoutViewId() {
        return R.layout.fragment_tab_login;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.btn_submit, R.id.tv_forget_psw})
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                submit();
                break;
            case R.id.tv_forget_psw:
                startActivity(new Intent(context, ChangePswActivity.class));
                break;
        }
    }

    /**
     * 登录
     */
    private void submit() {
        Map map = new HashMap<String, String>();
        String phone = et_phone.getText().toString().trim();
        String password = et_paseword.getText().toString().trim();

        if (phone == null || phone.length() != 11) {
            ToastUtil.showShort(context, "手机号码有误");
            return;
        }
        if (password == null || password.length() < 6) {
            ToastUtil.showShort(context, "密码有误");
            return;
        }
        try {
            String iv = Encryption.newIv();
            map.put("password", Encryption.encrypt(password, iv));
            map.put("mobile", phone);
            map.put("iv", iv);
            map.put("device", App.getApplication().getAndroidID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCompositeSubscription.add(UserLogic.login(map).subscribe(new BaseSubscriber<Register>() {
            @Override
            public void onSuccess(Register s) {
                ToastUtil.showShort(context, "登录成功");
                if (s != null) {
                    //本地化用户登录信息
                    App.getApplication().setConfigs(new Properties() {{
                        try {
                            setProperty(UserConstant.TOKEN, Encryption.encrypt(s.getToken(), UserConstant.IV));
                        } catch (Exception e) {
                            LogUtil.e("加密异常:" + e.getMessage());
                        }
                    }});
                    App.getApplication().initUser();
                    startActivity(new Intent(context, MainActivity.class));
                    AppManager.getAppManager().finishActivity(LoginActivity.class);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.showShort(context, e.getMessage());
            }
        }));
    }

}

