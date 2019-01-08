package com.ela.wallet.sdk.didlibrary.bean;

import android.support.annotation.NonNull;

public class RecordsModel implements Comparable<RecordsModel>{

    private String type;
    private String time;
    private String value;

    public RecordsModel() {
    }

    public RecordsModel(String type, String time, String value) {
        this.type = type;
        this.time = time;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(@NonNull RecordsModel recordsModel) {
        return recordsModel.getTime().compareTo(this.getTime());
    }
}
