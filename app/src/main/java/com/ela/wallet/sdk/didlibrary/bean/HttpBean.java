package com.ela.wallet.sdk.didlibrary.bean;

import android.view.View;

/**
 * Created by Administrator on 2018/12/01.
 */

public class HttpBean {

    /**
     * result : Not Enough UTXO
     * status : 400
     */

    private Object result;
    private int status;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
