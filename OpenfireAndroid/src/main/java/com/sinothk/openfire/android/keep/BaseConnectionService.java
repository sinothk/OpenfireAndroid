package com.sinothk.openfire.android.keep;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class BaseConnectionService extends Service {

    // =========================================================================================
//    private volatile static ConnectionService singleton;
//
//    public static ConnectionService getInstance() {
//        if (singleton == null) {
//            synchronized (ConnectionService.class) {
//                if (singleton == null) {
//                    singleton = new ConnectionService();
//                }
//            }
//        }
//        return singleton;
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //创建Intent
        Intent intent = new Intent(this, ConnectionReceive.class);
        intent.setAction("com.example.clock");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //定义开始时间
        long triggerAtTime = SystemClock.elapsedRealtime();

        //周期触发
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, 5000, pendingIntent);//5 * 1000
        }

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (isStop) {
//            // 正常退出
//            //创建Intent
//            Intent intent = new Intent(this, ConnectionReceive.class);
//            intent.setAction("com.example.clock");
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//
//            //周期触发
//            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            // 服务停止，同时停止定时任务
//            if (manager != null) {
//                manager.cancel(pendingIntent);
//            }
//        } else {
        // 异常退出，重启服务
        Intent intent = new Intent("com.example.service_destory");
        sendBroadcast(intent);
//        }
    }

}
