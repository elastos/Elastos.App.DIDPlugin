package com.ela.wallet.sdk.didlibrary.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ela.wallet.sdk.didlibrary.R;
import com.ela.wallet.sdk.didlibrary.activity.ScanActivity;
import com.ela.wallet.sdk.didlibrary.base.BaseFragment;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.utils.LogUtil;

import java.util.concurrent.BrokenBarrierException;

public class PayFragment extends BaseFragment {

    private EditText et_scan_address;
    private ImageView iv_scan;

    @Override
    protected void initView(View rootView, @Nullable Bundle savedInstanceState) {
        et_scan_address = rootView.findViewById(R.id.et_scan_address);
        iv_scan = rootView.findViewById(R.id.iv_scan);

        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ScanActivity.class);
                startActivityForResult(intent, Constants.INTENT_REQUEST_CODE_SCAN);
            }
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_pay;
    }

    @Override
    protected void onFragmentResume() {

    }

    @Override
    protected void onFragmentPause() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK && data != null) {
            String result = data.getStringExtra(Constants.INTENT_PARAM_KEY_SCANRESUTL);
            et_scan_address.setText(result);
        }
    }
}
