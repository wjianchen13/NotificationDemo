package com.example.notificationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}