package com.example.notificationdemo.badge;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.notificationdemo.foreground.RoomActivity;
import com.example.notificationdemo.utils.NotificationUtils;

/**
 * 应用桌面角标设置的管理类
 * Created by zlq on 2017 17/8/23 14:50.
 * https://blog.csdn.net/weixin_48488560/article/details/130420568
 */

public class BadgeNumberManager {

    private final static int NOTICI_ID = 1;
    private static BadgeNumberManager manager;
    private NotificationManager notificationManager;
    private Notification notification;

    private BadgeNumberManager() {}

    public static BadgeNumberManager getInstance() {
        if (manager == null) {
            synchronized (BadgeNumberManager.class) {
                if (manager == null) {
                    manager = new BadgeNumberManager();
                }
            }
        }
        return manager;
    }

    private static final BadgeNumberManager.Impl IMPL;


    /**
     * 设置应用在桌面上显示的角标数字
     *
     * @param number 显示的数字
     */
    public void setBadgeNumber(Context context, int number) {
        IMPL.setBadgeNumber(context, number);
    }

    /**
     * 设置应用在桌面上显示的角标数字
     *
     * @param number 显示的数字
     */
    public void setBadgeNumber(Context context, int number, String title, String desc) {
        setNotification(context, number, title, desc);
        if (IMPL instanceof ImplXiaoMi || IMPL instanceof ImplBase) {
            IMPL.setBadgeNumber(notification, number);
        } else {
            IMPL.setBadgeNumber(context, number);
        }
    }

    public void setNotification(Context context, int number, String title, String desc) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "approval_message_id";
            String channelName = "新增待审批消息通知";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            channel.setShowBadge(false);
            channel.setDescription("收到新的待审批消息时使用的通知类别");
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, RoomActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, NotificationUtils.defaultPendingIntentFlag());

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notification = new NotificationCompat.Builder(context, "approval_message_id")
                .setContentTitle(title)
                .setContentText(desc)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setAutoCancel(true)
                .setChannelId("approval_message_id")
//                .setNumber(number)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentIntent(pendingIntent)//通知栏点击进入的页面
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//设置将从系统默认值继承哪些通知属性 基本就是用来设置是否通知音效或者震动
                .setSound(alarmSound)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400}) //自定义震动的频率
                .setTicker(desc)//收到通知后从顶部弹出精简版通知
                .build();

        //取消掉上一条通知消息
        notificationManager.cancel(NOTICI_ID);
        notificationManager.notify(NOTICI_ID, notification);
    }

    public void cancleNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(NOTICI_ID);
        }
    }

    interface Impl {

        void setBadgeNumber(Context context, int number);

        void setBadgeNumber(Notification notification, int number);
    }

    static class ImplHuaWei implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerHuaWei.setBadgeNumber(context, number);
        }

        @Override
        public void setBadgeNumber(Notification notification, int number) {

        }
    }

    static class ImplXiaoMi implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {

        }

        @Override
        public void setBadgeNumber(Notification notification, int number) {
            //小米机型的桌面应用角标API跟通知绑定在一起了，所以单独做处理
            BadgeNumberManagerXiaoMi.setBadgeNumber(notification, number);
        }
    }

    static class ImplVIVO implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerVIVO.setBadgeNumber(context, number);
        }

        @Override
        public void setBadgeNumber(Notification notification, int number) {

        }
    }

    static class ImplOPPO implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerOPPO.setBadgeNumber(context, number);
        }

        @Override
        public void setBadgeNumber(Notification notification, int number) {

        }
    }

    static class ImplSAMSUNG implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerSAMSUNG.setBadgeNumber(context, number);
        }

        @Override
        public void setBadgeNumber(Notification notification, int number) {

        }
    }

    static class ImplSONY implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerSONY.setBadgeNumber(context, number);
        }

        @Override
        public void setBadgeNumber(Notification notification, int number) {

        }
    }

    static class ImplLENOVO implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerLENOVO.setBadgeNumber(context, number);
        }

        @Override
        public void setBadgeNumber(Notification notification, int number) {

        }
    }

    static class ImplBase implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {

        }

        @Override
        public void setBadgeNumber(Notification notification, int number) {
            BadgeNumberManagerXiaoMi.setBadgeNumber(notification, number);
        }
    }

    static {
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equalsIgnoreCase(MobileBrand.HUAWEI)) {
            IMPL = new ImplHuaWei();//华为
        } else if (manufacturer.equalsIgnoreCase(MobileBrand.XIAOMI)) {
            IMPL = new ImplXiaoMi();//小米
        } else if (manufacturer.equalsIgnoreCase(MobileBrand.VIVO)) {
            IMPL = new ImplVIVO();//vivo
        } else if (manufacturer.equalsIgnoreCase(MobileBrand.OPPO)) {
            IMPL = new ImplOPPO();//oppo
        } else if (manufacturer.equalsIgnoreCase(MobileBrand.SAMSUNG)) {
            IMPL = new ImplSAMSUNG();//三星
        } else if (manufacturer.equalsIgnoreCase(MobileBrand.SONY)) {
            IMPL = new ImplSONY();//索尼
        } else if (manufacturer.equalsIgnoreCase(MobileBrand.LENOVO)) {
            IMPL = new ImplLENOVO();//联想
        } else {
            IMPL = new ImplBase();
        }
    }

    public static String getLauncherClassName(Context context) {
        ComponentName launchComponent = getLauncherComponentName(context);
        if (launchComponent == null) {
            return "";
        } else {
            return launchComponent.getClassName();
        }
    }

    public static ComponentName getLauncherComponentName(Context context) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context
                .getPackageName());
        if (launchIntent != null) {
            return launchIntent.getComponent();
        } else {
            return null;
        }
    }
}