package com.ela.wallet.sdk.didlibrary.activity;

//import android.support.design.widget.TabLayout;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.base.BaseActivity;
import com.ela.wallet.sdk.didlibrary.bean.AllTxsBean;
import com.ela.wallet.sdk.didlibrary.bean.RecordsModel;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.ela.wallet.sdk.didlibrary.widget.RecordsRecyclerViewAdapter;
import com.ela.wallet.sdk.didlibrary.widget.SweetAlertDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordsActivity extends BaseActivity {

    private ListView mTab;
    private ListView mRv;
    private RecordsRecyclerViewAdapter mAdapter;
    private List<RecordsModel> mList;
    private List<RecordsModel> mList1;
    private List<RecordsModel> mList2;
    private List<RecordsModel> mList3;
    private List<RecordsModel> mList4;

    private SweetAlertDialog mDialog;

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
        mTab = (ListView) findViewById(R.id.tab_records);
        mRv = (ListView) findViewById(R.id.rv_records);
    }

    @Override
    protected void initData() {
        final String[] tabs = {
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
        //TODO houhong
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        mRv.setLayoutManager(llm);
        mRv.setAdapter(mAdapter);
        mAdapter.setData(mList);


//        for(int k=0;k<5;k++) {
//
//            mTab.addTab(mTab.newTab());
//            TabLayout.Tab tabItem = mTab.getTabAt(k);
//            tabItem.setText(tabs[k]);
//        }
//        mTab.setTabMode(TabLayout.MODE_SCROLLABLE);

        mTab.setAdapter(new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int i) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                  TextView tv = tv = new TextView(RecordsActivity.this);
                  tv.setText(tabs[i]);
                  return tv;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return tabs.length;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });

        mTab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        });

//        mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
//        {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                switch (position) {
//                    case 1:
//                        mAdapter.setData(mList1);
//                        break;
//                    case 2:
//                        mAdapter.setData(mList2);
//                        break;
//                    case 3:
//                        mAdapter.setData(mList3);
//                        break;
//                    case 4:
//                        mAdapter.setData(mList4);
//                        break;
//                    case 0:
//                        mAdapter.setData(mList);
//                    default:
//                }
//            }
//
//        });

        loadDidTxData();
//        loadElaTxData();
    }

    private void loadDidTxData() {
        if (mDialog == null) {
            mDialog = new SweetAlertDialog(this);
        }
        mDialog.setTitle(getString(R.string.loading));
        mDialog.show();

//        String url = String.format("%s%s%s", Urls.SERVER_DID_HISTORY, Urls.DID_HISTORY, "ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4");
        String url = String.format("%s%s%s", Urls.SERVER_DID_HISTORY, Urls.DID_HISTORY, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AllTxsBean allTxsBean = new Gson().fromJson(response, AllTxsBean.class);
                        if (allTxsBean.getStatus() != 200 || allTxsBean.getResult().getTotalNum() == 0) {
                            if (mDialog != null && mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            return;
                        }
                        mList.clear();
                        if (allTxsBean.getResult().getTotalNum() > 0) {
                            mList1.clear();
                            mList2.clear();
                            mList4.clear();
                            for (AllTxsBean.ResultBean.HistoryBean historyBean : allTxsBean.getResult().getHistory()) {
                                if ("spend".equals(historyBean.getType()) && "TransferCrossChainAsset".equals(historyBean.getTxType())) {
                                    mList2.add(new RecordsModel(getString(R.string.nav_record2), historyBean.getCreateTime()+"", "-" + (historyBean.getValue() - historyBean.getFee()), historyBean.getFee()));
                                    mList.add(new RecordsModel(getString(R.string.nav_record2), historyBean.getCreateTime()+"", "-" + (historyBean.getValue() - historyBean.getFee()), historyBean.getFee()));
                                } else if ("income".equals(historyBean.getType()) && "RechargeToSideChain".equals(historyBean.getTxType())) {
                                    mList4.add(new RecordsModel(getString(R.string.nav_record4), historyBean.getCreateTime()+"", "+" + historyBean.getValue(), 0));
                                    mList.add(new RecordsModel(getString(R.string.nav_record4), historyBean.getCreateTime()+"", "+" + historyBean.getValue(), 0));
                                } else {
                                    if ("spend".equals(historyBean.getType())) {
                                        mList1.add(new RecordsModel(getString(R.string.nav_record1), historyBean.getCreateTime()+"", "-" + (historyBean.getValue() - historyBean.getFee()), historyBean.getFee()));
                                        mList.add(new RecordsModel(getString(R.string.nav_record1), historyBean.getCreateTime()+"", "-" + (historyBean.getValue() - historyBean.getFee()), historyBean.getFee()));
                                    } else {
                                        mList1.add(new RecordsModel(getString(R.string.nav_record1), historyBean.getCreateTime()+"", "+" + historyBean.getValue(), 0));
                                        mList.add(new RecordsModel(getString(R.string.nav_record1), historyBean.getCreateTime()+"", "+" + historyBean.getValue(), 0));
                                    }
                                }
//                                String prefix = historyBean.getType().equals("income") ? "+" : "-";
//                                mList.add(new RecordsModel(historyBean.getType(), historyBean.getCreateTime()+"", prefix + historyBean.getValue()));
                            }
//                            mAdapter.setData(mList);
                        }
                        loadElaTxData();
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

    private void loadElaTxData() {
        String url = String.format("%s%s%s", Urls.SERVER_WALLET_HISTORY, Urls.ELA_HISTORY, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        AllTxsBean allTxsBean = new Gson().fromJson(response, AllTxsBean.class);
                        if (allTxsBean.getStatus() != 200) {
                            return;
                        }
                        if (allTxsBean.getResult().getTotalNum() > 0) {
                            mList3.clear();
                            for (AllTxsBean.ResultBean.HistoryBean historyBean : allTxsBean.getResult().getHistory()) {
                                String prefix = historyBean.getType().equals("income") ? "+" : "-";
                                if (!historyBean.getTxType().equals("WithdrawFromSideChain") && !historyBean.getTxType().equals("TransferCrossChainAsset")) {
                                    if ("income".equals(historyBean.getType())) {
                                        mList3.add(new RecordsModel(getString(R.string.nav_record3), historyBean.getCreateTime()+"", prefix + historyBean.getValue(), 0));
                                        mList.add(new RecordsModel(getString(R.string.nav_record3), historyBean.getCreateTime()+"", prefix + historyBean.getValue(), 0));
                                    } else {
                                        mList3.add(new RecordsModel(getString(R.string.nav_record3), historyBean.getCreateTime()+"", prefix + (historyBean.getValue() - historyBean.getFee()), historyBean.getFee()));
                                        mList.add(new RecordsModel(getString(R.string.nav_record3), historyBean.getCreateTime()+"", prefix + (historyBean.getValue() - historyBean.getFee()), historyBean.getFee()));
                                    }
                                }
                            }
                        }
                        parseTransData();
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
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

    private void parseTransData() {
        Collections.sort(mList);
        Collections.reverse(mList1);
        Collections.reverse(mList2);
        Collections.reverse(mList3);
        Collections.reverse(mList4);
//        mTab.getTabAt(0).select();
        mAdapter.setData(mList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
