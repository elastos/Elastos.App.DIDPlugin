package com.ela.wallet.sdk.didlibrary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.LogUtil;
import com.qingmei2.library.view.QRCodeScannerView;
import com.qingmei2.library.view.QRCoverView;

public class ScanActivity extends BaseActivity {

    private final int PERMISSION_REQUEST_CAMERA = 0;
    boolean isProcess = false;
    Context mContext;

    QRCodeScannerView scanner;
    QRCoverView cover;

    @Override
    protected int getRootViewId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initData() {

    }

    protected void initView() {
        mContext =this;
        scanner=findViewById(R.id.scanner);
        cover=findViewById(R.id.cover);
        //自动聚焦间隔2s
        scanner.setAutofocusInterval(2000L);
        //扫描结果监听处理
        scanner.setOnQRCodeReadListener(new QRCodeScannerView.OnQRCodeScannerListener() {
            @Override
            public void onDecodeFinish(String text, PointF[] points) {
                if (!isProcess) {
                    isProcess = true;
                    judgeResult(text, points);
                }

            }
        });
        //相机权限监听
        scanner.setOnCheckCameraPermissionListener(new QRCodeScannerView.OnCheckCameraPermissionListener() {
            public boolean onCheckCameraPermission() {
                if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                    return false;
                }
            }
        });
        //开启后置摄像头
        scanner.setBackCamera();
    }

    /**
     * 权限请求回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanner.grantCameraPermission();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanner.startCamera();
    }

    private void judgeResult(String result, PointF[] points) {
        LogUtil.i("scan result:" + result);
        if(TextUtils.isEmpty(result)) {
            Toast.makeText(ScanActivity.this, getString(R.string.scan_noresult), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_PARAM_KEY_SCANRESUTL, result);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}