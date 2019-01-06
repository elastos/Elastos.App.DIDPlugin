package com.ela.wallet.sdk.didlibrary.utils;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ela.wallet.sdk.didlibrary.activity.HomeActivity;
import com.ela.wallet.sdk.didlibrary.service.DidService;

/**
 * Created by Administrator on 2018/12/3.
 */

public class DidEntry {

    /**
     * log开关
     * @param open
     */
    public static void openLog(boolean open) {
        if (open) {
            LogUtil.setLogLevel(LogUtil.VERBOSE);
        }
    }

    /**
     * 初始化接口
     * @param context
     */
    public static void init(Context context) {
        LogUtil.i("DidEntry:init");
        try {
            Intent intent = new Intent();
            intent.setClass(context, DidService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动SDK界面
     * @param context
     */
    public static void launch(Context context) {
        LogUtil.i("DidEntry:launch");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context = Utilty.getContext() == null ? context : Utilty.getContext();
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
