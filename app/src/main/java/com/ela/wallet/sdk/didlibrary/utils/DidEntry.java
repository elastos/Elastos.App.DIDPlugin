package com.ela.wallet.sdk.didlibrary.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

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
        Utilty.setServiceContext(context);
        try {
            Intent intent = new Intent();
            intent.setClass(context, DidService.class);
            context.startService(intent);
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
        Utilty.setContext(context);
        Intent intent = new Intent();
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
