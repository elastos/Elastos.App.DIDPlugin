package com.ela.wallet.sdk.didlibrary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class PwdInputActivity extends BaseActivity {

    private EditText et_pwd;
    private EditText et_pwd_again;


    @Override
    protected int getRootViewId() {
        return R.layout.activity_pwd_input;
    }

    @Override
    protected void initView() {
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd_again = (EditText) findViewById(R.id.et_pwd_again);
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getTitleText() {
        return getString(R.string.input_pwd);
    }

    public void onImportClick(View view) {
        if (et_pwd.getText().toString().length() < 8) {
            Toast.makeText(PwdInputActivity.this, getString(R.string.toast_pwd_notcorrect), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!et_pwd.getText().toString().equals(et_pwd_again.getText().toString())) {
            Toast.makeText(PwdInputActivity.this, getString(R.string.toast_pwd_same), Toast.LENGTH_SHORT).show();
            return;
        }
        String password = et_pwd.getText().toString();
        Utilty.setPreference(Constants.SP_KEY_DID_PASSWORD, password);
        Utilty.setPreference(Constants.SP_KEY_DID_ISBACKUP, "true");
        Utilty.setPreference(Constants.SP_KEY_DID_MNEMONIC, "");
        Toast.makeText(PwdInputActivity.this, getString(R.string.word_backdup), Toast.LENGTH_SHORT).show();
        showSuccessDialog();
    }

    private void showSuccessDialog() {
        new DidAlertDialog(this)
                .setTitle(getString(R.string.word_backdup))
                .setMessage(getString(R.string.word_tips))
                .setMessageGravity(Gravity.LEFT)
                .setRightButton(getString(R.string.btn_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(PwdInputActivity.this, PersonalActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PwdInputActivity.this.startActivity(intent);
                    }
                })
                .setCancelable(false)
                .show();
    }

}
