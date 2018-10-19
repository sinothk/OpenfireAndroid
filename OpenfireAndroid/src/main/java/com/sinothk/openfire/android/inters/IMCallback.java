package com.sinothk.openfire.android.inters;

import com.sinothk.openfire.android.bean.IMResult;

/**
 * @ author LiangYT
 * @ create 2018/10/19 9:24
 * @ Describe
 */
public interface IMCallback {
    void onStart();

    void onEnd(IMResult result);
}
