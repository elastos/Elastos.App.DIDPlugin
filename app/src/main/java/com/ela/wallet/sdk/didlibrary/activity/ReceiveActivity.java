package com.ela.wallet.sdk.didlibrary.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.bean.BalanceBean;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.ela.wallet.sdk.didlibrary.utils.DidLibrary;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.DidAlertDialog;
import com.google.gson.Gson;
import com.qingmei2.library.encode.QRCodeEncoder;

public class ReceiveActivity extends BaseActivity {

    private ImageView iv_qr;
    private TextView tv_copy;
    private TextView tv_reset;
    private TextView tv_balance;

    @Override
    protected void initView() {
        iv_qr = findViewById(R.id.iv_charge_qr);
        tv_copy = findViewById(R.id.tv_charge_copyaddress);
        tv_reset = findViewById(R.id.tv_charge_reset_address);
        tv_balance = findViewById(R.id.tv_charge_did_balance);

        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("did", Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "")));
                Toast.makeText(ReceiveActivity.this, "copy success", Toast.LENGTH_SHORT).show();
            }
        });
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAddress = DidLibrary.resetAddress();
                if (!TextUtils.isEmpty(newAddress)) {
                    initQrView();
                    loadBalanceData();
                }
            }
        });
    }

    @Override
    protected void initData() {
        initQrView();
        loadBalanceData();
        if (!Utilty.isBacked()) {
            showBackupDialog();
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_receive;
    }

    private void initQrView() {
        String address = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");
        if (!TextUtils.isEmpty(address)) {
            new QRCodeEncoder(ReceiveActivity.this).createQrCode2ImageView(address, iv_qr, R.drawable.icon_launcher);
            tv_copy.setText(address);
        }
    }

    private void loadBalanceData() {
        String url = String.format("%s%s%s", Urls.SERVER_DID, Urls.DID_BALANCE, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BalanceBean bean = new Gson().fromJson(response, BalanceBean.class);
                        String text = String.format("%s: %s ELA", getString(R.string.home_balance), bean.getResult());
                        tv_balance.setText(text);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("%s: %s ELA", getString(R.string.home_balance), "--");
                        tv_balance.setText(text);
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
                        intent.setClass(ReceiveActivity.this, BackupTipsActivity.class);
                        ReceiveActivity.this.startActivity(intent);
                    }
                })
                .show();
    }

}
