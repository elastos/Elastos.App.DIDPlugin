package com.ela.wallet.sdk.didlibrary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseFragment;
import com.ela.wallet.sdk.didlibrary.bean.AllTxsBean;
import com.ela.wallet.sdk.didlibrary.bean.BalanceBean;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.google.gson.Gson;

public class HomeFragment extends BaseFragment {

    private TextView tv_balance;

    private Button btn_expense;
    private Button btn_income;
    private RecyclerView rv_trans;

    @Override
    protected void initView(View rootView, @Nullable Bundle savedInstanceState) {
        tv_balance = rootView.findViewById(R.id.tv_home_did_balance);
        btn_expense = rootView.findViewById(R.id.btn_home_trans_expense);
        btn_income = rootView.findViewById(R.id.btn_home_trans_income);
        btn_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_income.setTextColor(getResources().getColor(R.color.textBlack));
                btn_expense.setTextColor(getResources().getColor(R.color.appColor));
            }
        });
        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_expense.setTextColor(getResources().getColor(R.color.textBlack));
                btn_income.setTextColor(getResources().getColor(R.color.appColor));
            }
        });
        rv_trans = rootView.findViewById(R.id.rv_home);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //init trans title
        btn_expense.setTextColor(getResources().getColor(R.color.appColor));
        //load wallet balance data
        loadBalanceData();
        //load trans data
        loadTransData();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onFragmentResume() {

    }

    @Override
    protected void onFragmentPause() {

    }


    private void loadBalanceData() {
        //todo : change to really address
        String url = String.format("%s%s%s", Urls.SERVER_DID, Urls.DID_BALANCE, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                mActivity.runOnUiThread(new Runnable() {
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
                mActivity.runOnUiThread(new Runnable() {
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
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AllTxsBean bean = new Gson().fromJson(response, AllTxsBean.class);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }
}

