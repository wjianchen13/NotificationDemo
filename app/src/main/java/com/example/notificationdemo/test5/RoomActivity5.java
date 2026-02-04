package com.example.notificationdemo.test5;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.notificationdemo.BaseApp;
import com.example.notificationdemo.R;
import com.example.notificationdemo.badge.BadgeNumberManager;
import com.example.notificationdemo.foreground.NotificationActivity;
import com.example.notificationdemo.foreground.RoomEvent;
import com.example.notificationdemo.utils.NotificationUtils;
import com.example.notificationdemo.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * NotificationActivity 启动RoomActivity，需要把RoomActivity的launchMode设置成singleTask才行，否则每次都会重建
 * 通知在后台弹出的时候，每次都会有一个角标，现在小米手机测试是这样，
 * 如果前台弹出，设置IMPORTANCE_LOW，则没有角标
 */
public class RoomActivity5 extends AppCompatActivity {

    private TextView tvTest;
    private static final int NOTIFICATION_ID = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.log("RoomActivity onCreate");
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_room5);
        tvTest = findViewById(R.id.tv_test);
    }

    public void onTest(View v) {
//        tvTest.setText("skdfjkasdkfaskjdf");
        BadgeNumberManager.getInstance().setBadgeNumber(this, 0, "hello", "title");
//        BadgeNumberManager.getInstance().setBadgeNumber(this, 102);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Utils.log("RoomActivity onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.log("RoomActivity onResume");
        clearNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dataReceive(RoomEvent roomEvent) {
        if(roomEvent.isInBackground() && roomEvent.isThis(this)) {
            showNotification();
//            BadgeNumberManager.getInstance().setBadgeNumber(this, 2, "hello", "title");
        }
    }

    private void clearNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification() {
        String title = "You are in the room of [room name]";
        String content = "click to return the room";

        Intent goIntent = new Intent(RoomActivity5.this, NotificationActivity5.class);
        Bundle bundle = new Bundle();
        bundle.putInt(NotificationActivity.PARAM_TYPE, 1);
        goIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(RoomActivity5.this, 0, goIntent, NotificationUtils.defaultPendingIntentFlag());

        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = NotificationUtils.getLivingChannel();
            if (channel != null) {
                channel.setShowBadge(false);
                manager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(RoomActivity5.this, channel.getId());
            }
        }

        if (builder == null) {
            builder = new NotificationCompat.Builder(RoomActivity5.this);
        }

//        builder.setSmallIcon(R.drawable.ic_app);
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder);
        builder.setColor(ContextCompat.getColor(BaseApp.getInstance(), R.color.colorAccent));

        Bitmap var3 = BitmapFactory.decodeResource(BaseApp.getInstance().getResources(), R.drawable.ic_notification_big);
        builder.setLargeIcon(var3);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setAutoCancel(false);
        builder.setOngoing(false);

//        builder.setAutoCancel(true);

        Notification notification = builder.build();
        notification.when = System.currentTimeMillis();
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.contentIntent = pendingIntent;

        manager.notify(NOTIFICATION_ID, notification);
    }

}
