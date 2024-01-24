package com.example.notificationdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.example.notificationdemo.foreground.ActivityManager;
import com.example.notificationdemo.foreground.RoomEvent;
import com.example.notificationdemo.utils.Utils;

import org.greenrobot.eventbus.EventBus;

public class BaseApp  extends Application {

    private static BaseApp mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                try {
                    ActivityManager.getInstance().addActivity(activity);
                    Utils.log("onActivityCreated activity: " + activity + "  size: " + ActivityManager.getInstance().getActivitysSize());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                boolean isInBackground = ActivityManager.getInstance().isInBackground();
                Utils.log("onActivityStarted activity: " + activity + "  isInBackground: " + isInBackground);
                ActivityManager.getInstance().addAppCount();
                if (!ActivityManager.getInstance().isInBackground() && isInBackground)
                    EventBus.getDefault().post(new RoomEvent(activity, false));
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Utils.log("onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Utils.log("onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ActivityManager.getInstance().minusAppCount();
                boolean isInBackground = ActivityManager.getInstance().isInBackground();
                Utils.log("onActivityStopped activity: " + activity + "  isInBackground: " + isInBackground);
                if (isInBackground)
                    EventBus.getDefault().post(new RoomEvent(activity, true));

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Utils.log("onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                try {
                    ActivityManager.getInstance().removeActivity(activity);
                    Utils.log("onActivityDestroyed activity: " + activity + "  size: " + ActivityManager.getInstance().getActivitysSize());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mApplication = this;
    }

    public static BaseApp getInstance() {
        return mApplication;
    }


}
