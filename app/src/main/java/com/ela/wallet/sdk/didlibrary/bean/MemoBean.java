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
            String key = this.Key;
            if(key.startsWith(DidAppDirPath) == true) {
                key = this.Key.replace(DidAppDirPath, "");
            }
            return key;
        }

        public void setKey(String key) {
            this.Key = key;
            if(Key.startsWith(DidAppDirPath) == false) {
                this.Key = DidAppDirPath + key;
            }
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

    public static final String DidAppName = "org.elastos.debug.didagent";
    public static final String DidAppId = "DC92DEC59082610D1D4698F42965381EBBC4EF7DBDA08E4B3894D530608A64AAA65BB82A170FBE16F04B2AF7B25D88350F86F58A7C1F55CC29993B4C4C29E405";
    public static final String DidAppDirPath = "Apps/" + DidAppId + "/";
}
