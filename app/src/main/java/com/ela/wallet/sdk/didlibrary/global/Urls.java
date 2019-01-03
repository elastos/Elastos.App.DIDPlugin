package com.ela.wallet.sdk.didlibrary.global;

public class Urls {

    private static String SERVER_DID_DEBUG = "https://api-wallet-did-testnet.elastos.org";
    private static String SERVER_DID_RELEASE = "https://api-wallet-did.elastos.org";
    private static String SERVER_DID_HISTORY_DEBUG = "https://api-wallet-did-testnet.elastos.org";
    private static String SERVER_DID_HISTORY_RELEASE = "https://api-wallet-did.elastos.org";
    private static String SERVER_WALLET_DEBUG = "https://api-wallet-ela-testnet.elastos.org";//http://18.179.207.38:8080";
    private static String SERVER_WALLET_RELEASE = "https://api-wallet-ela.elastos.org";
    private static String SERVER_WALLET_HISTORY_DEBUG = "https://api-wallet-ela-testnet.elastos.org";//"http://54.64.220.165:8080";
    private static String SERVER_WALLET_HISTORY_RELEASE = "https://api-wallet-ela.elastos.org";

    public static String SERVER_DID = SERVER_DID_RELEASE;
    public static String SERVER_WALLET = SERVER_WALLET_RELEASE;
    public static String SERVER_DID_HISTORY = SERVER_DID_HISTORY_RELEASE;
    public static String SERVER_WALLET_HISTORY = SERVER_WALLET_HISTORY_RELEASE;

    public static String DID_GETDID = "/api/1/didexplorer/did/";
    public static String DID_UPLOAD = "/api/1/blockagent/upchain/data";
    public static String DID_BALANCE = "/api/1/balance/";//{address};
    public static String DID_CTX = "/api/1/createTx";
    public static String DID_CCT = "/api/1/createCrossTx";
    public static String DID_SRT = "/api/1/sendRawTx";
    public static String DID_TX = "/api/1/tx/";
    public static String DID_ATX = "/api/1/getAllTxs";
    public static String DID_HISTORY = "/api/1/history/";//{address};

    public static String ELA_BALANCE = "/api/1/balance/";
    public static String ELA_CTX = "/api/1/createTx";
    public static String ELA_CCT = "/api/1/createCrossTx";
    public static String ELA_TX = "/api/1/tx/";
    public static String ELA_ATX = "/api/1/getAllTxs";
    public static String ELA_SRT = "/api/1/sendRawTx";
    public static String ELA_HISTORY = "/api/1/history/";//{address};

    static {
        if (Constants.isDebug) {
            SERVER_DID              = SERVER_DID_DEBUG;
            SERVER_WALLET           = SERVER_WALLET_DEBUG;
            SERVER_DID_HISTORY      = SERVER_DID_HISTORY_DEBUG;
            SERVER_WALLET_HISTORY   = SERVER_WALLET_HISTORY_DEBUG;
        } else {
            SERVER_DID              = SERVER_DID_RELEASE;
            SERVER_WALLET           = SERVER_WALLET_RELEASE;
            SERVER_DID_HISTORY      = SERVER_DID_HISTORY_RELEASE;
            SERVER_WALLET_HISTORY   = SERVER_WALLET_HISTORY_RELEASE;
        }
    }
}
