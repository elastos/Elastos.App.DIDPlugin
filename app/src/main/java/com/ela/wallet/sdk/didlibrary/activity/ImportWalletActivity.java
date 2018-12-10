package com.ela.wallet.sdk.didlibrary.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.DidLibrary;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.DidAlertDialog;

public class ImportWalletActivity extends BaseActivity {

    private EditText et_import_word;
    private EditText et_pwd;
    private EditText et_pwd_again;

    private DidAlertDialog mConfirmDialog;
    private DidAlertDialog mErrorDialog;

    @Override
    protected int getRootViewId() {
        return R.layout.activity_import_wallet;
    }

    @Override
    protected void initView() {
        et_import_word = findViewById(R.id.et_import_word);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_again = findViewById(R.id.et_pwd_again);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        if (mConfirmDialog != null && mConfirmDialog.isShowing()) {
            mConfirmDialog.dismiss();
            mConfirmDialog = null;
        }
        if (mErrorDialog != null && mErrorDialog.isShowing()) {
            mErrorDialog.dismiss();
            mErrorDialog = null;
        }
        super.onDestroy();
    }

    public void onImportClick(View view) {
        if (et_pwd.getText().toString().length() < 8) {
            Toast.makeText(ImportWalletActivity.this, getString(R.string.toast_pwd_notcorrect), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!et_pwd.getText().toString().equals(et_pwd_again.getText().toString())) {
            Toast.makeText(ImportWalletActivity.this, getString(R.string.toast_pwd_same), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mConfirmDialog == null) {
            mConfirmDialog = new DidAlertDialog(this);
        }
        mConfirmDialog.setTitle(getString(R.string.dialog_becareful))
                .setMessage(getString(R.string.dialog_switch_wallet))
                .setMessageGravity(Gravity.CENTER)
                .setLeftButton(getString(R.string.btn_cancel), null)
                .setRightButton(getString(R.string.btn_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_import_word.getText().toString().length() <= 0) {
                            showErrorDialog();
                        }
                        boolean b = DidLibrary.importWallet(et_import_word.getText().toString());
                        if (b) {
                            Utilty.setPreference(Constants.SP_KEY_DID_PASSWORD, et_import_word.getText().toString());
                            Toast.makeText(ImportWalletActivity.this, getString(R.string.toast_import_success), Toast.LENGTH_SHORT).show();
                            ImportWalletActivity.this.finish();
                        } else {
                            showErrorDialog();
                        }
                    }
                })
                .show();
    }

    @Override
    public String getTitleText() {
        return getString(R.string.import_wallet);
    }

    private void showErrorDialog() {
        if (mErrorDialog == null) {
            mErrorDialog = new DidAlertDialog(this);
        }
        mErrorDialog
                .setTitle(getString(R.string.dialog_unable_import))
                .setMessage(getString(R.string.dialog_invalid_phrase))
                .setMessageGravity(Gravity.CENTER)
                .setRightButton(getString(R.string.btn_ok), null)
                .show();
    }
}
