package net.lueying.s_image.ui.common;


import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import net.lueying.s_image.R;
import net.lueying.s_image.base.BaseActivity;
import net.lueying.s_image.constant.CommonConstant;
import net.lueying.s_image.utils.LogUtil;
import net.lueying.s_image.utils.ToastUtil;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QuickMarkActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final int RESULT_CODE = 102;
    @BindView(R.id.zxingview)
    ZXingView zxingview;

    @Override
    protected int layoutViewId() {
        return R.layout.activity_quick_mark;
    }

    @Override
    public void initView() {
        super.initView();
        //设置二维码的代理
        zxingview.setDelegate(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //打开后置摄像头预览,但并没有开始扫描
        zxingview.startCamera();
        //开启扫描框
        zxingview.showScanRect();
        zxingview.startSpot();
    }

    @Override
    protected void onStop() {
        zxingview.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zxingview.onDestroy();
        super.onDestroy();
    }

    //扫描成功解析二维码成功后调用,可做对应的操作
    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtil.i("扫描结果为:" + result);
//        zxingview.startSpot();
        if (!TextUtils.isEmpty(result)) {
            Intent in = new Intent();
            in.putExtra(CommonConstant.QRCODE, result);
            setResult(RESULT_CODE, in);
            finish();
        }


    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "相机打开失败", Toast.LENGTH_SHORT).show();
    }
}
