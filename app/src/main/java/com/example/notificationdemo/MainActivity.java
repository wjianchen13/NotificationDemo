package com.example.notificationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.notificationdemo.foreground.RoomActivity;
import com.example.notificationdemo.foreground.RoomEvent;
import com.example.notificationdemo.test5.RoomActivity5;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTest(View v) {
        NotificaitonUtils.showNotification1(this);
    }

    public void onTest1(View v) {
        startActivity(new Intent(this, RemoteActivity.class));
    }

    public void onTest3(View v) {
        startActivity(new Intent(this, PermissionActivity.class));
    }

    /**
     * 模拟进入房间，切换到桌面，点击通知返回
     * @param v
     */
    public void onTest4(View v) {
        startActivity(new Intent(this, RoomActivity.class));
    }

    /**
     * 模拟进入房间，切换到桌面，点击通知返回
     * @param v
     */
    public void onTest5(View v) {
        startActivity(new Intent(this, RoomActivity5.class));
    }


}