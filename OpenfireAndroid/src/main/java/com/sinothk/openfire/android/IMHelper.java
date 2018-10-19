package com.sinothk.openfire.android;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.sinothk.openfire.android.bean.IMCode;
import com.sinothk.openfire.android.bean.IMResult;
import com.sinothk.openfire.android.inters.IMCallback;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;


/**
 * @ author LiangYT
 * @ create 2018/10/18 15:17
 * @ Describe
 */
public class IMHelper {

    // 连接实体
    private static AbstractXMPPConnection connection;

    private static String serverName;
    private static String serverIp;
    private static int serverPort;
    private static String TAG = IMHelper.class.getSimpleName();

    public static void init(String server_name, String server_ip, int server_port) {
        serverName = server_name;
        serverIp = server_ip;
        serverPort = server_port;
    }

    /**
     * 判断是否已连接
     */
    public static boolean checkConnection() {
        return null != connection && connection.isConnected();
    }

    /**
     * 判断连接是否通过了身份验证
     * 即是否已登录
     *
     * @return
     */
    public static boolean isAuthenticated() {
        return connection != null && connection.isConnected() && connection.isAuthenticated();
    }

    /**
     * 打开连接
     */
    public static void exeConnection(final Activity currActivity, final IMCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (currActivity == null || callback == null) {
                    Log.e(TAG, "openConnection -> 参数为空 ...");
                    return;

                } else {
                    currActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onStart();
                        }
                    });
                }

                if (TextUtils.isEmpty(serverName) || TextUtils.isEmpty(serverIp) || serverPort == 0) {
                    returnInfo(currActivity, callback, new IMResult(IMCode.ERROR, "参数初始化有误", "连接参数异常：请检查IMHelper.init(*)是否已经调用！"));

                } else {
                    // 执行连接
                    IMResult result = exeConnection();
                    returnInfo(currActivity, callback, result);
                }
            }
        }).start();
    }

    /**
     * 连接
     *
     * @return
     */
    public static IMResult exeConnection() {
        if (null == connection || !connection.isAuthenticated()) {

            SmackConfiguration.DEBUG = true;

            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();

            //设置openfire主机IP
//                config.setHostAddress(InetAddress.getByName(serverIp));
            config.setHost(serverIp);

            //设置openfire服务器名称
//                config.setXmppDomain(serverName);
            config.setServiceName(serverName);

            //设置端口号：默认5222
            config.setPort(serverPort);
            //禁用SSL连接
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).setCompressionEnabled(false);
            //设置Debug
            config.setDebuggerEnabled(true);
            //设置离线状态
            config.setSendPresence(false);
            //设置开启压缩，可以节省流量
            config.setCompressionEnabled(true);

            //需要经过同意才可以添加好友
            Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);

            // 将相应机制隐掉
            //SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
            //SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");

            connection = new XMPPTCPConnection(config.build());

            try {
                connection.connect();// 连接到服务器
                return new IMResult(IMCode.SUCCESS, "连接成功");

            } catch (XMPPException | SmackException | IOException e) {
                e.printStackTrace();
                connection = null;

                return new IMResult(IMCode.ERROR, "连接异常", e.getMessage());
            }
        } else {
            return new IMResult(IMCode.SUCCESS, "当前已是连接状态");
        }
    }

    /**
     * 用户登录
     *
     * @param userName
     * @param pwd
     * @return
     */
    public static IMResult login(String userName, String pwd) {
        try {

            if (!checkConnection()) {
                exeConnection();
            }

            if (isAuthenticated()) {
                return new IMResult(IMCode.SUCCESS, "当前已登录");
            } else {
                connection.login(userName, pwd);
                return new IMResult(IMCode.SUCCESS, "登录成功");
            }
        } catch (XMPPException | SmackException | IOException e) {
            e.printStackTrace();
            return new IMResult(IMCode.ERROR, "登录失败", e.getMessage());
        }
    }

    /**
     * 用户登录
     *
     * @param userName
     * @param pwd
     * @return
     */
    public static void login(final Activity currActivity, final String userName, final String pwd, final IMCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final IMResult result = login(userName, pwd);

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onEnd(result);
                    }
                });
            }
        }).start();
    }


    public static IMResult logout() {
        try {
            connection.disconnect();
            return new IMResult(IMCode.SUCCESS, "退出成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new IMResult(IMCode.ERROR, "退出失败", e.getMessage());
        }
    }


    public static void logout(final Activity mActivity, final IMCallback imCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final IMResult result = logout();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(result);
                    }
                });
            }
        }).start();
    }

    /**
     * =================================================
     */
    private static void returnInfo(Activity currActivity, final IMCallback callback, final IMResult iMResult) {
        if (currActivity == null || callback == null || iMResult == null) {
            return;
        }

        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onEnd(iMResult);
            }
        });
    }


}
