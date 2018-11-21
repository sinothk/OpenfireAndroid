package com.sinothk.openfire.android.keep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ConnectionReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("ConnectionReceive", "开机启动");

        } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {

            Log.d("ConnectionReceive", "手机被唤醒");
            Intent i = new Intent();
            i.setClass(context, ConnectionService.class);
            context.startService(i);

        } else if ("com.example.service_destory".equals(intent.getAction())) {

            Log.d("un", "上次服务被挂了");
            Intent i = new Intent();
            i.setClass(context, ConnectionService.class);
            context.startService(i);

        } else if ("com.example.clock".equals(intent.getAction())) {
            Log.d("ConnectionReceive", "定时闹钟的广播");
            Intent i = new Intent();
            i.setClass(context, ConnectionService.class);
            context.startService(i);
        }
    }
}



