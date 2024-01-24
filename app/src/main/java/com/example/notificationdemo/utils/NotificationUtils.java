package com.example.notificationdemo.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.res.Resources;
import android.os.Build;

import com.example.notificationdemo.BaseApp;
import com.example.notificationdemo.R;

public class NotificationUtils {

    public static int defaultPendingIntentFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE;
        } else {
            return PendingIntent.FLAG_UPDATE_CURRENT;
        }
    }

    public static NotificationChannel getLivingChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(BaseApp.getInstance().getResources().getString(R.string.room_notification_id)
                        , BaseApp.getInstance().getResources().getString(R.string.room_notification_name)
                        , NotificationManager.IMPORTANCE_LOW);
                channel.setDescription(BaseApp.getInstance().getResources().getString(R.string.room_notification_desc));
                channel.setShowBadge(true);
                return channel;
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
