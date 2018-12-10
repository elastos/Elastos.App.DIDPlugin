package com.ela.wallet.sdk.didlibrary.activity;

import android.content.Intent;
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
import com.ela.wallet.sdk.didlibrary.bean.AllTxsBean;
import com.ela.wallet.sdk.didlibrary.bean.BalanceBean;
import com.ela.wallet.sdk.didlibrary.bean.RecordsModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.RecordsRecyclerViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends BaseActivity {

    private RelativeLayout rl_did;

    private TextView tv_balance;

//    private Button btn_expense;
//    private Button btn_income;
//    private RecyclerView rv_trans;
//    private RecordsRecyclerViewAdapter mAdapter;
//    private List<RecordsModel> mList;
//    private List<RecordsModel> mList1;
//    private List<RecordsModel> mList2;


    private RelativeLayout rl_finance;
    private RelativeLayout rl_record;

    private RelativeLayout rl_language;
    private TextView tv_language_tips;
    private RelativeLayout rl_importwallet;


    @Override
    protected void initView() {
        rl_did = findViewById(R.id.rl_home_did);
        tv_balance = findViewById(R.id.tv_home_did_balance);
//        btn_expense = findViewById(R.id.btn_home_trans_expense);
//        btn_income = findViewById(R.id.btn_home_trans_income);
        rl_did.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(HomeActivity.this, PersonalActivity.class);
//                startActivity(intent);
            }
        });
//        btn_expense.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn_income.setTextColor(getResources().getColor(R.color.textBlack));
//                btn_expense.setTextColor(getResources().getColor(R.color.appColor));
//                mAdapter.setData(mList2);
//            }
//        });
//        btn_income.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn_expense.setTextColor(getResources().getColor(R.color.textBlack));
//                btn_income.setTextColor(getResources().getColor(R.color.appColor));
//                mAdapter.setData(mList1);
//            }
//        });
//        rv_trans = findViewById(R.id.rv_home);


        rl_finance = findViewById(R.id.rl_personal_finance);
        rl_record = findViewById(R.id.rl_personal_record);

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
                intent.setClass(HomeActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });

        rl_language = findViewById(R.id.rl_personal_language);
        rl_importwallet = findViewById(R.id.rl_personal_wallet);
        tv_language_tips = findViewById(R.id.tv_personal_settings_language_tips);

        rl_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, LanguageActivity.class);
                startActivity(intent);
            }
        });
        rl_importwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, ImportWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initData() {
        //init trans title
//        btn_expense.setTextColor(getResources().getColor(R.color.appColor));
//        rv_trans.setLayoutManager(new LinearLayoutManager(this));
        //load wallet balance data
//        loadBalanceData();
        //load trans data
//        loadTransData();
//        loadLocalTransData();

//        mAdapter = new RecordsRecyclerViewAdapter(this, mList1);
//        rv_trans.setAdapter(mAdapter);
//        mAdapter.setData(mList2);

        tv_balance.setText(Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
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

//    private void loadLocalTransData() {
//        mList = new ArrayList<>();
//        mList1 = new ArrayList<>();
//        mList2 = new ArrayList<>();
//        mList.add(new RecordsModel("did1@did.com", "1585555555555", "02/12/2018"));
//        for(RecordsModel records : mList) {
////            if (getString(R.string.nav_charges).equals(records.getType())) {
//                mList1.add(records);
////            } else if(getString(R.string.nav_pay).equals(records.getType())) {
//                mList2.add(records);
////            }
//        }
//    }

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
}
