package com.example.notificationdemo.badge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * 联想机型的桌面角标设置管理类
 * Created by zlq on 2017 17/8/23 16:35.
 */

public class BadgeNumberManagerLENOVO {

    public static boolean setBadgeNumber(Context context, int count) {
        try {
            Bundle extra = new Bundle();
            ArrayList<String> ids = new ArrayList<>();
            // 以列表形式传递快捷方式id，可以添加多个快捷方式id
            // ids这个参数可以为空或者“null”表示对主图标进行角标标记
//        ids.add("custom_id_1");
//        ids.add("custom_id_2");
            extra.putStringArrayList("app_shortcut_custom_id", ids);
            extra.putInt("app_badge_count", count);
            Uri contentUri = Uri.parse("content://com.android.badge/badge");
            Bundle bundle = context.getContentResolver().call(contentUri, "setAppBadgeCount", null,
                    extra);
            return bundle != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}