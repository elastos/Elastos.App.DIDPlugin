package com.ela.wallet.sdk.didlibrary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.LogUtil;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;

public class WordShowActivity extends BaseActivity{

    private TextView tv_word_show;

    @Override
    protected int getRootViewId() {
        return R.layout.activity_word_show;
    }

    @Override
    protected void initView() {
        tv_word_show = findViewById(R.id.tv_word_show);
    }

    @Override
    protected void initData() {
        String words = Utilty.getPreference(Constants.SP_KEY_DID_MNEMONIC, "");
        LogUtil.w(words);
//        罚 津 召 抬 裁 蛋 摇 侵 式 桃 铺 豪
        tv_word_show.setText(words);
    }

    public void onOKClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, WordInputActivity.class);
        startActivity(intent);
    }
}
