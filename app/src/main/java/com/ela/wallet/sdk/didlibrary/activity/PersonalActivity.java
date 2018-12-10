package com.ela.wallet.sdk.didlibrary.activity;

import android.content.Intent;
import android.icu.text.Collator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.bean.BalanceBean;
import com.ela.wallet.sdk.didlibrary.bean.SettingModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.PersonalRecyclerViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonalActivity extends BaseActivity {

    private TextView tv_balance;
//    private RelativeLayout rl_language;
//    private TextView tv_language_tips;
//    private RelativeLayout rl_importwallet;

    private RecyclerView rv_personal;
    private PersonalRecyclerViewAdapter mAdapter;
    private List<SettingModel> mList;


    @Override
    protected int getRootViewId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initView() {
        rv_personal = findViewById(R.id.rv_personal);
        tv_balance = findViewById(R.id.tv_personal_did_balance);
//        rl_language = findViewById(R.id.rl_personal_language);
//        rl_importwallet = findViewById(R.id.rl_personal_wallet);
//        tv_language_tips = findViewById(R.id.tv_personal_settings_language_tips);
//
//        rl_language.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(PersonalActivity.this, LanguageActivity.class);
//                startActivity(intent);
//            }
//        });
//        rl_importwallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(PersonalActivity.this, ImportWalletActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>(8);
        mList.add(new SettingModel(getString(R.string.nav_charges)));
        mList.add(new SettingModel(getString(R.string.nav_pay)));
        mList.add(new SettingModel(getString(R.string.me_recharge)));
        mList.add(new SettingModel(getString(R.string.me_withdraw)));
//        mList.add(new SettingModel(getString(R.string.me_records)));
        mAdapter = new PersonalRecyclerViewAdapter(this, mList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_personal.setLayoutManager(llm);
        rv_personal.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PersonalRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(PersonalActivity.this, ReceiveActivity.class);
                        break;
                    case 1:
                        intent.setClass(PersonalActivity.this, SendActivity.class);
                        break;
                    case 2:
                        intent.setClass(PersonalActivity.this, ReChargeActivity.class);
                        break;
                    case 3:
                        intent.setClass(PersonalActivity.this, WithDrawActivity.class);
                        break;
                    case 4:
                        intent.setClass(PersonalActivity.this, RecordsActivity.class);
                        break;
                }
                PersonalActivity.this.startActivity(intent);
            }
        });

        loadBalanceData();
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        String language = Utilty.getPreference(Constants.SP_KEY_APP_LANGUAGE, "");
//        if (TextUtils.isEmpty(language)) {
//            if (Locale.getDefault().getLanguage().contains("zh")) {
//                tv_language_tips.setText("中文（简体）");
//            } else {
//                tv_language_tips.setText("ENGLISH");
//            }
//        } else {
//            tv_language_tips.setText(language.equals("english") ? "ENGLISH" : "中文（简体）");
//        }
//    }

    private void loadBalanceData() {
        String url = String.format("%s%s%s", Urls.SERVER_DID, Urls.DID_BALANCE, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BalanceBean bean = new Gson().fromJson(response, BalanceBean.class);
                        String text = String.format("%s: %s ELA", getString(R.string.home_balance), bean.getResult());
                        tv_balance.setText(text);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("%s: %s ELA", getString(R.string.home_balance), "--");
                        tv_balance.setText(text);
                    }
                });
            }
        });
    }
}
