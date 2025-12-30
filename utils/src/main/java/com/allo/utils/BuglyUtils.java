package com.allo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.UUID;

/**
 * desc:
 * 集成参考 https://bugly.qq.com/docs/user-guide/advance-features-android/
 * bug查看 https://bugly.qq.com/v2/crash-reporting/crashes/c6cd4a696f?pid=1
 * logcat tag CrashReport
 * verson:
 * create by zhijun on 2022/7/28 10:53
 * update by zhijun on 2022/7/28 10:53
 */
public class BuglyUtils {

    /**
     * 为了隐私合规，，这里采用uuid随机生成一个--没有登录的情况
     */
    private static String uuid = null;

    /**
     * 初始化 腾讯bugly
     *
     * @param context
     */
    public static void init(Context context, String key, String channel, String ver) {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        createUUID(context);
        strategy.setDeviceID(uuid);
        strategy.setAppChannel(channel);  //设置渠道
        strategy.setDeviceModel(android.os.Build.MANUFACTURER + "/" + android.os.Build.BRAND + "/" + android.os.Build.MODEL);

        if (BuildConfig.DEBUG) {//构建的是DEBUG模式或者日志开关是打开状态统计到debug
            strategy.setAppVersion(ver + "(dev)");
        } else {
            strategy.setAppVersion(ver);//App的版本
        }
        strategy.setAppPackageName(context.getPackageName());  //App的包名
        CrashReport.initCrashReport(context, key, BuildConfig.DEBUG, strategy);

    }

    /**
     * 设置当前用户信息
     *
     * @param context
     */
    public static void setUserId(Context context) {
        try {
            String uid = SPUtils.with().getString("login_user_id", "");
            String phone = SPUtils.with().getString("login_user_phone", "");
            if (!TextUtils.isEmpty(uid)) {
                CrashReport.setUserId(context, uid + "/" + phone);
            } else {
                createUUID(context);
                CrashReport.setUserId(context, uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上报异常
     *
     * @param thr
     */
    public static void postCachedException(Throwable thr) {
        if (BuildConfig.DEBUG){
            thr.printStackTrace();
        }

        CrashReport.postCatchedException(thr); // bugly 自定义异常接口 上报
    }

    /**
     * 创建本地设备 uuid （为了防止隐私合规问题）
     *
     * @param context
     */
    public static void createUUID(Context context) {
        if (uuid != null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences("CrashReport", Context.MODE_PRIVATE);
        String crashReportUserId = sp.getString("crash_report_device_id", "");
        if (TextUtils.isEmpty(crashReportUserId)) {
            uuid = UUID.randomUUID().toString();
            sp.edit().putString("CrashReportUserId", uuid).apply();
        } else {
            uuid = crashReportUserId;
        }
    }
}
