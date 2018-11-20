package com.sinothk.openfire.android.util;

import android.content.Context;

import com.sinothk.comm.utils.NetUtil;


public class IMUtil {
    public static boolean checkNetwork(Context mContext) {
        return NetUtil.isConnected(mContext);
    }
}
