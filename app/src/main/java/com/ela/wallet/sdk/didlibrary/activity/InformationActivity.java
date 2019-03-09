package com.ela.wallet.sdk.didlibrary.activity;

import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.bean.GetDidBean;
import com.ela.wallet.sdk.didlibrary.bean.RecordsModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.ela.wallet.sdk.didlibrary.utils.LogUtil;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.RecordsRecyclerViewAdapter;
import com.ela.wallet.sdk.didlibrary.widget.SweetAlertDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

import fi.iki.elonen.NanoHTTPD;

public class InformationActivity extends BaseActivity {

    private TextView tv_balance;

    private ListView rv_trans;
    private RecordsRecyclerViewAdapter mAdapter;
    private List<RecordsModel> mList;

    private SweetAlertDialog mDialog;

    @Override
    protected int getRootViewId() {
        return R.layout.activity_information;
    }

    @Override
    protected void initView() {
        tv_balance = (TextView) findViewById(R.id.tv_home_did_balance);
        rv_trans =(ListView) findViewById(R.id.rv_home);
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        tv_balance.setText(Utilty.getPreference(Constants.SP_KEY_DID, ""));
        //TODO houhong
//        rv_trans.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecordsRecyclerViewAdapter(this, mList);
        rv_trans.setAdapter(mAdapter);
        loadInfoData();
    }

    @Override
    public String getTitleText() {
        return getString(R.string.me_information);
    }

    private void loadInfoData() {
        if (mDialog == null) {
            mDialog = new SweetAlertDialog(this);
        }
        mDialog.setTitle(getString(R.string.loading));
        mDialog.show();

        String url = String.format("%s%s%s", Urls.SERVER_DID, Urls.DID_GETDID, Utilty.getPreference(Constants.SP_KEY_DID, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.d("loadInfoData:response=" + response);
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }

                        GetDidBean bean = new Gson().fromJson(response, GetDidBean.class);
                        if (bean.getStatus() != 200 || TextUtils.isEmpty(bean.getResult().trim())) return;
                        try {
                            JSONArray jsonArray = new JSONArray(bean.getResult());
                            for(int k=0;k<jsonArray.length();k++) {
                                String key = jsonArray.getJSONObject(k).getString("key");
                                String value = jsonArray.getJSONObject(k).getString("value");
                                if (TextUtils.isEmpty(key) && TextUtils.isEmpty(value)) continue;
//                                if ("imei".equals(key.trim().toLowerCase())) continue;
                                mList.add(new RecordsModel(key, null, value));
                            }
                            mAdapter.setData(mList);
                        } catch (Exception e) {
                            LogUtil.e(e.getMessage());
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
