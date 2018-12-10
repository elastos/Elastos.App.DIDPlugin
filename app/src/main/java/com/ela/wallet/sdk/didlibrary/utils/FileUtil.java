package com.ela.wallet.sdk.didlibrary.utils;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    /**
     * 读取assets下的txt文件，返回utf-8 String
     * @param context
     * @param fileName
     * @return
     */
    public static String readAssetsTxt(Context context,String fileName){
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer, "utf-8");
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
