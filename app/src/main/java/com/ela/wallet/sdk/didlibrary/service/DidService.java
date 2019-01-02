package com.ela.wallet.sdk.didlibrary.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.http.HttpServer;
import com.ela.wallet.sdk.didlibrary.utils.DidLibrary;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;

import java.io.IOException;
import java.util.Locale;

public class DidService extends Service {

    private HttpServer mHttpServer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utilty.setServiceContext(this);
        DidLibrary.init(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utilty.setServiceContext(this);
        mHttpServer = new HttpServer(34561);
        try {
            mHttpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mHttpServer != null) {
            mHttpServer.stop();
        }
        super.onDestroy();
    }
}
