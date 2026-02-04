package com.example.notificationdemo.test5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.notificationdemo.foreground.RoomActivity;

/**
 * notify跳转回来的中转类，某些机型在notify下设置flag不起作用，导致activity重建
 * 主要用于直播间切换后台后发出通知被点击重新切换到前台,导致直播间被重建
 * 及分享的回调页面，分享不走welcome（可能通过这个页面转过去）
 * 推送消息走welcome，比如开播通知，陪聊通知
 */
public class NotificationActivity5 extends Activity {

    public static final String PARAM_TYPE = "param_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int type = intent.getIntExtra(PARAM_TYPE, 0);
        if(type == 1) {
            Intent it = new Intent(this, RoomActivity5.class);
            intent.putExtra("param1", "param1");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(it);
        }
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
