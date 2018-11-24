//package com.sinothk.openfire.android.keep;
//
//import android.content.Intent;
//import android.util.Log;
//
//
//public class ConnectionService extends BaseConnectionService {
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.e("ConnectionService", "Service onStartCommand");
//        running();
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private void running() {
//        if (!IMHelper.isAuthenticated()) {
//            IMHelper.autoLogin();
//
//        }
//    }
//}
//
