package com.example.notificationdemo.badge;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * 三星机型的桌面角标设置管理类
 * Created by zlq on 2017 17/8/23 16:35.
 */

public class BadgeNumberManagerSAMSUNG {

    public static void setBadgeNumber(Context context, int count) {
        String launcherClassName = BadgeNumberManager.getLauncherClassName(context);
        if (TextUtils.isEmpty(launcherClassName)) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }
}