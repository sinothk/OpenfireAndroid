package com.sinothk.openfire.android;

import android.app.Application;

import com.sinothk.comm.utils.PreferUtil;

public class IMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PreferUtil.init(this);
    }
}
