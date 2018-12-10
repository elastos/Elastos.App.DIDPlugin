package com.ela.wallet.sdk.didlibrary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.bean.WordModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.LanguageRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends BaseActivity {

    private RecyclerView rv_language;
    private LanguageRecyclerViewAdapter mAdapter;
    private List<WordModel> mList;

    @Override
    protected int getRootViewId() {
        return R.layout.activity_language;
    }

    @Override
    protected void initView() {
        rv_language = findViewById(R.id.rv_language);
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>(2);
        String sp_lang = Utilty.getPreference(Constants.SP_KEY_APP_LANGUAGE, "");
        if (TextUtils.isEmpty(sp_lang)) {
            if (Locale.getDefault().getLanguage().contains("zh")) {
                sp_lang = "chinese";
            } else {
                sp_lang = "english";
            }
        }
        mList.clear();
        if ("chinese".equals(sp_lang)) {
            mList.add(new WordModel("ENGLISH", false));
            mList.add(new WordModel("中文（简体）", true));
        } else {
            mList.add(new WordModel("ENGLISH", true));
            mList.add(new WordModel("中文（简体）", false));
        }
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new LanguageRecyclerViewAdapter(this, mList);
        rv_language.setLayoutManager(linearLayoutManager);
        rv_language.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LanguageRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position, TextView textView) {
                final int size = mList.size();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int k=0;k<size;k++) {
                            TextView tv = linearLayoutManager.findViewByPosition(k).findViewById(R.id.tv_item_language);
                            if (k == position) {
                                mList.get(k).setClicked(true);
                                tv.setTextColor(getResources().getColor(R.color.appColor));
                            } else {
                                mList.get(k).setClicked(false);
                                tv.setTextColor(getResources().getColor(R.color.textBlack));
                            }
                        }
                    }
                });
            }
        });
    }

}
