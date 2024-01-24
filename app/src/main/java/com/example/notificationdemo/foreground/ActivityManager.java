package com.example.notificationdemo.foreground;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
    private static ActivityManager INSTANCE = new ActivityManager();

    private int appCount = 0;
    private List<Activity> mActivityList = new ArrayList<>();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ActivityManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ActivityManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 方法3：通过ActivityLifecycleCallbacks来批量统计Activity的生命周期，来做判断，此方法在API 14以上均有效，但是需要在Application中注册此回调接口
     * 必须：
     * 1. 自定义Application并且注册ActivityLifecycleCallbacks接口
     * 2. AndroidManifest.xml中更改默认的Application为自定义
     * 3. 当Application因为内存不足而被Kill掉时，这个方法仍然能正常使用。虽然全局变量的值会因此丢失，但是再次进入App时候会重新统计一次的
     *
     * @return
     */
    public boolean isInBackground() {
        return appCount <= 0;
    }

    public int getAppCount() {
        return appCount;
    }

    public void addAppCount() {
        appCount++;
    }

    public void minusAppCount() {
        appCount--;
    }

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    public int getActivitysSize() {
        return mActivityList.size();
    }

    public List<Activity> getActivitys() {
        return mActivityList;
    }

    public Activity getStackPreviousActivity(Activity activity) {
        if (mActivityList != null && mActivityList.size() > 0 && activity != null) {
            for (int i = 0; i < mActivityList.size(); i++) {
                if (mActivityList.get(i) == activity)
                    return i > 0 ? mActivityList.get(i - 1) : activity;
            }

            return mActivityList.get(mActivityList.size() - 1);
        }

        return activity;

    }

    public Activity getStackTopActivity() {
        if (mActivityList != null && mActivityList.size() > 0) {
            try {
                return mActivityList.get(mActivityList.size() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean hasActivity(Class clazz) {
        if (mActivityList != null && mActivityList.size() > 0) {
            try {
                for (int i = 0; i < mActivityList.size(); i++) {
                    if (mActivityList.get(i).getClass().equals(clazz))
                        return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishStackTopActivity() {
        try {
            Activity activity = getStackTopActivity();
            if (activity != null) {
                activity.finish();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 遍历所有activity
     * @param checker
     */
    public void activitiesFor(IChecker checker) {

        if (checker == null)
            return;

        if (mActivityList != null && mActivityList.size() > 0) {
            try {
                for (int i = 0; i < mActivityList.size(); i++) {
                    Activity pre = null;
                    int preIndex = i - 1;
                    if (preIndex >= 0 && preIndex < mActivityList.size())
                        pre = mActivityList.get(preIndex);

                    Activity next = null;
                    int nextIndex = i + 1;
                    if (nextIndex >= 0 && nextIndex < mActivityList.size())
                        next = mActivityList.get(nextIndex);

                    if (checker.check(mActivityList.size(), i, mActivityList.get(i), pre, next))
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings(value = "unchecked")
    public <T> T findActivity(Class<T> clazz) {
        if (mActivityList != null && mActivityList.size() > 0 && clazz != null) {
            try {
                for (int i = mActivityList.size() - 1; i >= 0; i--) {
                    if (clazz.isAssignableFrom(mActivityList.get(i).getClass()))
                        return (T) mActivityList.get(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 查找全部activity
     * @param clazz
     * @return
     */
    @NonNull
    @SuppressWarnings(value = "unchecked")
    public <T> List<T> findAllActivities(Class<T> clazz) {
        List<T> results = new ArrayList<>();
        if (mActivityList != null && mActivityList.size() > 0 && clazz != null) {
            try {
                for (int i = 0; i < mActivityList.size(); i++) {
                    if (clazz.isAssignableFrom(mActivityList.get(i).getClass()))
                        results.add((T) mActivityList.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    /**
     * 如果context不是activity，那么使用top替代
     * @param context
     * @return
     */
    public @Nullable Activity getTopActivityInsteadOf(Context context){
        if (context instanceof Activity)
            return (Activity) context;
        return getStackTopActivity();
    }

    /************************************************************************************************
     *
     * class
     *
     ***********************************************************************************************/
    public interface IChecker{
        boolean check(int size, int index, Activity activity, Activity pre, Activity next);
    }
}
