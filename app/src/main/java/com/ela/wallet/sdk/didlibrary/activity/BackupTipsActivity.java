package com.ela.wallet.sdk.didlibrary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;

public class BackupTipsActivity extends BaseActivity {

    @Override
    protected int getRootViewId() {
        return R.layout.activity_backup_tips;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void onNextClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, WordShowActivity.class);
        startActivity(intent);
    }
}
