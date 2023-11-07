package com.example.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

public class RemoteActivity extends AppCompatActivity {

    private Button bt;
    private String PAUSE_EVENT = ""; // “暂停/继续”事件的标识串
    private NotificationManager notifyMgr;
    private Handler handler;
    private Runnable runnable;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        bt = findViewById(R.id.btn_test);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 从系统服务中获取通知管理器
                notifyMgr = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                // 获取自定义消息的通知对象
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        progress++;
                        Notification notify = getNotify(RemoteActivity.this, PAUSE_EVENT,
                                "雪绒花", true, progress, SystemClock.elapsedRealtime());
                        // 使用通知管理器推送通知，然后在手机的通知栏就会看到该消息
                        notifyMgr.notify(R.string.app_name, notify);
                        handler.postDelayed(runnable,1000);
                    }
                };
                handler.postDelayed(runnable,100);
            }
        });
    }

    private Notification getNotify(Context ctx, String event, String song, boolean isPlaying, int progress, long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        // 创建一个广播事件的意图
        Intent intent1 = new Intent(event);
        //调用后只有8.0以上执行
        createNotifyChannel(notifyMgr,this,"channel_id");
        // 创建一个用于广播的延迟意图
        PendingIntent broadIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            broadIntent = PendingIntent.getBroadcast(ctx, R.string.app_name, intent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            broadIntent = PendingIntent.getBroadcast(ctx, R.string.app_name, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        // 根据布局文件notify_music.xml生成远程视图对象
        RemoteViews notify_music = new RemoteViews(ctx.getPackageName(), R.layout.notify_music);
        if (isPlaying) { // 正在播放
            notify_music.setTextViewText(R.id.btn_play, "暂停"); // 设置按钮文字
            notify_music.setTextViewText(R.id.tv_play, song + "正在播放"); // 设置文本文字
            notify_music.setTextViewText(R.id.chr_play,format.format(time)); // 设置计数器
        } else { // 不在播放
            notify_music.setTextViewText(R.id.btn_play, "继续"); // 设置按钮文字
            notify_music.setTextViewText(R.id.tv_play, song + "暂停播放"); // 设置文本文字
            notify_music.setTextViewText(R.id.chr_play,"00:00"); // 设置计数器
        }
        // 设置远程视图内部的进度条属性
        notify_music.setProgressBar(R.id.pb_play, 100, progress, false);
        // 整个通知已经有点击意图了，那要如何给单个控件添加点击事件？
        // 办法是设置控件点击的广播意图，一旦点击该控件，就发出对应事件的广播。
        notify_music.setOnClickPendingIntent(R.id.btn_play, broadIntent);
        // 创建一个跳转到活动页面的意图
        Intent intent2 = new Intent(ctx, MainActivity.class);
        // 创建一个用于页面跳转的延迟意图
        PendingIntent clickIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            clickIntent = PendingIntent.getActivity(ctx, R.string.app_name, intent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            clickIntent = PendingIntent.getActivity(ctx, R.string.app_name, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        // 创建一个通知消息的构造器
        Notification.Builder builder = new Notification.Builder(ctx);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0开始必须给每个通知分配对应的渠道
            builder = new Notification.Builder(this,"channel_id");
        }
        builder.setContentIntent(clickIntent) // 设置内容的点击意图
                .setContent(notify_music) // 设置内容视图
                .setTicker(song) // 设置状态栏里面的提示文本
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher); // 设置状态栏里的小图标
        // 根据消息构造器构建一个通知对象
        Notification notify = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notify = builder.build();
        }
        notify.bigContentView = notify_music;
        return notify;
    }

    /**
     * 创建通知渠道，Android8.0开始必须给每个通知分配对应的渠道
     * @param notifyMgr
     * @param ctx
     * @param channelId
     */
    private void createNotifyChannel(NotificationManager notifyMgr,Context ctx,String channelId){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //创建一个默认重要性的通知渠道
            NotificationChannel channel = new NotificationChannel(channelId,"Channel",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null,null);
            channel.setShowBadge(true);
            channel.canBypassDnd();//可否绕过请勿打扰模式
            channel.enableLights(true);//闪光
            channel.setLockscreenVisibility(Notification.FLAG_ONLY_ALERT_ONCE);//锁屏显示通知
            channel.canShowBadge();//桌面ICON是否可以显示角标
            channel.getGroup();//获取通知渠道组
            channel.shouldShowLights();//是否会闪光
            notifyMgr.createNotificationChannel(channel);
        }
    }
}