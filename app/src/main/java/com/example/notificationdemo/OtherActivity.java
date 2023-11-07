package com.example.notificationdemo;

import android.app.PendingIntent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }

//    private NotificationCompat.Builder getChannelNotification(String subject, String message, PendingIntent intent) {
//
//        if (Build.VERSION.SDK_INT {
//        return null;
//    }
//
//    return new NotificationCompat.Builder(context, XXX_ID)
//            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//            .setSmallIcon(R.mipmap.icon_notification_small)
//            .setContentIntent(intent)
//            .setContentTitle(subject)
//            .setContentText(message)
//            .setAutoCancel(true)
//            .setShowWhen(true)
//            .setVisibility(Notification.VISIBILITY_PUBLIC)
//            .setPriority(NotificationCompat.PRIORITY_HIGH);
//
//}
}