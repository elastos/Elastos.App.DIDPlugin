package com.ela.wallet.sdk.didlibrary.http;

import android.text.TextUtils;

import com.ela.wallet.sdk.didlibrary.callback.TransCallback;
import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.global.Urls;
import com.ela.wallet.sdk.didlibrary.utils.DidLibrary;
import com.ela.wallet.sdk.didlibrary.utils.LogUtil;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import fi.iki.elonen.NanoHTTPD;

public class HttpServer extends NanoHTTPD {
    public enum Status implements NanoHTTPD.Response.IStatus {
        SWITCH_PROTOCOL(101, "Switching Protocols"),
        NOT_USE_POST(700, "not use post");

        private final int requestStatus;
        private final String description;

        Status(int requestStatus, String description) {
            this.requestStatus = requestStatus;
            this.description = description;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public int getRequestStatus() {
            return 0;
        }
    }

    public HttpServer(int port) {
        super(port);
    }

    private Response getReturnData(String data) {
        Response response = newFixedLengthResponse(Response.Status.OK, "application/json", data);
        response.addHeader("Access-Control-Allow-Headers",  "x-requested-with, Content-Type, content-type");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Max-Age", "" + 42 * 60 * 60);
        return response;
    }

    @Override
    public Response serve(IHTTPSession session) {
        LogUtil.d("httpd serve:");
        if (Method.POST.equals(session.getMethod())) {
            Map<String, String> files = new HashMap<>();
            Map<String, String> header = session.getHeaders();
            try {
                session.parseBody(files);
                String param = files.get("postData");
                LogUtil.d("header : " + header);
                LogUtil.d("body : " + param);
                header.get("http-client-ip");

                String uri = session.getUri();
                LogUtil.d("uri=" + uri);
                if (uri.startsWith("/api/v1/sendTransfer")) {
                    String transferData = dealWithZhuanzhang(param);
                    return getReturnData(transferData);
                } else if (uri.startsWith("/api/v1/setDidInfo")) {
                    String didinfo = dealWithSetDid(param);
                    return getReturnData(didinfo);
                } else {

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ResponseException e) {
                e.printStackTrace();
            }
            String data = String.format("{\"status\":\"400\",\"result\":\"bad request\"}");
            return getReturnData(data);
        }else if (Method.GET.equals(session.getMethod())){
            String uri = session.getUri();
            LogUtil.d("uri=" + uri);
            String params = session.getQueryParameterString();
            LogUtil.d("params=" + params);
            if (uri.equals("/api/v1/getDid")) {
                String did = Utilty.getPreference(Constants.SP_KEY_DID, "");
                if (TextUtils.isEmpty(did)) {
                    String data = String.format("{\"status\":\"500\",\"result\":\"internal error\"}");
                    return getReturnData(data);
                } else {
                    String data = String.format("{\"status\":\"200\",\"result\":\"%s\"}", did);
                    return getReturnData(data);
                }
            } else if (uri.startsWith("/api/v1/getAddress")) {
                String candyAddress = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");
                if (TextUtils.isEmpty(candyAddress)) {
                    String data = String.format("{\"status\":\"500\",\"result\":\"internal error\"}");
                    return getReturnData(data);
                } else {
                    String data = String.format("{\"status\":\"200\",\"result\":\"%s\"}", candyAddress);
                    return getReturnData(data);
                }
            } else if (uri.contains("/api/v1/getBalance")) {
                String balance = dealWithBalance();
                return getReturnData(balance);
            } else if (uri.startsWith("/api/v1/getTxById")) {
                String trans = dealWithGetTx(params);
                return getReturnData(trans);
            } else if (uri.startsWith("/api/v1/getAllTxs")) {
                String allTxs = dealWithAllTxs(params);
                return getReturnData(allTxs);
            } else if (uri.equals("/api/v1/getDidInfo")) {
                String didinfo = dealWithGetDid();
                return getReturnData(didinfo);
            }

            String data = String.format("{\"status\":\"400\",\"result\":\"bad request\"}");
            return getReturnData(data);
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "400");
            jsonObject.addProperty("result", "method don't support");
            return getReturnData(jsonObject.toString());
        }
    }

    /**
     * GET http://127.0.0.1:port/api/v1/getBalance
     * @return The current logined user balance. units is sela, 1 ela = 100000000 sela.
     */
    private String balanceResult;
    private String dealWithBalance() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        String url = String.format("%s%s%s", Urls.SERVER_DID, Urls.DID_BALANCE, Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, ""));
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                balanceResult = response;
                countDownLatch.countDown();
            }

            @Override
            public void onError(Exception e) {
                txResult = String.format("{\"status\":\"500\",\"result\":\"internal error\"}");
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await(30, TimeUnit.SECONDS);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogUtil.d("balanceResult=" + balanceResult);
        return balanceResult;
    }

    /**
     * GET http://127.0.0.1:port/api/v1/getTxById?txId=(string:`txid`)
     * @param  params:`txid`
     * @return The tranaction data
     */
    private String txResult;
    private String dealWithGetTx(String params) {
        LogUtil.d("dealWithGetTx:params=" + params);
        String txId = params.substring(5);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        String url = String.format("%s%s%s", Urls.SERVER_DID, Urls.DID_TX, txId);
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                txResult = response;
                countDownLatch.countDown();
            }

            @Override
            public void onError(Exception e) {
                txResult = String.format("{\"status\":\"500\",\"result\":\"internal error\"}");
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return txResult;
    }


    /**
     * GET http://127.0.0.1:port/api/v1/getAllTxs[?][pageNum=(number:`page number`)][&][pageSize=(number:`page size`)]
     * @param params
     * @return
     */
    private String allTxs;
    private String dealWithAllTxs(String params) {
        String address = Utilty.getPreference(Constants.SP_KEY_DID_ADDRESS, "");
        String url = String.format("%s%s%s", Urls.SERVER_DID_HISTORY, Urls.DID_HISTORY, address);
        if (!TextUtils.isEmpty(params) && params.contains("&")) {
            url = String.format("%s%s%s?%s", Urls.SERVER_DID_HISTORY, Urls.DID_HISTORY, address, params);
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HttpRequest.sendRequestWithHttpURLConnection(url, new HttpRequest.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                allTxs = response;
                countDownLatch.countDown();
            }

            @Override
            public void onError(Exception e) {
                allTxs = String.format("{\"status\":\"500\",\"result\":\"internal error\"}");
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allTxs;
    }


    private String dealWithChongzhi() {
        return "";
    }

    private String dealWithTiXian() {
        return "";
    }

    private String zhuanzhangData = String.format("{\"status\":\"500\",\"result\":\"internal error\"}");
    private String dealWithZhuanzhang(String param) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            JSONObject js = new JSONObject(param);
            String toAddress = js.optString("toAddress");
            long amount = js.optLong("amount");
            String memo = js.optString("memo");
            String info = js.optString("info");
            if (!TextUtils.isEmpty(toAddress) && amount >= 0) {
                DidLibrary.Zhuanzhang(toAddress, amount, new TransCallback() {
                    @Override
                    public void onSuccess(String result) {
                        zhuanzhangData = result;
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onFailed(String result) {
                        zhuanzhangData = result;
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await(30, TimeUnit.SECONDS);
            } else {
                zhuanzhangData = String.format("{\"status\":\"10001\",\"result\":\"param error\"}");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return zhuanzhangData;
    }

    private String dealWithShoukuan() {
        return "";
    }

    private String didInfo;
    private String dealWithSetDid(String memo) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            if (!TextUtils.isEmpty(memo)) {
                DidLibrary.setDidInfo(memo, new TransCallback() {
                    @Override
                    public void onSuccess(String result) {
                        didInfo = result;
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onFailed(String result) {
                        didInfo = result;
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await(30, TimeUnit.SECONDS);
            } else {
                didInfo = String.format("{\"status\":\"10001\",\"result\":\"param error\"}");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return didInfo;
    }

    private String dealWithGetDid() {
        String didInfo = Utilty.getPreference(Constants.SP_KEY_DID_INFO, "");
        return String.format("{\"status\":\"200\",\"result\":\"%s\"}", didInfo);
    }
}
