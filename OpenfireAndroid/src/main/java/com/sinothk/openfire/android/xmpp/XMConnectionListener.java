package com.sinothk.openfire.android.xmpp;

import android.text.TextUtils;
import android.util.Log;

import com.sinothk.openfire.android.BuildConfig;
import com.sinothk.openfire.android.xmpp.XmppConnection;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 连接监听类
 */
public class XMConnectionListener implements ConnectionListener {

    private Timer tExit;
    private String username;
    private String password;
    private int loginTime = 2000;

    public XMConnectionListener(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    @Override
    public void connected(XMPPConnection xmppConnection) {
        Log.i("XMConnectionListener", "connected");
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {
        Log.i("XMConnectionListener", "authenticated" + b);
    }


    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }

    @Override
    public void connectionClosed() {
        Log.i("XMConnectionListener", "连接关闭");
        doReconnect("");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.i("XMConnectionListener", "连接关闭异常");
        doReconnect(e.getMessage());
    }

    private void doReconnect(String errorDesc) {
        // 判断账号已被登录
        boolean isConflict = "stream:error (conflict)".equals(errorDesc);
        if (isConflict) {
            // 退出应用
        } else {
            // 关闭连接
            XmppConnection.getInstance().getConnection().disconnect();
            // 重连服务器
            tExit = new Timer();
            tExit.schedule(new TimeTask(), loginTime);
        }
    }

    private class TimeTask extends TimerTask {
        @Override
        public void run() {

            if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {

                if (BuildConfig.DEBUG) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Log.e("XMConnectionListener", "尝试登录——" + sdf.format(new Date()));
                }

                // 连接服务器
                try {
                    if (!XmppConnection.getInstance().isAuthenticated()) {// 用户未登录

                        if ("".equals(XmppConnection.getInstance().login(username, password))) {
                            Log.i("XMConnectionListener", "登录成功");
                        } else {
                            Log.i("XMConnectionListener", "重新登录");
                            tExit.schedule(new TimeTask(), loginTime);
                        }
                    }
                } catch (Exception e) {
                    Log.i("XMConnectionListener", "尝试登录,出现异常!");
                }
            } else {
                Log.i("XMConnectionListener", "重登username和password不能为空！");
            }
        }
    }
}
