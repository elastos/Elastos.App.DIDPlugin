package com.ela.wallet.sdk.didlibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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

public class SendActivity extends BaseActivity {


    private EditText et_scan_address;
    private ImageView iv_scan;

    private EditText et_amount;

    @Override
    protected void initView() {
        et_scan_address = findViewById(R.id.et_scan_address);
        iv_scan = findViewById(R.id.iv_scan);
        et_amount = findViewById(R.id.et_amount);

        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SendActivity.this, ScanActivity.class);
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

    @Override
    protected int getRootViewId() {
        return R.layout.activity_send;
    }

    @Override
    public String getTitleText() {
        return getString(R.string.nav_pay);
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

    public void onOKClick(View view) {
        if (Utilty.isFastDoubleClick()) return;
        String toAddress = et_scan_address.getText().toString();
        String amount = et_amount.getText().toString();
        if (TextUtils.isEmpty(toAddress) || TextUtils.isEmpty(amount)) {
            Toast.makeText(SendActivity.this, "params invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = Utilty.getPreference(Constants.SP_KEY_DID_PASSWORD, "");
        if (!TextUtils.isEmpty(password)) {
            final DidAlertDialog dialog = new DidAlertDialog(this);
                dialog.setTitle(getString(R.string.send_enter_pwd))
                    .setEditText(true)
                    .setLeftButton(getString(R.string.btn_cancel), null)
                    .setRightButton(getString(R.string.btn_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String input = dialog.getEditTextView().getText().toString().trim();
                            if (password.equals(input)) {
                                doSend();
                            } else {
                                Toast.makeText(SendActivity.this, getString(R.string.toast_pwd_wrong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
        } else {
            doSend();
        }
    }

    private void doSend() {
        String toAddress = et_scan_address.getText().toString();
        String amount = et_amount.getText().toString();
        if (TextUtils.isEmpty(toAddress) || TextUtils.isEmpty(amount)) {
            Toast.makeText(SendActivity.this, "params invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        DidLibrary.Zhuanzhang(toAddress, Long.parseLong(amount), new TransCallback() {
            @Override
            public void onSuccess(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "";
                        if (result.contains("200")) {
                            msg = getString(R.string.dialog_send_success);
                        } else {
                            msg = getString(R.string.dialog_finance_failed);
                        }
                        Toast.makeText(SendActivity.this, result, Toast.LENGTH_SHORT).show();
                        new DidAlertDialog(SendActivity.this)
                                .setTitle(msg)
                                .setMessage(result)
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
                        Toast.makeText(SendActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
                        intent.setClass(SendActivity.this, BackupTipsActivity.class);
                        SendActivity.this.startActivity(intent);
                    }
                })
                .show();
    }
}
