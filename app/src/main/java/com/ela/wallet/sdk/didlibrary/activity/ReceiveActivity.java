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
    private TextView tv_title;
    private TextView tv_balance;

    @Override
    protected void initView() {
        iv_qr = (ImageView) findViewById(R.id.iv_charge_qr);
        tv_copy = (TextView) findViewById(R.id.tv_charge_copyaddress);
        tv_reset = (TextView) findViewById(R.id.tv_charge_reset_address);
        tv_title = (TextView) findViewById(R.id.tv_charge_did);
        tv_balance = (TextView)  findViewById(R.id.tv_charge_did_balance);

        iv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("did", Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "")));
                Toast.makeText(ReceiveActivity.this, getString(R.string.toast_copy), Toast.LENGTH_SHORT).show();
            }
        });

        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("did", Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "")));
                Toast.makeText(ReceiveActivity.this, getString(R.string.toast_copy), Toast.LENGTH_SHORT).show();
            }
        });
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String newAddress = DidLibrary.resetAddress();
//                if (!TextUtils.isEmpty(newAddress)) {
//                    initQrView();
//                    loadBalanceData();
//                }
            }
        });
    }

    @Override
    protected void initData() {
        initQrView();
        if (!Utilty.isBacked()) {
            showBackupDialog();
        }

        String from = getIntent().getStringExtra(Constants.INTENT_PARAM_KEY_QRCODE_FROM);
        if (!TextUtils.isEmpty(from) && from.equals("did")) {
            setTitleText(getString(R.string.did_qr));
            loadDidBalanceData();
        }

        if (!TextUtils.isEmpty(from) && from.equals("ela")) {
            setTitleText(getString(R.string.ela_qr));
            loadElaBalanceData();
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_receive;
    }

    @Override
    public String getTitleText() {
        return getString(R.string.nav_charges);
    }

    private void initQrView() {
        String address = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");
        if (!TextUtils.isEmpty(address)) {
            new QRCodeEncoder(ReceiveActivity.this).createQrCode2ImageView(address, iv_qr);
            tv_copy.setText(address);
        }
    }

    private void loadDidBalanceData() {
        tv_title.setText(getString(R.string.did_balance));
        String url = String.format("%s%s%s", Urls.SERVER_DID, Urls.DID_BALANCE, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BalanceBean bean = new Gson().fromJson(response, BalanceBean.class);
                        String text = String.format("%s ELA", bean.getResult());
                        tv_balance.setText(text);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("%s ELA", "--");
                        tv_balance.setText(text);
                    }
                });
            }
        });
    }

    private void loadElaBalanceData() {
        tv_title.setText(getString(R.string.ela_balance));
        String url = String.format("%s%s%s", Urls.SERVER_WALLET, Urls.ELA_BALANCE, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BalanceBean bean = new Gson().fromJson(response, BalanceBean.class);
                        String text = String.format("%s ELA", bean.getResult());
                        tv_balance.setText(text);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("%s ELA", "--");
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
