package com.sinothk.openfire.android.demo;

import com.sinothk.comm.utils.LogUtil;
import com.sinothk.comm.utils.PreferUtil;
import com.sinothk.comm.utils.ToastUtil;
import com.sinothk.openfire.android.IMApplication;

/**
 * @ author LiangYT
 * @ create 2018/10/20 13:22
 * @ Describe
 */
public class MainApplication extends IMApplication {

    @Override
    public void onCreate() {
        super.onCreate();

//        PreferUtil.init(this);
        ToastUtil.init(this);
        LogUtil.init(this,true);
    }
}
