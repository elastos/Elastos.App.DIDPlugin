package com.ela.wallet.sdk.didlibrary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.activity.ImportWalletActivity;
import com.ela.wallet.sdk.didlibrary.activity.LanguageActivity;
import com.ela.wallet.sdk.didlibrary.activity.ReChargeActivity;
import com.ela.wallet.sdk.didlibrary.activity.WithDrawActivity;
import com.ela.wallet.sdk.didlibrary.base.BaseFragment;
import com.ela.wallet.sdk.didlibrary.bean.BalanceBean;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.google.gson.Gson;

public class PersonalFragment extends BaseFragment {

    private TextView tv_balance;
    private Button btn_recharge;
    private Button btn_withdraw;
    private RelativeLayout rl_history;
    private RelativeLayout rl_language;
    private RelativeLayout rl_importwallet;

    @Override
    protected void initView(View rootView, @Nullable Bundle savedInstanceState) {
        tv_balance = rootView.findViewById(R.id.tv_personal_did_balance);
        btn_recharge = rootView.findViewById(R.id.btn_personal_trans_recharge);
        btn_withdraw = rootView.findViewById(R.id.btn_personal_trans_withdraw);
        rl_history = rootView.findViewById(R.id.rl_personal_history);
        rl_language = rootView.findViewById(R.id.rl_personal_language);
        rl_importwallet = rootView.findViewById(R.id.rl_personal_wallet);

        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mActivity, ReChargeActivity.class);
                mActivity.startActivity(intent);
            }
        });
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mActivity, WithDrawActivity.class);
                mActivity.startActivity(intent);
            }
        });
        rl_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        rl_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mActivity, LanguageActivity.class);
                startActivity(intent);
            }
        });
        rl_importwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mActivity, ImportWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        loadBalanceData();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_personal;
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

}
