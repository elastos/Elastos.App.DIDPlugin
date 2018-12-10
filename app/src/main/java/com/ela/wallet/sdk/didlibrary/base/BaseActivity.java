package com.ela.wallet.sdk.didlibrary.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.activity.BackupTipsActivity;
import com.ela.wallet.sdk.didlibrary.utils.LogUtil;


public abstract class BaseActivity extends AppCompatActivity{

    private String activityName = this.getClass().getSimpleName();

    private TextView tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.i(activityName + ":onCreate");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(getRootViewId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        tv_title = findViewById(R.id.title_tv_title);
        String title = getTitleText();
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        initView();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            getWindow().getDecorView().setSystemUiVisibility(option);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            getWindow().getDecorView().setFitsSystemWindows(true);
        }
        initData();
    }

    protected abstract int getRootViewId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(activityName + ":onPause");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.i(activityName + ":onNewIntent");
        setIntent(intent);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(activityName + ":onResume");
    }

    @Override
    protected void onStop() {
        LogUtil.i(activityName + ":onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.i(activityName + ":onDestroy");
        super.onDestroy();
    }

    public void onLeftNavClick(View view) {
        finish();
    }

    public void onRightTextClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, BackupTipsActivity.class);
        startActivity(intent);
    }

    public String getTitleText() {
        return null;
    }

    public void setTitleText(String text) {
        tv_title.setText(text);
    }

}
