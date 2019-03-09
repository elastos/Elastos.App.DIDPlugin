package com.ela.wallet.sdk.didlibrary.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
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
import com.ela.wallet.sdk.didlibrary.widget.DidAlertDialog;
import com.ela.wallet.sdk.didlibrary.widget.PersonalRecyclerViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PersonalActivity extends BaseActivity {

    private RelativeLayout rl_did;
    private RelativeLayout rl_ela;
    private TextView tv_did;
    private TextView tv_ela;

    private TextView tv_balance;
//    private RelativeLayout rl_language;
//    private TextView tv_language_tips;
//    private RelativeLayout rl_importwallet;

    private ListView rv_personal;
    private PersonalRecyclerViewAdapter mAdapter;
    private List<SettingModel> mList;

    private ScheduledExecutorService scheduledThreadPool;


    @Override
    protected int getRootViewId() {
        return R.layout.activity_personal;
    }

    @Override
    public String getTitleText() {
        return getString(R.string.me_finance);
    }

    @Override
    protected void initView() {
        rl_did = (RelativeLayout) findViewById(R.id.rl_did);
        rl_ela = (RelativeLayout) findViewById(R.id.rl_ela);
        tv_did = (TextView) findViewById(R.id.did_balance);
        tv_ela = (TextView) findViewById(R.id.ela_balance);

        rv_personal = (ListView) findViewById(R.id.rv_personal);
        tv_balance = (TextView) findViewById(R.id.tv_personal_did_balance);

        rl_did.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utilty.isBacked()) {
                    showBackupDialog();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(PersonalActivity.this, ReceiveActivity.class);
                intent.putExtra(Constants.INTENT_PARAM_KEY_QRCODE_FROM, "did");
                startActivity(intent);
            }
        });
        rl_ela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utilty.isBacked()) {
                    showBackupDialog();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(PersonalActivity.this, ReceiveActivity.class);
                intent.putExtra(Constants.INTENT_PARAM_KEY_QRCODE_FROM, "ela");
                startActivity(intent);
            }
        });
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
        scheduledThreadPool = Executors.newScheduledThreadPool(1);
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>(8);
        mList.add(new SettingModel(R.drawable.list_icon_send, getString(R.string.nav_pay)));
        mList.add(new SettingModel(R.drawable.list_icon_withdraw, getString(R.string.me_withdraw)));
        mList.add(new SettingModel(R.drawable.list_icon_receive, getString(R.string.nav_charges)));
        mList.add(new SettingModel(R.drawable.list_icon_recharge, getString(R.string.me_recharge)));
        mList.add(new SettingModel(R.drawable.list_icon_record, getString(R.string.me_records)));
        mAdapter = new PersonalRecyclerViewAdapter(this, mList);
        //TODO houhong
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        rv_personal.setLayoutManager(llm);
        rv_personal.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PersonalRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (!Utilty.isBacked()) {
                    showBackupDialog();
                    return;
                }
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(PersonalActivity.this, SendActivity.class);
                        break;
                    case 1:
                        intent.setClass(PersonalActivity.this, WithDrawActivity.class);
                        break;
                    case 2:
                        intent.setClass(PersonalActivity.this, Ela2ElaActivity.class);
                        break;
                    case 3:
                        intent.setClass(PersonalActivity.this, ReChargeActivity.class);
                        break;
                    case 4:
                        intent.setClass(PersonalActivity.this, RecordsActivity.class);
                        break;
                }
                PersonalActivity.this.startActivity(intent);
            }
        });

        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                loadDidBalanceData();
                loadElaBalanceData();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        loadDidBalanceData();
//        loadElaBalanceData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdown();
        }
    }

    private void loadDidBalanceData() {
        String url = String.format("%s%s%s", Urls.SERVER_DID, Urls.DID_BALANCE, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BalanceBean bean = new Gson().fromJson(response, BalanceBean.class);
                        String text = String.format("%s ELA", bean.getResult());
                        tv_did.setText(text);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("%s ELA", "--");
                        tv_did.setText(text);
                    }
                });
            }
        });
    }

    private void loadElaBalanceData() {
        String url = String.format("%s%s%s", Urls.SERVER_WALLET, Urls.ELA_BALANCE, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BalanceBean bean = new Gson().fromJson(response, BalanceBean.class);
                        String text = String.format("%s ELA", bean.getResult());
                        tv_ela.setText(text);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("%s ELA", "--");
                        tv_ela.setText(text);
                    }
                });
            }
        });
    }

    private void showBackupDialog() {
        new DidAlertDialog(this)
                .setTitle(getString(R.string.send_backup))
                .setMessage(getString(R.string.send_tips))
                .setMessageGravity(Gravity.LEFT)
                .setRightButton(getString(R.string.btn_backup), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(PersonalActivity.this, BackupTipsActivity.class);
                        PersonalActivity.this.startActivity(intent);
                    }
                })
                .show();
    }
}
