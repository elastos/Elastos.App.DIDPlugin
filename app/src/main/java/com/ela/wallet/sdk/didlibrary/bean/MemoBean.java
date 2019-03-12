package com.ela.wallet.sdk.didlibrary.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/1/3.
 */

public class MemoBean {

    /**
     * Tag : DID Property
     * Ver : 1.0
     * Status : 1
     * Properties : [{"Key":"imei","Value":"","Status":"1"}]
     */

    private String Tag;
    private String Ver;
    private String Status;
    private List<PropertiesBean> Properties;

    public String getTag() {
        return Tag;
    }

    public void setTag(String Tag) {
        this.Tag = Tag;
    }

    public String getVer() {
        return Ver;
    }

    public void setVer(String Ver) {
        this.Ver = Ver;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public List<PropertiesBean> getProperties() {
        return Properties;
    }

    public void setProperties(List<PropertiesBean> Properties) {
        this.Properties = Properties;
    }

    public static class PropertiesBean {
        /**
         * Key : imei
         * Value :
         * Status : 1
         */

        private String Key;
        private String Value;
        private String Status;

        public String getKey() {
            return Key;
        }

        public void setKey(String Key) {
            this.Key = Key;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }
    }
}
