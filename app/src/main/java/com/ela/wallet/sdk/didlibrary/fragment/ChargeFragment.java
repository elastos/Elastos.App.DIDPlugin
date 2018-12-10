package com.ela.wallet.sdk.didlibrary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseFragment;
import com.ela.wallet.sdk.didlibrary.bean.BalanceBean;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.google.gson.Gson;
import com.qingmei2.library.encode.QRCodeEncoder;

public class ChargeFragment extends BaseFragment {

    private ImageView iv_qr;
    private TextView tv_copy;
    private TextView tv_reset;
    private TextView tv_balance;

    @Override
    protected void initView(View rootView, @Nullable Bundle savedInstanceState) {
        iv_qr = rootView.findViewById(R.id.iv_charge_qr);
        tv_copy = rootView.findViewById(R.id.tv_charge_copyaddress);
        tv_reset = rootView.findViewById(R.id.tv_charge_reset_address);
        tv_balance = rootView.findViewById(R.id.tv_charge_did_balance);

        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "copy success", Toast.LENGTH_SHORT).show();
            }
        });
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        initQrView();
        loadBalanceData();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_charge;
    }

    @Override
    protected void onFragmentResume() {

    }

    @Override
    protected void onFragmentPause() {

    }

    private void initQrView() {
        String address = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");
        if (!TextUtils.isEmpty(address)) {
            new QRCodeEncoder(mActivity).createQrCode2ImageView(address, iv_qr, R.drawable.icon_launcher);
            tv_copy.setText(address);
        }
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
