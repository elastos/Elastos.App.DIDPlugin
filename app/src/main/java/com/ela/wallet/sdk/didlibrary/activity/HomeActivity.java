package com.ela.wallet.sdk.didlibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.bean.AllTxsBean;
import com.ela.wallet.sdk.didlibrary.bean.BalanceBean;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.ela.wallet.sdk.didlibrary.utils.GetVersionInfo;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.google.gson.Gson;

import java.util.Locale;

public class HomeActivity extends BaseActivity {

    private RelativeLayout rl_did;

    private TextView tv_balance;
    private TextView tv_version;

    private RelativeLayout rl_finance;
    private RelativeLayout rl_record;

    private RelativeLayout rl_language;
    private TextView tv_language_tips;
    private RelativeLayout rl_importwallet;


    @Override
    protected void initView() {
        rl_did = (RelativeLayout) findViewById(R.id.rl_home_did);
        tv_balance = (TextView) findViewById(R.id.tv_home_did_balance);
        tv_version = (TextView) findViewById(R.id.tv_version);

        rl_finance = (RelativeLayout)findViewById(R.id.rl_personal_finance);
        rl_record = (RelativeLayout)findViewById(R.id.rl_personal_record);

        rl_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        rl_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });

        rl_language = (RelativeLayout)findViewById(R.id.rl_personal_language);
        rl_importwallet = (RelativeLayout)findViewById(R.id.rl_personal_wallet);
        tv_language_tips = (TextView) findViewById(R.id.tv_personal_settings_language_tips);

        rl_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, LanguageActivity.class);
                startActivityForResult(intent, Constants.INTENT_REQUEST_CODE_LANGUAGE);
            }
        });
        rl_importwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, ImportWalletActivity.class);
                startActivityForResult(intent, Constants.INTENT_REQUEST_CODE_IMPORT);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initData() {
        Utilty.setContext(this);
        tv_balance.setText(Utilty.getPreference(Constants.SP_KEY_DID, ""));
        tv_version.append(GetVersionInfo.getMetaData());
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_home;
    }

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

    private void loadTransData() {
        //todo : change to really addres
        String url = String.format("%s%s%s", Urls.SERVER_DID_HISTORY, Urls.DID_HISTORY, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AllTxsBean bean = new Gson().fromJson(response, AllTxsBean.class);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String language = Utilty.getPreference(Constants.SP_KEY_APP_LANGUAGE, "");
        if (TextUtils.isEmpty(language)) {
            if (Locale.getDefault().getLanguage().contains("zh")) {
                tv_language_tips.setText("中文（简体）");
            } else {
                tv_language_tips.setText("ENGLISH");
            }
        } else {
            tv_language_tips.setText(language.equals("english") ? "ENGLISH" : "中文（简体）");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_REQUEST_CODE_LANGUAGE &&
                resultCode == Activity.RESULT_OK) {
            finish();
            startActivity(getIntent());
        } else if (requestCode == Constants.INTENT_REQUEST_CODE_IMPORT &&
                resultCode == Activity.RESULT_OK) {
            tv_balance.setText(Utilty.getPreference(Constants.SP_KEY_DID, ""));
        }
    }
}
