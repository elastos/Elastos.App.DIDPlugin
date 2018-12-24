package com.ela.wallet.sdk.didlibrary.bean;

/**
 * Created by Administrator on 2018/12/23.
 */

public class GetDidBean {

    /**
     * result : [{"key":"key20181222","value":"value20181222"},{"key":"key20181224","value":"value20181224"}]
     * status : 200
     */

    private String result;
    private int status;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
