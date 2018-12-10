package com.ela.wallet.sdk.didlibrary.bean;

import java.util.List;

/**
 * Check out all transactions of login user.
 */

public class AllTxsBean {

    /**
     * status : 200
     * result : {"History":[{"Txid":"cd21b8729ca6173862034fb5515d395c25e5c7779330aa3634d7128435ddd4f4","Type":"sending","Value":"100","CreateTime":"1541755973","Height":157576,"Fee":"1","Inputs":["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"],"Outputs":["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT","EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"]},{"Txid":"1e368ad6af41fb626d35f6e2dac238b6b64bfadcf3d3f297919f81029e0027ff","Type":"spend","Value":"101000","CreateTime":"1542089418","Height":160171,"Fee":"100","Inputs":["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"],"Outputs":["EMHc9JSpxKWbTMf8gQDcWm7Tz1C5nQNA8Z","EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"]},{"Txid":"17649d6b4a98b1834f51b4a319568a89669a6e4ae89d33a8188c8743bb51b62d","Type":"income","Value":"8000000","CreateTime":"1542263232","Height":161512,"Fee":"1","Inputs":["EaVuwkuk9gMcCRX28FRqrmQZh3KUuXJnzL"],"Outputs":["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT","EaVuwkuk9gMcCRX28FRqrmQZh3KUuXJnzL"]}],"TotalNum":8}
     */

    private int status;
    private ResultBean result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * History : [{"Txid":"cd21b8729ca6173862034fb5515d395c25e5c7779330aa3634d7128435ddd4f4","Type":"sending","Value":"100","CreateTime":"1541755973","Height":157576,"Fee":"1","Inputs":["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"],"Outputs":["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT","EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"]},{"Txid":"1e368ad6af41fb626d35f6e2dac238b6b64bfadcf3d3f297919f81029e0027ff","Type":"spend","Value":"101000","CreateTime":"1542089418","Height":160171,"Fee":"100","Inputs":["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"],"Outputs":["EMHc9JSpxKWbTMf8gQDcWm7Tz1C5nQNA8Z","EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"]},{"Txid":"17649d6b4a98b1834f51b4a319568a89669a6e4ae89d33a8188c8743bb51b62d","Type":"income","Value":"8000000","CreateTime":"1542263232","Height":161512,"Fee":"1","Inputs":["EaVuwkuk9gMcCRX28FRqrmQZh3KUuXJnzL"],"Outputs":["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT","EaVuwkuk9gMcCRX28FRqrmQZh3KUuXJnzL"]}]
         * TotalNum : 8
         */

        private int TotalNum;
        private List<HistoryBean> History;

        public int getTotalNum() {
            return TotalNum;
        }

        public void setTotalNum(int TotalNum) {
            this.TotalNum = TotalNum;
        }

        public List<HistoryBean> getHistory() {
            return History;
        }

        public void setHistory(List<HistoryBean> History) {
            this.History = History;
        }

        public static class HistoryBean {
            /**
             * Txid : cd21b8729ca6173862034fb5515d395c25e5c7779330aa3634d7128435ddd4f4
             * Type : sending
             * Value : 100
             * CreateTime : 1541755973
             * Height : 157576
             * Fee : 1
             * Inputs : ["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"]
             * Outputs : ["EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT","EbAATdrW7gaomFY3SAy81rokqwqKA3EXbT"]
             */

            private String Txid;
            private String Type;
            private String Value;
            private String CreateTime;
            private int Height;
            private String Fee;
            private List<String> Inputs;
            private List<String> Outputs;

            public String getTxid() {
                return Txid;
            }

            public void setTxid(String Txid) {
                this.Txid = Txid;
            }

            public String getType() {
                return Type;
            }

            public void setType(String Type) {
                this.Type = Type;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String Value) {
                this.Value = Value;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }

            public int getHeight() {
                return Height;
            }

            public void setHeight(int Height) {
                this.Height = Height;
            }

            public String getFee() {
                return Fee;
            }

            public void setFee(String Fee) {
                this.Fee = Fee;
            }

            public List<String> getInputs() {
                return Inputs;
            }

            public void setInputs(List<String> Inputs) {
                this.Inputs = Inputs;
            }

            public List<String> getOutputs() {
                return Outputs;
            }

            public void setOutputs(List<String> Outputs) {
                this.Outputs = Outputs;
            }
        }
    }
}
