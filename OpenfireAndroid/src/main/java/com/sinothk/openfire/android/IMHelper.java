package com.sinothk.openfire.android;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.sinothk.openfire.android.bean.IMCode;
import com.sinothk.openfire.android.bean.IMResult;
import com.sinothk.openfire.android.bean.IMUser;
import com.sinothk.openfire.android.inters.IMCallback;
import com.sinothk.openfire.android.xmpp.XmppConnection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.parts.Domainpart;
import org.jxmpp.jid.parts.Localpart;

import java.io.IOException;


/**
 * @ author LiangYT
 * @ create 2018/10/18 15:17
 * @ Describe
 */
public class IMHelper {

    private static String TAG = IMHelper.class.getSimpleName();

    public static void init(String server_name, String server_ip, int server_port) {
        XmppConnection.init(server_name, server_ip, server_port);
    }

    public static XmppConnection getConnection() {
        return XmppConnection.getInstance();
    }

    /**
     * 判断是否已连接
     */
    public static boolean checkConnection() {
        return XmppConnection.getInstance().checkConnection();
    }

    /**
     * 判断连接是否通过了身份验证
     * 即是否已登录
     *
     * @return
     */
    public static boolean isAuthenticated() {
        return XmppConnection.getInstance().isAuthenticated();
    }

    /**
     * 打开连接
     */
    public static void exeConnection(final Activity currActivity, final IMCallback callback) {

        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (XmppConnection.getInstance().checkConfig()) {

                    final IMResult imResult = exeConnection();

                    currActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 执行连接
                            callback.onEnd(imResult);
                        }
                    });

                } else {
                    currActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onEnd(new IMResult(IMCode.ERROR, "参数初始化有误", "连接参数异常：请检查IMHelper.init(*)是否已经调用！"));
                        }
                    });
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
        if (XmppConnection.getInstance().checkConnection() || XmppConnection.getInstance().isAuthenticated()) {
            return new IMResult(IMCode.SUCCESS, "当前已是连接状态");
        }

        if (null == XmppConnection.getInstance().getConnection()) {
            return new IMResult(IMCode.ERROR, "连接异常");
        } else {
            return new IMResult(IMCode.SUCCESS, "连接成功");
        }
    }


    // ===================================================================

    /**
     * 用户登录
     *
     * @param userName
     * @param pwd
     * @return
     */
    public static IMResult login(String userName, String pwd) {

        if (isAuthenticated()) {
            disconnect();
        }

        if (!checkConnection()) {
            exeConnection();
        }

        String result = XmppConnection.getInstance().login(userName, pwd);

        if (TextUtils.isEmpty(result)) {
            return new IMResult(IMCode.SUCCESS, "登录成功");
        } else {
            disconnect();

            if (result.contains("not-authorized")) {
                return new IMResult(IMCode.ERROR, "账号或密码错误", result);
            } else {
                return new IMResult(IMCode.ERROR, "登录失败", result);
            }
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

        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onStart();
            }
        });

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


    public static IMResult disconnect() {
        try {

            XmppConnection.getInstance().logout();

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

                final IMResult result = disconnect();

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
     * 注册
     *
     * @param account
     * @param userPwd
     * @return
     */
    private static IMResult signUp(String account, String userPwd) {
        if (isAuthenticated()) {
            disconnect();
        }

        if (!checkConnection()) {
            exeConnection();
        }

        String result = XmppConnection.getInstance().register(account, userPwd);

        if (TextUtils.isEmpty(result)) {
            return new IMResult(IMCode.SUCCESS, "注册成功");
        } else {
            disconnect();

            if (result.contains("conflict")) {
                return new IMResult(IMCode.ERROR, "账号已存在", result);
            } else {
                return new IMResult(IMCode.ERROR, "注册失败", result);
            }
        }
    }

    /**
     * 注册
     *
     * @param currActivity
     * @param userName
     * @param userPwd
     * @param imCallback
     */
    public static void signUp(final Activity currActivity, final String userName, final String userPwd, final IMCallback imCallback) {

        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                final IMResult result = signUp(userName, userPwd);

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(result);
                    }
                });
            }
        }).start();
    }

    public static IMUser getCurrUser() {
        IMUser imUser = new IMUser();
        try {
            // 账号
            EntityFullJid entityFullJid = XmppConnection.getInstance().getConnection().getUser();
            Localpart localpart = entityFullJid.getLocalpart();
            imUser.setUserName(localpart.toString());
            //
            Domainpart domainpart = entityFullJid.getDomain();
            if (domainpart == null) {

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return imUser;
    }

    public static void changePassword(final Activity currActivity, final String newPwd, final IMCallback imCallback) {
        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                final IMResult result = changePassword(newPwd);

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(result);
                    }
                });
            }
        }).start();
    }

    private static IMResult changePassword(String newPwd) {
        if (!checkConnection()) {
            exeConnection();
        }

        String result = XmppConnection.getInstance().changePassword(newPwd);

        if (TextUtils.isEmpty(result)) {
            return new IMResult(IMCode.SUCCESS, "修改密码成功");
        } else {
            return new IMResult(IMCode.ERROR, "修改密码失败", result);

//            disconnect();
//            if (result.contains("conflict")) {
//                return new IMResult(IMCode.ERROR, "账号已存在", result);
//            } else {
//                return new IMResult(IMCode.ERROR, "注册失败", result);
//            }
        }
    }

    // ========================================== 其他账号 ================================================

    public static VCard getUserVCard(String nameStr) {
        return XmppConnection.getInstance().getUserVCard(nameStr);
    }


}
