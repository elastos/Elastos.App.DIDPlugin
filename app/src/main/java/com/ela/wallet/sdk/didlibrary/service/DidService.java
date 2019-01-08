package com.ela.wallet.sdk.didlibrary.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ela.wallet.sdk.didlibrary.http.HttpServer;
import com.ela.wallet.sdk.didlibrary.utils.DidLibrary;
import com.ela.wallet.sdk.didlibrary.utils.Utilty;

import java.io.IOException;

public class DidService extends Service {

    private static HttpServer mHttpServer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utilty.setServiceContext(this);
        DidLibrary.init(this);
        mHttpServer = new HttpServer(34561);
        try {
            mHttpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 26)
            startForeground(34561, new Notification());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mHttpServer != null) {
            mHttpServer.stop();
        }
        Intent localIntent = new Intent();
        localIntent.setClass(this, DidService.class);  //销毁时重新启动Service
        this.startService(localIntent);
    }
}
