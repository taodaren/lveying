package net.lueying.s_image.ui.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.lueying.s_image.R;
import net.lueying.s_image.base.BaseFragment;
import net.lueying.s_image.constant.UserConstant;
import net.lueying.s_image.core.App;
import net.lueying.s_image.entity.Register;
import net.lueying.s_image.logic.UserLogic;
import net.lueying.s_image.net.BaseSubscriber;
import net.lueying.s_image.net.HttpResult;
import net.lueying.s_image.utils.Encryption;
import net.lueying.s_image.utils.LogUtil;
import net.lueying.s_image.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TabRegistFragment extends BaseFragment {

    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_verification)
    EditText et_verification;
    @BindView(R.id.et_paseword)
    EditText et_paseword;
    @BindView(R.id.btn_getverification)
    Button btn_getverification;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    private int count_time = 300; //短信发送之后间隔时间

    @Override
    protected int layoutViewId() {
        return R.layout.fragment_tab_regist;
    }

    @Override
    public void initListener() {
        super.initListener();
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkPhoneNum(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    /**
     * 检查号码尾数
     *
     * @param phone
     */
    private void checkPhoneNum(String phone) {
        if (phone != null && phone.length() == 11) {
            btn_getverification.setClickable(true);
            btn_getverification.setBackground(getResources().getDrawable(R.drawable.bg_submit_selector));
            btn_getverification.setTextColor(getResources().getColor(R.color.colorGray_666));
        } else {
            btn_getverification.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_getverification.setBackground(getResources().getDrawable(R.drawable.shape_bg_submit_gray));
        }
    }

    @Override
    public void initData() {
        super.initData();
    }


    @OnClick({R.id.btn_getverification, R.id.btn_submit})
    public void onClick(View view) {
        Map<String, String> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.btn_getverification:
                map.clear();
                try {
                    String iv = Encryption.newIv();
                    String phone = Encryption.encrypt(et_phone.getText().toString(), iv);
                    map.put("mobile", phone);
                    map.put("iv", iv);
                    mCompositeSubscription.add(UserLogic.sendMsg(map)
                            .subscribe(new BaseSubscriber<HttpResult>() {
                                           @Override
                                           public void onSuccess(HttpResult s) {
                                               ToastUtil.showShort(context, "短信已发送");
                                           }

                                           @Override
                                           public void onFailed(Throwable e) {
                                               ToastUtil.showShort(context, e.getMessage());
                                           }
                                       }
                            ));

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    openTimer();
                }
                break;
            case R.id.btn_submit:
                map.clear();
                String phone = et_phone.getText().toString().trim();
                String password = et_paseword.getText().toString().trim();
                String code = et_verification.getText().toString().trim();

                if (phone == null || phone.length() != 11) {
                    ToastUtil.showShort(context, "手机号码有误");
                    return;
                }
                if (code == null || code.length() != 4) {
                    ToastUtil.showShort(context, "验证码有误");
                    return;
                }
                if (password == null || password.length() < 6) {
                    ToastUtil.showShort(context, "密码有误");
                    return;
                }
                map.put("code", code);
                map.put("password", password);
                map.put("mobile", phone);
                map.put("device", App.getApplication().getAndroidID());

                mCompositeSubscription.add(UserLogic.register(map).subscribe(new BaseSubscriber<Register>() {
                    @Override
                    public void onSuccess(Register s) {
                        ToastUtil.showShort(context,"注册成功");
                        if (s != null) {
                            //本地化用户登录信息
                            App.getApplication().setConfigs(new Properties() {{
                                try {
                                    setProperty(UserConstant.TOKEN, Encryption.encrypt(s.getToken(), UserConstant.IV));
                                    App.getApplication().initUser();
                                } catch (Exception e) {
                                    LogUtil.e("加密异常:" + e.getMessage());
                                }
                            }});
                        }
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        ToastUtil.showShort(context, e.getMessage());
                    }
                }));
                break;
        }
    }

    /**
     * 5分钟之内只能点击一次
     */
    private void openTimer() {
        Observable.interval(0, 1, TimeUnit.SECONDS) //0延迟  每隔1秒触发
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .take(count_time + 1) //设置循环次数
                .map(aLong -> count_time - aLong) //从60-1
                .doOnSubscribe(() -> btn_getverification.setClickable(false)) //执行过程中按键为不可点击状态
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {//循环结束调用此方法
                        //复原按钮状态
                        btn_getverification.setClickable(true);
                        btn_getverification.setBackground(getResources().getDrawable(R.drawable.bg_submit_selector));
                        btn_getverification.setText("获取验证码");
                        btn_getverification.setTextColor(getResources().getColor(R.color.colorGray_666));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {//每隔一秒执行
                        //修改按钮为不可点击
                        btn_getverification.setTextColor(getResources().getColor(R.color.colorWhite));
                        btn_getverification.setBackground(getResources().getDrawable(R.drawable.shape_bg_submit_gray));
                        btn_getverification.setText(aLong + "s重新发送");
                    }
                });
    }


}
