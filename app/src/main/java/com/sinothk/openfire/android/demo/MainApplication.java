package com.sinothk.openfire.android.demo;

import android.app.Application;

import com.sinothk.comm.utils.PreferUtil;
import com.sinothk.comm.utils.ToastUtil;

/**
 * @ author LiangYT
 * @ create 2018/10/20 13:22
 * @ Describe
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PreferUtil.init(this);
        ToastUtil.init(this);


    }
}
