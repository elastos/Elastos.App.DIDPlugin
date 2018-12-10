package com.ela.wallet.sdk.didlibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.callback.TransCallback;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.DidLibrary;
import com.ela.wallet.sdk.didlibrary.utils.LogUtil;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.DidAlertDialog;


public class ReChargeActivity extends BaseActivity {
    private EditText et_scan_address;
    private ImageView iv_scan;

    private EditText et_amount;
    private EditText et_phrase;

    @Override
    protected int getRootViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        et_scan_address = findViewById(R.id.et_scan_address);
        iv_scan = findViewById(R.id.iv_scan);

        et_amount = findViewById(R.id.et_amount);
        et_phrase = findViewById(R.id.et_phrase);

        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ReChargeActivity.this, ScanActivity.class);
                startActivityForResult(intent, Constants.INTENT_REQUEST_CODE_SCAN);
            }
        });
    }

    @Override
    protected void initData() {
        if (!Utilty.isBacked()) {
            showBackupDialog();
        }
    }

    public void onOKClick(View view) {
        //todo:
        String fromAddress = et_scan_address.getText().toString();
        String mnemonic = et_phrase.getText().toString();
        String amount = et_amount.getText().toString();
        if (TextUtils.isEmpty(fromAddress) || TextUtils.isEmpty(mnemonic) || TextUtils.isEmpty(amount)) {
            Toast.makeText(ReChargeActivity.this, "params invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        DidLibrary.Chongzhi(fromAddress, Long.parseLong(amount), mnemonic, new TransCallback() {
            @Override
            public void onSuccess(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ReChargeActivity.this, result, Toast.LENGTH_SHORT).show();
                        new DidAlertDialog(ReChargeActivity.this)
                                .setTitle("充值成功")
                                .setRightButton(getString(R.string.btn_ok), null)
                                .show();
                    }
                });
            }

            @Override
            public void onFailed(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ReChargeActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK && data != null) {
            String result = data.getStringExtra(Constants.INTENT_PARAM_KEY_SCANRESUTL);
            et_scan_address.setText(result);
        }
    }

    private void showBackupDialog() {
        new DidAlertDialog(this)
                .setTitle(getString(R.string.send_backup))
                .setMessage(getString(R.string.send_tips))
                .setMessageGravity(Gravity.LEFT)
                .setRightButton(getString(R.string.btn_backup), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(ReChargeActivity.this, BackupTipsActivity.class);
                        ReChargeActivity.this.startActivity(intent);
                    }
                })
                .show();
    }
}
