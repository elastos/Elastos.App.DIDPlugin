package com.ela.wallet.sdk.didlibrary.utils;

import android.content.Context;
import android.text.TextUtils;

import com.ela.wallet.sdk.didlibrary.bean.CctBean;
import com.ela.wallet.sdk.didlibrary.bean.HttpBean;
import com.ela.wallet.sdk.didlibrary.bean.MemoBean;
import com.ela.wallet.sdk.didlibrary.bean.SetDidBean;
import com.ela.wallet.sdk.didlibrary.callback.TransCallback;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import org.elastos.sdk.keypair.ElastosKeypair;
import org.elastos.sdk.keypair.ElastosKeypairDID;
import org.elastos.sdk.keypair.ElastosKeypairSign;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DidLibrary {

    private static Context mContext;
    private static ElastosKeypair.Data mSeed;
    private static int mSeedLen;

    private static String mPrivateKey;

    public static void openLog(boolean open) {
        if (open) {
            LogUtil.setLogLevel(LogUtil.VERBOSE);
        }
    }

    public static String init(Context context) {
        LogUtil.d("init");
        mContext = context;
        if (!"true".equals(Utilty.getPreference(Constants.SP_KEY_DID_ISBACKUP, "false")) &&
                TextUtils.isEmpty(Utilty.getPreference(Constants.SP_KEY_DID_MNEMONIC, ""))) {
            GenrateMnemonic();
        } else {
            loadLibrary();
            mPrivateKey = Utilty.getPreference(Constants.SP_KEY_DID_PRIVATEKEY, "");
            saveSysInfo();
        }

        uploadSysinfoIfNeeded();

        return "init success";
    }

    private static void GenrateMnemonic() {
        LogUtil.d("GenrateMnemonic");
        String message = "";
        String language = "";
        String words = "";
        String sp_lang = Utilty.getPreference(Constants.SP_KEY_APP_LANGUAGE, "");
        if (TextUtils.isEmpty(sp_lang)) {
            if (Locale.getDefault().getLanguage().contains("zh")) {
                sp_lang = "chinese";
            }
        }
        if ("chinese".equals(sp_lang)) {
            language = "chinese";
            words = FileUtil.readAssetsTxt(mContext, "ElastosWalletLib/mnemonic_chinese.txt");
        } else {
            language = "english";
            words = "";
        }


        String mnemonic = ElastosKeypair.generateMnemonic(language, words);
        if (mnemonic == null) {
            String errmsg = "Failed to generate mnemonic.";
            LogUtil.e(errmsg);
            message += errmsg;
            return ;
        }
        Utilty.setPreference(Constants.SP_KEY_DID_MNEMONIC, mnemonic);
//        message += "mnemonic: " + mnemonic + "\n";

        mSeed = new ElastosKeypair.Data();
        int ret = ElastosKeypair.getSeedFromMnemonic(mSeed, mnemonic, language, words, "");
        if (ret <= 0) {
            String errmsg = "Failed to get seed from mnemonic. ret=" + ret + "\n";
            LogUtil.e(errmsg);
            message += errmsg;
            return ;
        }
        mSeedLen = ret;
        message += "seed: " + mSeed.buf + ", len: " + mSeedLen + "\n";

        String privateKey = ElastosKeypair.getSinglePrivateKey(mSeed, mSeedLen);
        if (privateKey == null) {
            String errmsg = "Failed to generate privateKey.\n";
            LogUtil.e(errmsg);
            message += errmsg;

            return ;
        }
        mPrivateKey = privateKey;
//        message += "privateKey: " + privateKey + "\n";
        Utilty.setPreference(Constants.SP_KEY_DID_PRIVATEKEY, privateKey);

        String publicKey = ElastosKeypair.getSinglePublicKey(mSeed, mSeedLen);
        if (publicKey == null) {
            String errmsg = "Failed to generate publicKey.\n";
            LogUtil.e(errmsg);
            message += errmsg;

            return ;
        }
        Utilty.setPreference(Constants.SP_KEY_DID_PUBLICKEY, publicKey);
        message += "publicKey: " + publicKey + "\n";

        String address = ElastosKeypair.getAddress(publicKey);
        if (address == null) {
            String errmsg = "Failed to get address.\n";
            LogUtil.e(errmsg);
            message += errmsg;

            return ;
        }
        message += "address: " + address + "\n";
        Utilty.setPreference(Constants.SP_KEY_DID_ADDRESS, address);

        String did = ElastosKeypairDID.getDid(publicKey);
        if (did == null) {
            String errmsg = "Failed to get did.\n";
            LogUtil.e(errmsg);
            message += errmsg;
            return ;
        }
        message += "did: " + did + "\n";
        Utilty.setPreference(Constants.SP_KEY_DID, did);

        ElastosKeypair.Data data = new ElastosKeypair.Data();
        data.buf = new byte[]{0, 1, 2, 3, 4, 5};
        ElastosKeypair.Data signedData = new ElastosKeypair.Data();
        int signedLen = ElastosKeypair.sign(privateKey, data, data.buf.length, signedData);
        if (signedLen <= 0) {
            String errmsg = "Failed to sign data.\n";
            LogUtil.e(errmsg);
            message += errmsg;

            return ;
        }

        boolean verified = ElastosKeypair.verify(publicKey, data, data.buf.length, signedData, signedLen);
        message += "verified: " + verified + "\n";

        message += "================================================\n";
        LogUtil.i("Wallet Info: " + message);
        return ;
    }

    private static void loadLibrary() {
        ElastosKeypair.Data data = new ElastosKeypair.Data();
        data.buf = new byte[]{0, 1, 2, 3, 4, 5};
        ElastosKeypair.Data signedData = new ElastosKeypair.Data();
        int signedLen = ElastosKeypair.sign(Utilty.getPreference(Constants.SP_KEY_DID_PRIVATEKEY, ""), data, data.buf.length, signedData);
        if (signedLen <= 0) {
            String errmsg = "Failed to sign data.\n";
            LogUtil.e(errmsg);
        }
    }

    /**
     * ELA->ELA
     * @param address
     * @param amount
     */
    public static void Ela2Ela(String address, String amount) {
        Ela2Ela(address, amount, null);
    }
    
    public static void Ela2Ela(String address, String aParam, final TransCallback callback) {
        long amount = Math.round(Float.parseFloat(aParam) * 100000000) ;
        String fromAddress = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");

        String param = String.format("{\"inputs\":[\"%s\"],\"outputs\":[{\"addr\":\"%s\", \"amt\":%d}]}", fromAddress, address, amount);
        HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_WALLET + Urls.ELA_CTX, param, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                LogUtil.d("Ela2Ela response:" + response);
                HttpBean ctxBean = new Gson().fromJson(response, HttpBean.class);
                if (ctxBean.getStatus() != 200 && callback != null) {
                    callback.onFailed(response);
                    return;
                }
                String signed = parseEla2ElaData(response);
                if (TextUtils.isEmpty(signed)) {
                    if (callback != null) {
                        callback.onFailed(response);
                    }
                    return;
                }
                LogUtil.d("Ela2Ela signed data=" + signed);
                String signedparam = String.format("{\"data\":\"%s\"}", signed);
                LogUtil.d("Ela2Ela signed param data=" + signedparam);
                HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_WALLET + Urls.ELA_SRT, signedparam, new HttpRequest.HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {
                        LogUtil.d("Ela2Ela result:" + response);
                        if (callback != null) {
                            callback.onSuccess(response);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        if (callback != null) {
                            callback.onFailed(e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onFailed(e.getMessage());
                }
            }
        });

    }

    /**
     * 充值 ELA-DID
     */
    public static void Ela2Did(String address, String amount) {
        Ela2Did(address, amount, null);
    }

    public static void Ela2Did(String address, String aParam, final TransCallback callback) {
        long amount = Math.round(Float.parseFloat(aParam) * 100000000) ;
        //充值来源ELA链地址
        String fromAddress = address;
        String toAddress = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");

        String param = String.format("{\"inputs\":[\"%s\"],\"outputs\":[{\"addr\":\"%s\",\"amt\":%d}]}", fromAddress, toAddress, amount);
        LogUtil.d("ela2Did param=" + param);
        HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_WALLET + Urls.ELA_CCT, param, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                LogUtil.d("ela2Did response=" + response);
                HttpBean ctxBean = new Gson().fromJson(response, HttpBean.class);
                if (ctxBean.getStatus() != 200 && callback != null) {
                    callback.onFailed(response);
                    return;
                }
                String signed = parseChongzhiData(response);
                LogUtil.d("ela2Did signed data=" + signed);
                if (TextUtils.isEmpty(signed)) {
                    return;
                }
                String signdparam = String.format("{\"data\":\"%s\"}", signed);
                LogUtil.d("ela2Did srt data=" + signdparam);
                HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_WALLET + Urls.ELA_SRT, signdparam, new HttpRequest.HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {
                        LogUtil.d("ela2Did srt result:" + response);
                        if (callback != null) {
                            callback.onSuccess(response);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        if (callback != null) {
                            callback.onFailed(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onFailed(e.getMessage());
                }
            }
        });


    }

    public static void Tixian(String address, String amount) {
        Tixian(address, amount, null);
    }

    /**
     * 提现 DID-ELA
     */
    public static void Tixian(String toAddress, String aParam, final TransCallback callback) {
        long amount = Math.round(Float.parseFloat(aParam) * 100000000) ;
        String fromAddress = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");

        String param = String.format("{\"inputs\":[\"%s\"],\"outputs\":[{\"addr\":\"%s\",\"amt\":%d}]}", fromAddress, toAddress, amount);

        HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_DID + Urls.DID_CCT, param, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                LogUtil.d("tixian response=" + response);
                HttpBean ctxBean = new Gson().fromJson(response, HttpBean.class);
                if (ctxBean.getStatus() != 200 && callback != null) {
                    callback.onFailed(response);
                    return;
                }
                String signed = parseTixianData(response);
                if (TextUtils.isEmpty(signed)) {
                    return;
                }
                String signdparam = String.format("{\"data\":\"%s\"}", signed);
                LogUtil.d("signdparam data=" + signdparam);
                HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_DID + Urls.DID_SRT, signdparam, new HttpRequest.HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {
                        LogUtil.d("tixian result:" + response);
                        if (callback != null) {
                            callback.onSuccess(response);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        if (callback != null) {
                            callback.onFailed(e.getMessage());
                        }
                    }
                });
                LogUtil.d("response=" + response);
            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onFailed(e.getMessage());
                }
            }
        });
    }

    public static void Zhuanzhang(String toAddress, String amount) {
        Zhuanzhang(toAddress, amount, null);
    }

    /**
     * 转账 DID-DID
     */
    public static void Zhuanzhang(String toAddress, String aParam, final TransCallback callback) {
        long amount = Math.round(Float.parseFloat(aParam) * 100000000) ;
        String fromAddress = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");

        String param = String.format("{\"inputs\":[\"%s\"],\"outputs\":[{\"addr\":\"%s\", \"amt\":%d}]}", fromAddress, toAddress, amount);
        HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_DID + Urls.DID_CTX, param, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                LogUtil.d("response:" + response);
                HttpBean ctxBean = new Gson().fromJson(response, HttpBean.class);
                if (ctxBean.getStatus() != 200 && callback != null) {
                    callback.onFailed(response);
                    return;
                }
                String signed = parseZhuanzhangData(response);
                if (TextUtils.isEmpty(signed)) {
                    if (callback != null) {
                        callback.onFailed(response);
                    }
                    return;
                }
                LogUtil.d("signed data=" + signed);
                String signdparam = String.format("{\"data\":\"%s\"}", signed);
                LogUtil.d("signdparam data=" + signdparam);
                HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_DID + Urls.DID_SRT, signdparam, new HttpRequest.HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {
                        LogUtil.d("zhuanzhang result:" + response);
                        if (callback != null) {
                            callback.onSuccess(response);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        if (callback != null) {
                            callback.onFailed(e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onFailed(e.getMessage());
                }
            }
        });

    }

    public static void Shoukuan(String fromAddress, long amount) {
        Shoukuan(fromAddress, amount, null);
    }

    /**
     * 收款 DID-DID
     */
    public static void Shoukuan(String fromAddress, long amount, TransCallback callback) {
        String toAddress = Utilty.getPreference(Constants.SP_KEY_DID_PUBLICKEY, "");

        String param = String.format("{\"inputs\":[\"%s\"],\"outputs\":[{\"addr\":\"%s\", \"amt\":%d}]}", fromAddress, toAddress, amount);
        HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_DID + Urls.DID_CTX, param, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                LogUtil.d("response:" + response);
                String signed = parseShoukuanData(response);//testCosignTxData();
                LogUtil.d("signed data=" + signed);
                String signdparam = String.format("{\"data\":\"%s\"}", signed);
                LogUtil.d("signdparam data=" + signdparam);
                HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_DID + Urls.DID_SRT, signdparam, new HttpRequest.HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {
                        LogUtil.d("shoukuan result:" + response);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    private static String getSignedData(String origin, String pubKey) {
        ElastosKeypair.Data data = new ElastosKeypair.Data();
        data.buf = origin.getBytes();
        ElastosKeypair.Data signedData = new ElastosKeypair.Data();
        int signedLen = ElastosKeypair.sign(mPrivateKey, data, data.buf.length, signedData);
        if (signedLen <= 0) {
            String errmsg = "Failed to sign data.\n";
            LogUtil.e(errmsg);
        }

        boolean verified = ElastosKeypair.verify(pubKey, data, data.buf.length, signedData, signedLen);
        LogUtil.i("verified=" + verified);

        return Utilty.bytesToHexString2(signedData.buf);
    }

    private static String testSignTxData() {
        String message = "";
        String transaction = String.format("{\"Transactions\":[{\"UTXOInputs\":[{\"address\":\"ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4\",\"txid\":\"583ca6c3780b3ba880b446c7ce5427e538a82fc185e54749e61805a97dc3b222\",\"index\":0, \"privateKey\":\"%s\"}],\"CrossChainAsset\":[{\"amount\":100000000,\"address\":\"ENUFoHcsfvXkAVomXJLDrhM189k7qSy3xD\"}],\"Fee\":20000,\"Outputs\":[{\"amount\":100010000,\"address\":\"XKUh4GLhFJiqAMTF6HyWQrV9pK9HcGUdfJ\"},{\"amount\":9899980000,\"address\":\"ESs1jakyQjxBvEgwqEGxtceastbPAR1UJ4\"}]}]}", "840d6c631e3d612aa624dae2d7f6d354e58135a7a6cb16ed6dd264b7d104aae7");
        LogUtil.d("origin data:" + transaction);
        String signedData = ElastosKeypairSign.generateRawTransaction(transaction);
        if(signedData == null) {
            String errmsg = "Failed to generate raw transaction.\n";
            message += errmsg;

            return message;
        }
        message += signedData + "\n";

        message += "================================================\n";
        return signedData;
    }

    private static String parseEla2ElaData(String data) {
        String returnData = "";
        CctBean cctBean = new Gson().fromJson(data, CctBean.class);
        if (cctBean.getStatus() != 200) return null;
        cctBean.getResult().getTransactions().get(0).getUTXOInputs().get(0).setPrivateKey(mPrivateKey);
        String trans = new Gson().toJson(cctBean.getResult());
        LogUtil.d("Ela2Ela:trans data=" + trans);
        returnData = ElastosKeypairSign.generateRawTransaction(trans);
        return returnData;
    }

    private static String parseChongzhiData(String data) {
        String returnData = "";
        CctBean cctBean = new Gson().fromJson(data, CctBean.class);
        if (cctBean.getStatus() != 200) return null;
        cctBean.getResult().getTransactions().get(0).getUTXOInputs().get(0).setPrivateKey(mPrivateKey);
        String trans = new Gson().toJson(cctBean.getResult());
        LogUtil.d("ela2Did:trans data=" + trans);
        returnData = ElastosKeypairSign.generateRawTransaction(trans);
        return returnData;
    }

    private static String parseTixianData(String data) {
        String returnData = "";
        CctBean cctBean = new Gson().fromJson(data, CctBean.class);
        if (cctBean.getStatus() != 200) return null;
        cctBean.getResult().getTransactions().get(0).getUTXOInputs().get(0).setPrivateKey(mPrivateKey);
        String trans = new Gson().toJson(cctBean.getResult());
        LogUtil.d("ela2Did:trans data=" + trans);
        returnData = ElastosKeypairSign.generateRawTransaction(trans);
        return returnData;
    }

    private static String parseZhuanzhangData(String data) {
        String returnData = "";
        CctBean cctBean = new Gson().fromJson(data, CctBean.class);
        if (cctBean.getStatus() != 200) return null;
        cctBean.getResult().getTransactions().get(0).getUTXOInputs().get(0).setPrivateKey(mPrivateKey);
        String trans = new Gson().toJson(cctBean.getResult());
        LogUtil.d("zhuanzhang:trans data=" + trans);
        returnData = ElastosKeypairSign.generateRawTransaction(trans);
        return returnData;
    }

    private static String parseShoukuanData(String data) {
        String returnData = "";
        CctBean cctBean = new Gson().fromJson(data, CctBean.class);
        if (cctBean.getStatus() != 200) return null;
        cctBean.getResult().getTransactions().get(0).getUTXOInputs().get(0).setPrivateKey(mPrivateKey);
        String trans = new Gson().toJson(cctBean.getResult());
        LogUtil.d("ela2Did:trans data=" + trans);
        returnData = ElastosKeypairSign.generateRawTransaction(trans);
        return returnData;
    }

    /**
     * 导入钱包
     * @param mnemonic
     * @return true:if import success;false:otherwise
     */
    public static boolean importWallet(String mnemonic) {
        LogUtil.i("importWallet mnemonic=" + mnemonic);
        String language = "";
        String words = "";
        if (Utilty.isChinese(mnemonic)) {
            language = "chinese";
        } else {
            language = "english";
        }

        if (language.equals("chinese")) {
            words = FileUtil.readAssetsTxt(mContext, "ElastosWalletLib/mnemonic_chinese.txt");
        } else {
            words = "";
        }

        mSeed = new ElastosKeypair.Data();
        int ret = ElastosKeypair.getSeedFromMnemonic(mSeed, mnemonic, language, words, "");
        if (ret <= 0) {
            String errmsg = "Failed to get seed from mnemonic. ret=" + ret + "\n";
            LogUtil.e(errmsg);
            return false;
        }
        mSeedLen = ret;

        String privateKey = ElastosKeypair.getSinglePrivateKey(mSeed, mSeedLen);
        if (privateKey == null) {
            String errmsg = "Failed to generate privateKey.\n";
            LogUtil.e(errmsg);
            return false;
        }
        LogUtil.d("privatekey=" + privateKey);

        String publicKey = ElastosKeypair.getSinglePublicKey(mSeed, mSeedLen);
        if (publicKey == null) {
            String errmsg = "Failed to generate publicKey.\n";
            LogUtil.e(errmsg);
            return false;
        }

        String address = ElastosKeypair.getAddress(publicKey);
        if (address == null) {
            String errmsg = "Failed to get address.\n";
            LogUtil.e(errmsg);
            return false;
        }

        ElastosKeypair.Data data = new ElastosKeypair.Data();
        data.buf = new byte[]{0, 1, 2, 3, 4, 5};
        ElastosKeypair.Data signedData = new ElastosKeypair.Data();
        int signedLen = ElastosKeypair.sign(privateKey, data, data.buf.length, signedData);
        if (signedLen <= 0) {
            String errmsg = "Failed to sign data.\n";
            LogUtil.e(errmsg);
            return false;
        }

        boolean verified = ElastosKeypair.verify(publicKey, data, data.buf.length, signedData, signedLen);
        if (!verified) {
            return false;
        }

        //after import success:
        mPrivateKey = privateKey;
        Utilty.setPreference(Constants.SP_KEY_DID_PRIVATEKEY, privateKey);
        Utilty.setPreference(Constants.SP_KEY_DID_PUBLICKEY, publicKey);
        Utilty.setPreference(Constants.SP_KEY_DID_ADDRESS, address);
        Utilty.setPreference(Constants.SP_KEY_DID_ISBACKUP, "true");
        Utilty.setPreference(Constants.SP_KEY_DID_MNEMONIC, "");
        Utilty.setPreference(Constants.SP_KEY_DID, ElastosKeypairDID.getDid(publicKey));
        return true;
    }

    /**
     * 重置收款地址
     * @return
     */
    public static String resetAddress() {
        String address = ElastosKeypair.getAddress(Utilty.getPreference(Constants.SP_KEY_DID_PUBLICKEY, ""));
        if (TextUtils.isEmpty(address)) return null;
        Utilty.setPreference(Constants.SP_KEY_DID_ADDRESS, address);
        return address;
    }


    public static void setDidInfo(final String memo, final TransCallback callback) {
        String fromAddress = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");

        String param = String.format("{\"inputs\":[\"%s\"],\"outputs\":[{\"addr\":\"%s\", \"amt\":%d}]}", fromAddress, fromAddress, 0);
        HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_DID + Urls.DID_CTX, param, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                LogUtil.d("response:" + response);
                HttpBean ctxBean = new Gson().fromJson(response, HttpBean.class);
                if (ctxBean.getStatus() != 200 && callback != null) {
                    callback.onFailed(response);
                    return;
                }
                String signed = parseDidData(response, memo);
                if (TextUtils.isEmpty(signed)) {
                    if (callback != null) {
                        callback.onFailed("{\"status\":\"500\",\"result\":\"internal error\"}");
                    }
                    return;
                }
                LogUtil.d("signed data=" + signed);
                String signdparam = String.format("{\"data\":\"%s\"}", signed);
                LogUtil.d("signdparam data=" + signdparam);
                HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_DID + Urls.DID_SRT, signdparam, new HttpRequest.HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {
                        LogUtil.d("setdid result:" + response);
                        if (callback != null) {
                            callback.onSuccess(response);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        if (callback != null) {
                            callback.onFailed(e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onFailed(e.getMessage());
                }
            }
        });

    }

    private static String parseDidData(String httpData, String memo) {
        String returnData = "";
        CctBean cctBean = new Gson().fromJson(httpData, CctBean.class);
        if (cctBean.getStatus() != 200) return null;
        cctBean.getResult().getTransactions().get(0).getUTXOInputs().get(0).setPrivateKey(mPrivateKey);

        String privateKey = mPrivateKey;
        String publicKey = Utilty.getPreference(Constants.SP_KEY_DID_PUBLICKEY, "");
        LogUtil.w("did=" + ElastosKeypairDID.getDid(publicKey));

        ElastosKeypair.Data data = new ElastosKeypair.Data();
        data.buf = memo.getBytes();
        ElastosKeypair.Data signedData = new ElastosKeypair.Data();
        int signedLen = ElastosKeypair.sign(privateKey, data, data.buf.length, signedData);
        if (signedLen <= 0) {
            String errmsg = "Failed to sign data.\n";
            LogUtil.e(errmsg);
        }

        boolean verified = ElastosKeypair.verify(publicKey, data, data.buf.length, signedData, signedLen);
        LogUtil.i("verified=" + verified);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("msg", Utilty.bytesToHexString2(data.buf));
        jsonObject.addProperty("pub", publicKey);
        jsonObject.addProperty("sig", Utilty.bytesToHexString2(signedData.buf));

        cctBean.getResult().getTransactions().get(0).setMemo(jsonObject.toString());

        String trans = new Gson().toJson(cctBean.getResult());
        LogUtil.d("setdid:trans data=" + trans);
        returnData = ElastosKeypairSign.generateRawTransaction(trans);
        return returnData;
    }

    public static void uploadSysinfoIfNeeded() {
        SetDidBean setDidBean = new SetDidBean();
        setDidBean.setTag("DID Property");
        setDidBean.setVer("1.0");
        setDidBean.setStatus("Normal");
        SetDidBean.PropertiesBean propertiesBean = new SetDidBean.PropertiesBean();
        propertiesBean.setStatus("Normal");
        propertiesBean.setKey("uuid");
        propertiesBean.setValue(Utilty.getUUID());
        List<SetDidBean.PropertiesBean> mList = new ArrayList<>();
        mList.add(propertiesBean);
        setDidBean.setProperties(mList);
        String memo = new Gson().toJson(setDidBean);

        LogUtil.i("upload info=" + memo);
        String isUploaded = Utilty.getPreference(Constants.SP_KEY_DID_ISUPLOADED, "false");
        if (isUploaded.equals("true")) {
            LogUtil.i("is uploaded on the previous time, ignore to upload.");
            return;
        }

        String privateKey = mPrivateKey;
        String publicKey = Utilty.getPreference(Constants.SP_KEY_DID_PUBLICKEY, "");

        ElastosKeypair.Data data = new ElastosKeypair.Data();
        data.buf = memo.getBytes();
        ElastosKeypair.Data signedData = new ElastosKeypair.Data();
        int signedLen = ElastosKeypair.sign(privateKey, data, data.buf.length, signedData);
        if (signedLen <= 0) {
            String errmsg = "Failed to sign data.\n";
            LogUtil.e(errmsg);
        }

        boolean verified = ElastosKeypair.verify(publicKey, data, data.buf.length, signedData, signedLen);
        LogUtil.i("verified=" + verified);

        JsonObject param = new JsonObject();
        param.addProperty("msg", Utilty.bytesToHexString2(data.buf));
        param.addProperty("pub", publicKey);
        param.addProperty("sig", Utilty.bytesToHexString2(signedData.buf));

        HashMap<String, String> header = new HashMap<>(2);
        String header_value = getHeaderValue();
        header.put("X-Elastos-Agent-Auth", header_value);
        HttpRequest.sendRequestWithHttpURLConnection(Urls.SERVER_DID + Urls.DID_UPLOAD, header, param.toString(), new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                LogUtil.d("upload sysInfo response:" + response);

                try {
                    JSONObject responseJson = new JSONObject(response);
                    if(responseJson.getInt("status") == 200) {
                        LogUtil.i("success to upload system info.");
                        Utilty.setPreference(Constants.SP_KEY_DID_ISUPLOADED, "true");
                    }
                } catch (Exception e) {
                    LogUtil.e("Failed to upload system info.");
                    LogUtil.e(e.getMessage());
                }
            }

            @Override
            public void onError(Exception e) {
                LogUtil.e(e.getMessage());
                LogUtil.e("Failed to upload system info.");
            }
        });
    }

    /**
     * SD保存的文件被清除时从sp恢复
     */
    public static void saveSysInfo() {
        String sdUuid = (String)FileUtils.getObjectFromSdcard(Constants.FILE_NAME);
        String spUuid = Utilty.getPreference(Constants.SP_KEY_UUID,"");
        if (TextUtils.isEmpty(sdUuid) && !TextUtils.isEmpty(spUuid)) {
            FileUtils.saveObjectToSdcard(Constants.FILE_NAME, spUuid);
        }
    }

    private static String getHeaderValue() {
        long time = System.currentTimeMillis();
        String auth = Utilty.getMd5(mAgentHeaderKey + time).toLowerCase();
        return String.format(Locale.getDefault(), "id=%s;time=%s;auth=%s", mAgentHeaderId, time, auth);
    }

    private static final String mAgentHeaderId = MemoBean.DidAppName;
    private static final String mAgentHeaderKey = "b2gvzUM79yLhCbbGNWCuhSsGdqYhA7sS";
}
