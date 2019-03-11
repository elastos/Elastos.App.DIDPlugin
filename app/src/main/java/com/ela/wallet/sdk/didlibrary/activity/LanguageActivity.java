package com.ela.wallet.sdk.didlibrary.activity;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.bean.WordModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.DidAlertDialog;
import com.ela.wallet.sdk.didlibrary.widget.LanguageRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends BaseActivity {

    private ListView rv_language;
    private LanguageRecyclerViewAdapter mAdapter;
    private List<WordModel> mList;

    @Override
    protected int getRootViewId() {
        return R.layout.activity_language;
    }

    @Override
    public String getTitleText() {
        return getString(R.string.me_preference_language);
    }

    @Override
    protected void initView() {
        rv_language = (ListView) findViewById(R.id.rv_language);
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
        mAdapter = new LanguageRecyclerViewAdapter(this, mList);
        rv_language.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LanguageRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position, TextView textView) {
                final int size = mList.size();
                final TextView ftextView = textView;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int k = 0;k < size; k++) {
                            if (k == position) {
                                mList.get(k).setClicked(true);
                                ftextView.setTextColor(getResources().getColor(R.color.appColor));
                            } else {
                                mList.get(k).setClicked(false);
                                ftextView.setTextColor(getResources().getColor(R.color.textBlack));
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onLeftNavClick(View view) {
        setResult(Activity.RESULT_OK);
        super.onLeftNavClick(view);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }
}
