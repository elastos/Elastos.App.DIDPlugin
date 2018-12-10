package com.ela.wallet.sdk.didlibrary.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/11/20.
 * http cct request return data
 */

public class CctBean {

    /**
     * result : {"Transactions":[{"UTXOInputs":[{"address":"ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4","txid":"5b9f3a003dd2898bc8b1485ba17519e3d53ee2d10f99897cabcf443d3543b55e","index":1,"privateKey":"840d6c631e3d612aa624dae2d7f6d354e58135a7a6cb16ed6dd264b7d104aae7"}],"CrossChainAsset":[{"amount":100000000,"address":"EeEfKiN9tVtqFCJxHGuRj93CKywDBCxJat"}],"Fee":20000,"Outputs":[{"amount":100010000,"address":"XWr5fdmvTju7Br6i6SM2x9tK3R2U3WaXXT"},{"amount":9799960000,"address":"ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4"}]}]}
     * status : 200
     */

    private ResultBean result;
    private int status;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class ResultBean {
        private List<TransactionsBean> Transactions;

        public List<TransactionsBean> getTransactions() {
            return Transactions;
        }

        public void setTransactions(List<TransactionsBean> Transactions) {
            this.Transactions = Transactions;
        }

        public static class TransactionsBean {
            /**
             * UTXOInputs : [{"address":"ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4","txid":"5b9f3a003dd2898bc8b1485ba17519e3d53ee2d10f99897cabcf443d3543b55e","index":1,"privateKey":"840d6c631e3d612aa624dae2d7f6d354e58135a7a6cb16ed6dd264b7d104aae7"}]
             * CrossChainAsset : [{"amount":100000000,"address":"EeEfKiN9tVtqFCJxHGuRj93CKywDBCxJat"}]
             * Fee : 20000
             * Outputs : [{"amount":100010000,"address":"XWr5fdmvTju7Br6i6SM2x9tK3R2U3WaXXT"},{"amount":9799960000,"address":"ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4"}]
             */

            private int Fee;
            private String Memo;
            private List<UTXOInputsBean> UTXOInputs;
            private List<CrossChainAssetBean> CrossChainAsset;
            private List<OutputsBean> Outputs;

            public int getFee() {
                return Fee;
            }

            public void setFee(int Fee) {
                this.Fee = Fee;
            }

            public String getMemo() {
                return Memo;
            }

            public void setMemo(String memo) {
                Memo = memo;
            }

            public List<UTXOInputsBean> getUTXOInputs() {
                return UTXOInputs;
            }

            public void setUTXOInputs(List<UTXOInputsBean> UTXOInputs) {
                this.UTXOInputs = UTXOInputs;
            }

            public List<CrossChainAssetBean> getCrossChainAsset() {
                return CrossChainAsset;
            }

            public void setCrossChainAsset(List<CrossChainAssetBean> CrossChainAsset) {
                this.CrossChainAsset = CrossChainAsset;
            }

            public List<OutputsBean> getOutputs() {
                return Outputs;
            }

            public void setOutputs(List<OutputsBean> Outputs) {
                this.Outputs = Outputs;
            }

            public static class UTXOInputsBean {
                /**
                 * address : ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4
                 * txid : 5b9f3a003dd2898bc8b1485ba17519e3d53ee2d10f99897cabcf443d3543b55e
                 * index : 1
                 * privateKey : 840d6c631e3d612aa624dae2d7f6d354e58135a7a6cb16ed6dd264b7d104aae7
                 */

                private String address;
                private String txid;
                private int index;
                private String privateKey;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getTxid() {
                    return txid;
                }

                public void setTxid(String txid) {
                    this.txid = txid;
                }

                public int getIndex() {
                    return index;
                }

                public void setIndex(int index) {
                    this.index = index;
                }

                public String getPrivateKey() {
                    return privateKey;
                }

                public void setPrivateKey(String privateKey) {
                    this.privateKey = privateKey;
                }
            }

            public static class CrossChainAssetBean {
                /**
                 * amount : 100000000
                 * address : EeEfKiN9tVtqFCJxHGuRj93CKywDBCxJat
                 */

                private long amount;
                private String address;

                public long getAmount() {
                    return amount;
                }

                public void setAmount(int amount) {
                    this.amount = amount;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }
            }

            public static class OutputsBean {
                /**
                 * amount : 100010000
                 * address : XWr5fdmvTju7Br6i6SM2x9tK3R2U3WaXXT
                 */

                private long amount;
                private String address;

                public long getAmount() {
                    return amount;
                }

                public void setAmount(int amount) {
                    this.amount = amount;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }
            }
        }
    }
}
