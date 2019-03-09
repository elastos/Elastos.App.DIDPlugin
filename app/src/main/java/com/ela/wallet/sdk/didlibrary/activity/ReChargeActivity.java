package com.ela.wallet.sdk.didlibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

    @Override
    protected int getRootViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    public String getTitleText() {
        return getString(R.string.me_recharge);
    }

    @Override
    protected void initView() {
        et_scan_address = (EditText) findViewById(R.id.et_scan_address);
        iv_scan = (ImageView) findViewById(R.id.iv_scan);

        et_amount = (EditText) findViewById(R.id.et_amount);

        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ReChargeActivity.this, ScanActivity.class);
                startActivityForResult(intent, Constants.INTENT_REQUEST_CODE_SCAN);
            }
        });

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 8)
                {
                    editable.delete(posDot + 9, posDot + 10);
                }

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
        if (Utilty.isFastDoubleClick()) return;
//        String fromAddress = et_scan_address.getText().toString();
        String amount = et_amount.getText().toString();
        if (/*TextUtils.isEmpty(fromAddress) || */TextUtils.isEmpty(amount)) {
            Toast.makeText(ReChargeActivity.this, "params invalid", Toast.LENGTH_SHORT).show();
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
                                doRecharge();
                            } else {
                                Toast.makeText(ReChargeActivity.this, getString(R.string.toast_pwd_wrong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
        } else {
            doRecharge();
        }

    }

    private void doRecharge() {
        String fromAddress = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");
        String amount = String.format("%.8f", Float.parseFloat(et_amount.getText().toString()));
        if (TextUtils.isEmpty(fromAddress) || TextUtils.isEmpty(amount)) {
            Toast.makeText(ReChargeActivity.this, "params invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(et_amount, getString(R.string.trans_sending), Snackbar.LENGTH_SHORT).show();
        DidLibrary.Ela2Did(fromAddress, amount, new TransCallback() {
            @Override
            public void onSuccess(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "";
                        if (result.contains("200")) {
                            msg = getString(R.string.dialog_recharge_success);
                        } else {
                            msg = getString(R.string.dialog_finance_failed);
                        }
//                        Toast.makeText(ReChargeActivity.this, result, Toast.LENGTH_SHORT).show();
                        new DidAlertDialog(ReChargeActivity.this)
                                .setTitle(msg)
                                .setRightButton(getString(R.string.btn_ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        et_amount.setText("");
                                        et_scan_address.setText("");
                                    }
                                })
                                .show();
                    }
                });
            }

            @Override
            public void onFailed(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "";
                        if (result.contains("400") && result.contains("Not Enough UTXO")) {
                            msg = getString(R.string.dialog_trans_notenough);
                        } else {
                            msg = getString(R.string.dialog_trans_failed);
                        }
                        new DidAlertDialog(ReChargeActivity.this)
                                .setTitle(msg)
                                .setRightButton(getString(R.string.btn_ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        et_amount.setText("");
                                        et_scan_address.setText("");
                                    }
                                })
                                .show();
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
