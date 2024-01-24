package com.example.notificationdemo.foreground;

import android.app.Activity;

public class RoomEvent {

    private Activity activity;
    private boolean isInBackground;

    public RoomEvent(Activity activity, boolean isInBackground) {
        this.activity = activity;
        this.isInBackground = isInBackground;
    }

    public boolean isThis(Activity activity){
        return activity != null && activity == this.activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public boolean isInBackground(){
        return isInBackground;
    }

}
