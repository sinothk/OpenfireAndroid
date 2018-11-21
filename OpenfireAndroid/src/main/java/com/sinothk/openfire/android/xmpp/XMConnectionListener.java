package com.sinothk.openfire.android.xmpp;

import android.util.Log;

import com.sinothk.openfire.android.xmpp.XmppConnection;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

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
    public void connectionClosed() {
        Log.i("XMConnectionListener", "连接关闭");
        // 关闭连接
        XmppConnection.getInstance().closeConnection();
        // 重连服务器
        tExit = new Timer();
        tExit.schedule(new TimeTask(), loginTime);
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
    public void connectionClosedOnError(Exception e) {
        Log.i("XMConnectionListener", "连接关闭异常");

        // 判断账号已被登录
        boolean error = e.getMessage().equals("stream:error (conflict)");
        if (!error) {
            // 关闭连接
            XmppConnection.getInstance().getConnection().disconnect();

            // 重连服务器
            tExit = new Timer();
            tExit.schedule(new TimeTask(), loginTime);
        } else {
            // 退出登录
        }
    }

    private class TimeTask extends TimerTask {
        @Override
        public void run() {

            if (username != null && password != null) {
                Log.i("XMConnectionListener", "尝试登录");
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
            }
        }
    }
}
