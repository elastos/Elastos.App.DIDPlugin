package com.ela.wallet.sdk.didlibrary.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ela.wallet.sdk.didlibrary.global.Constants;
import com.ela.wallet.sdk.didlibrary.service.DidService;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilty {

    private static Context mContext;
    private static Context sContext;
    private static Context aContext;

    private static final String SP_NAME = "didlibrary";
    private static SharedPreferences sp;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static void setAppContext(Context context) {
        aContext = context;
    }

    public static void setServiceContext(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        LogUtil.i("pid=" + Process.myPid() + ";mContext=" + mContext);
        LogUtil.i("pid=" + Process.myPid() + ";sContext=" + sContext);
        if (sContext == null && mContext != null) {
            try {
                Intent intent = new Intent();
                intent.setClass(mContext, DidService.class);
                mContext.startService(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (sContext == null && mContext == null && aContext != null) {
            DidEntry.init(aContext);
            return null;
        }
        return sContext == null ? mContext : sContext;
    }

    public static boolean setPreference(String key, String value) {
        if (sp == null) {
            sp = getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor mEditor = sp.edit();
        mEditor.putString(key, value);
        return mEditor.commit();
    }

    @Nullable
    public static String getPreference(String key, String defValue) {
        if (getContext() == null) {
            return "";
        }
        if (sp == null) {
            try {
                sp = getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }
        }
        if (sp == null) {
            return "";
        }
        return sp.getString(key, defValue);
    }

    public static boolean isBacked() {
        return "true".equals(Utilty.getPreference(Constants.SP_KEY_DID_ISBACKUP, "false"));
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 字符串转换成十六进制字符串
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    /**
     * 把16进制字符串转换成字节数组
     * @param hex
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 数组转换成十六进制字符串
     * @param bArray
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 数组转成十六进制字符串
     * @param b
     * @return HexString
     */
    public static String toHexString1(byte[] b){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i){
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }
    public static String toHexString1(byte b){
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1){
            return "0" + s;
        }else{
            return s;
        }
    }


    public static String bytesToHexString2(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) {
            buf.append(String.format("%02x", new Integer(b & 0xFF)));
        }

        return buf.toString();
    }

    /**
     * 十六进制字符串转换成字符串
     * @param hexStr
     * @return String
     */
    public static String hexStr2Str(String hexStr) {

        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * 十六进制字符串转换字符串
     * @param s
     * @return String
     */
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    // 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        // 根据字节码判断
        return c >= 0x4E00 && c <= 0x9FA5;
    }

    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                // 有一个中文字符就返回
                return true;
            }
        }
        return false;
    }

    public static String getIMEI() {
        if (TextUtils.isEmpty(getDeviceIdForGeneral(getContext()))) {
            return "";
        }
        return getDeviceIdForGeneral(getContext());

    }

    public static String getIMSI() {
        //TODO houhong
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//            try {
//                TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
//                return tm.getSubscriberId();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "0000000000";
//            }
//        } else {
//            return "0000000000";
//        }
        return "0000000000";
    }

    private static String getDeviceIdForGeneral(Context var0) {
        String var1 = "";
        if(var0 == null) {
            return var1;
        } else {
            if(Build.VERSION.SDK_INT < 23) {
                var1 = getIMEI(var0);
                if(TextUtils.isEmpty(var1)) {
                    if(TextUtils.isEmpty(var1)) {
                        var1 = Settings.Secure.getString(var0.getContentResolver(), "android_id");
                        if(TextUtils.isEmpty(var1)) {
                            var1 = getSerialNo();
                        }
                    }
                }
            } else if(Build.VERSION.SDK_INT == 23) {
                var1 = getIMEI(var0);
                if(TextUtils.isEmpty(var1)) {
                    if(TextUtils.isEmpty(var1)) {
                        var1 = Settings.Secure.getString(var0.getContentResolver(), "android_id");

                        if(TextUtils.isEmpty(var1)) {
                            var1 = getSerialNo();
                        }
                    }
                }
            } else {
                var1 = getIMEI(var0);
                if(TextUtils.isEmpty(var1)) {
                    var1 = getSerialNo();
                    if(TextUtils.isEmpty(var1)) {
                        var1 = Settings.Secure.getString(var0.getContentResolver(), "android_id");
                    }
                }
            }

            return var1;
        }
    }

    private static String getIMEI(Context var0) {
        String var1 = "";
        if(var0 == null) {
            return var1;
        } else {
            TelephonyManager var2 = (TelephonyManager)var0.getSystemService(Context.TELEPHONY_SERVICE);
            if(var2 != null) {
                try {
                    if(checkPermission(var0, "android.permission.READ_PHONE_STATE")) {
                        var1 = var2.getDeviceId();
                    }
                } catch (Throwable var4) {
                }
            }

            return var1;
        }
    }

    private static String getSerialNo() {
        String var0 = "";
        if(Build.VERSION.SDK_INT >= 9) {
            if(Build.VERSION.SDK_INT >= 26) {
                try {
                    Class var1 = Class.forName("android.os.Build");
                    Method var2 = var1.getMethod("getSerial", new Class[0]);
                    var0 = (String)var2.invoke(var1, new Object[0]);
                } catch (Throwable var3) {
                }
            } else {
                var0 = Build.SERIAL;
            }
        }

        return var0;
    }

    public static boolean checkPermission(Context var0, String var1) {
        boolean var2 = false;
        if(var0 == null) {
            return var2;
        } else {
            if(Build.VERSION.SDK_INT >= 23) {
                try {
                    Class var3 = Class.forName("android.content.Context");
                    Method var4 = var3.getMethod("checkSelfPermission", new Class[]{String.class});
                    int var5 = ((Integer)var4.invoke(var0, new Object[]{var1})).intValue();
                    if(var5 == 0) {
                        var2 = true;
                    } else {
                        var2 = false;
                    }
                } catch (Throwable var6) {
                    var2 = false;
                }
            } else {
                PackageManager var7 = var0.getPackageManager();
                if(var7.checkPermission(var1, var0.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                    var2 = true;
                }
            }

            return var2;
        }
    }

    public static String getMd5(@NonNull String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5 = new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return fillMD5(md5);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误:" + e.getMessage(), e);
        }
    }

    public static String fillMD5(String md5) {
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }

    private static String getAndroidId() {
        String var1 = "";
        try {
            var1 = Settings.Secure.getString(getContext().getContentResolver(), "android_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return var1;
    }

    public static String getUUID() {
        if (!TextUtils.isEmpty(getPreference(Constants.SP_KEY_UUID, ""))) {
            return getPreference(Constants.SP_KEY_UUID, "");
        }
        String sdUuid = (String)FileUtils.getObjectFromSdcard(Constants.FILE_NAME);
        if (!TextUtils.isEmpty(sdUuid)) {
            setPreference(Constants.SP_KEY_UUID, sdUuid);
            return sdUuid;
        }
        String imei = getDeviceIdForGeneral(getContext());
        String androidId = getAndroidId();
        String serial = getSerialNo();
        String uuid = getMd5(imei + androidId + serial);
        setPreference(Constants.SP_KEY_UUID, uuid);
        FileUtils.saveObjectToSdcard(Constants.FILE_NAME, uuid);
        return uuid;
    }

}
