package com.ela.wallet.sdk.didlibrary.bean;

import java.util.List;

import static com.ela.wallet.sdk.didlibrary.bean.MemoBean.DidAppDirPath;
import static com.ela.wallet.sdk.didlibrary.bean.MemoBean.DidAppId;

/**
 * Created by Administrator on 2018/12/23.
 */

public class SetDidBean {

    /**
     * Tag : DID Property
     * Ver : 1.0
     * Status : 1
     * Properties : [{"Key":"key20181224","Value":"value20181224","Status":1}]
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
         * Key : key20181224
         * Value : value20181224
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
            if(key.startsWith(DidAppDirPath) == false) {
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
}
