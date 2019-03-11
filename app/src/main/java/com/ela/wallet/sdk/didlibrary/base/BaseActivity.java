package com.ela.wallet.sdk.didlibrary.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.activity.BackupTipsActivity;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.LogUtil;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;

import java.util.Locale;


public abstract class BaseActivity extends FragmentActivity {

    private String activityName = this.getClass().getSimpleName();

    private TextView tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.i(activityName + ":onCreate");
        super.onCreate(savedInstanceState);
		//TODO houhong
//        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        setContentView(getRootViewId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        tv_title = (TextView) findViewById(R.id.title_tv_title);
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

    @Override
    protected void attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 8.0需要使用createConfigurationContext处理
            super.attachBaseContext(updateResources(context));
        } else {
            super.attachBaseContext(context);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResources(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale;
        if (getSavedLanguage().equals("chinese")) {
            locale = Locale.SIMPLIFIED_CHINESE;
            config.locale = Locale.SIMPLIFIED_CHINESE;
            LogUtil.i(activityName + ":getSavedLanguage chinese");
            LogUtil.i(activityName + ":Locale.SIMPLIFIED_CHINESE");
        } else {
            locale = Locale.ENGLISH;// getSetLocale方法是获取新设置的语言
            config.locale = Locale.ENGLISH;
            LogUtil.i(activityName + ":getSavedLanguage ENGLISH");
            LogUtil.i(activityName + ":Locale.ENGLISH");
        }

        resources.updateConfiguration(config, dm);

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
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
        Utilty.getContext();
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

    private String getSavedLanguage() {
        String language = Utilty.getPreference(Constants.SP_KEY_APP_LANGUAGE, "");
        if (TextUtils.isEmpty(language)) {
            if (Locale.getDefault().getLanguage().contains("zh")) {
                return "chinese";
            } else {
                return "english";
            }
        } else {
            return language;
        }
    }

}
