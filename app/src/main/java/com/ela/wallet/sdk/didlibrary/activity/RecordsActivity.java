package com.ela.wallet.sdk.didlibrary.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TableLayout;
import android.widget.Toast;

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

public class RecordsActivity extends BaseActivity {

    private TabLayout mTab;
    private RecyclerView mRv;
    private RecordsRecyclerViewAdapter mAdapter;
    private List<RecordsModel> mList;
    private List<RecordsModel> mList1;
    private List<RecordsModel> mList2;
    private List<RecordsModel> mList3;
    private List<RecordsModel> mList4;

    @Override
    protected int getRootViewId() {
        return R.layout.activity_records;
    }

    @Override
    public String getTitleText() {
        return getString(R.string.me_records);
    }

    @Override
    protected void initView() {
        mTab = findViewById(R.id.tab_records);
        mRv = findViewById(R.id.rv_records);
    }

    @Override
    protected void initData() {
        String[] tabs = {
                getString(R.string.nav_all),
                getString(R.string.nav_record1),
                getString(R.string.nav_record2),
                getString(R.string.nav_record3),
                getString(R.string.nav_record4)
        };
        mList = new ArrayList<>();
        mList1 = new ArrayList<>();//DID->DID
        mList2 = new ArrayList<>();//DID->ELA
        mList3 = new ArrayList<>();//ELA->ELA
        mList4 = new ArrayList<>();//ELA->DID
//        //todo:
//        mList.add(new RecordsModel(tabs[1], "20/11/2018", "+1.5 ELA"));
//        mList.add(new RecordsModel(tabs[2], "20/11/2018", "-0.5 ELA"));
//        mList.add(new RecordsModel(tabs[3], "15/11/2018", "+2 ELA"));
//        mList.add(new RecordsModel(tabs[4], "21/11/2018", "-1 ELA"));
//        for(RecordsModel records : mList) {
//            if (tabs[1].equals(records.getType())) {
//                mList1.add(records);
//            } else if(tabs[2].equals(records.getType())) {
//                mList2.add(records);
//            } else if(tabs[3].equals(records.getType())) {
//                mList3.add(records);
//            } else {
//                mList4.add(records);
//            }
//        }
        mAdapter = new RecordsRecyclerViewAdapter(this, mList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRv.setLayoutManager(llm);
        mRv.setAdapter(mAdapter);
        mAdapter.setData(mList);

        for(int k=0;k<5;k++) {
            mTab.addTab(mTab.newTab());
            TabLayout.Tab tabItem = mTab.getTabAt(k);
            tabItem.setText(tabs[k]);
        }
        mTab.setTabMode(TabLayout.MODE_SCROLLABLE);

        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 1:
                        mAdapter.setData(mList1);
                        break;
                    case 2:
                        mAdapter.setData(mList2);
                        break;
                    case 3:
                        mAdapter.setData(mList3);
                        break;
                    case 4:
                        mAdapter.setData(mList4);
                        break;
                    case 0:
                        mAdapter.setData(mList);
                    default:
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        loadDidTxData();
//        loadElaTxData();
    }

    private void loadDidTxData() {
//        String url = String.format("%s%s%s", Urls.SERVER_DID_HISTORY, Urls.DID_HISTORY, "ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4");
        String url = String.format("%s%s%s", Urls.SERVER_DID_HISTORY, Urls.DID_HISTORY, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AllTxsBean allTxsBean = new Gson().fromJson(response, AllTxsBean.class);
                        if (allTxsBean.getStatus() != 200) {
                            return;
                        }
                        if (allTxsBean.getResult().getTotalNum() > 0) {
                            mList.clear();
                            mList1.clear();
                            mList2.clear();
                            mList4.clear();
                            for (AllTxsBean.ResultBean.HistoryBean historyBean : allTxsBean.getResult().getHistory()) {
                                if ("spend".equals(historyBean.getType()) && "WithdrawFromSideChain".equals(historyBean.getTxType())) {
                                    mList2.add(new RecordsModel(getString(R.string.nav_record2), historyBean.getCreateTime()+"", "-" + historyBean.getValue()));
                                    mList.add(new RecordsModel(getString(R.string.nav_record2), historyBean.getCreateTime()+"", "-" + historyBean.getValue()));
                                } else if ("income".equals(historyBean.getType()) && "RechargeToSideChain".equals(historyBean.getTxType())) {
                                    mList4.add(new RecordsModel(getString(R.string.nav_record4), historyBean.getCreateTime()+"", "+" + historyBean.getValue()));
                                    mList.add(new RecordsModel(getString(R.string.nav_record4), historyBean.getCreateTime()+"", "+" + historyBean.getValue()));
                                } else {
                                    if ("spend".equals(historyBean.getType())) {
                                        mList1.add(new RecordsModel(getString(R.string.nav_record1), historyBean.getCreateTime()+"", "-" + historyBean.getValue()));
                                        mList.add(new RecordsModel(getString(R.string.nav_record1), historyBean.getCreateTime()+"", "-" + historyBean.getValue()));
                                    } else {
                                        mList1.add(new RecordsModel(getString(R.string.nav_record1), historyBean.getCreateTime()+"", "+" + historyBean.getValue()));
                                        mList.add(new RecordsModel(getString(R.string.nav_record1), historyBean.getCreateTime()+"", "+" + historyBean.getValue()));
                                    }
                                }
//                                String prefix = historyBean.getType().equals("income") ? "+" : "-";
//                                mList.add(new RecordsModel(historyBean.getType(), historyBean.getCreateTime()+"", prefix + historyBean.getValue()));
                            }
//                            mAdapter.setData(mList);
                            loadElaTxData();
                        }
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

    private void loadElaTxData() {
        String url = String.format("%s%s%s", Urls.SERVER_WALLET_HISTORY, Urls.ELA_HISTORY, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AllTxsBean allTxsBean = new Gson().fromJson(response, AllTxsBean.class);
                        if (allTxsBean.getStatus() != 200) {
                            return;
                        }
                        if (allTxsBean.getResult().getTotalNum() > 0) {
                            mList3.clear();
                            for (AllTxsBean.ResultBean.HistoryBean historyBean : allTxsBean.getResult().getHistory()) {
                                String prefix = historyBean.getType().equals("income") ? "+" : "-";
                                mList3.add(new RecordsModel(getString(R.string.nav_record3), historyBean.getCreateTime()+"", prefix + historyBean.getValue()));
                                mList.add(new RecordsModel(getString(R.string.nav_record3), historyBean.getCreateTime()+"", prefix + historyBean.getValue()));
                            }
                        }
                        parseTransData();
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

    private void parseTransData() {
        mAdapter.setData(mList);
    }
}
