package com.sinothk.openfire.android;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
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
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.parts.Domainpart;
import org.jxmpp.jid.parts.Localpart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
            XmppConnection.getInstance().closeConnection();

            return new IMResult(IMCode.SUCCESS, "退出成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new IMResult(IMCode.ERROR, "退出失败", e.getMessage());
        }
    }

    /**
     * 退出登录
     *
     * @param mActivity
     * @param imCallback
     */
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

    private static IMResult logout() {
        try {
            XmppConnection.getInstance().logout();

            return new IMResult(IMCode.SUCCESS, "退出成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new IMResult(IMCode.ERROR, "退出失败", e.getMessage());
        }
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

    /**
     * 注册
     *
     * @param currActivity
     * @param newUser
     * @param imCallback
     */
    public static void signUp(final Activity currActivity, final IMUser newUser, final IMCallback imCallback) {

        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                final IMResult result = signUp(newUser);

                currActivity.runOnUiThread(new Runnable() {
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
     * @param newUser
     * @return
     */
    private static IMResult signUp(IMUser newUser) {
        if (isAuthenticated()) {
            disconnect();
        }

        if (!checkConnection()) {
            exeConnection();
        }

        String result = XmppConnection.getInstance().register(newUser);

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

    public static IMUser getCurrUser() {
        try {

            IMUser imUser = XmppConnection.getInstance().getCurrUserInfo();


//            // 账号
//            EntityFullJid entityFullJid = XmppConnection.getInstance().getConnection().getUser();
//            Localpart localpart = entityFullJid.getLocalpart();
//            imUser.setUserName(localpart.toString());
//            //
//            Domainpart domainpart = entityFullJid.getDomain();
//            if (domainpart == null) {
//
//            }
            return imUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new IMUser();
    }

    /**
     * 修改密码
     *
     * @param currActivity
     * @param newPwd
     * @param imCallback
     */
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

    /**
     * 修改密码
     *
     * @param newPwd
     * @return
     */
    private static IMResult changePassword(String newPwd) {
        if (!checkConnection()) {
            exeConnection();
        }

        String result = XmppConnection.getInstance().changePassword(newPwd);

        if (TextUtils.isEmpty(result)) {
            return new IMResult(IMCode.SUCCESS, "修改密码成功");
        } else {
            return new IMResult(IMCode.ERROR, "修改密码失败", result);
        }
    }

    /**
     * 设置用户状态
     *
     * @param currActivity
     * @param status
     * @param imCallback
     */
    public static void setUserStatus(final Activity currActivity, final int status, final IMCallback imCallback) {
        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                final IMResult result = setUserStatus(status);

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(result);
                    }
                });
            }
        }).start();
    }

    /**
     * 设置用户状态
     *
     * @param status
     * @return
     */
    private static IMResult setUserStatus(int status) {
        if (!checkConnection()) {
            exeConnection();
        }

        String result = XmppConnection.getInstance().setPresence(status);

        if (TextUtils.isEmpty(result)) {
            return new IMResult(IMCode.SUCCESS, "设置成功");
        } else {
            return new IMResult(IMCode.ERROR, "设置失败", result);
        }
    }

    /**
     * 删除账号
     *
     * @param currActivity
     * @param imCallback
     */
    public static void deleteAccount(final Activity currActivity, final IMCallback imCallback) {
        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                final IMResult result = deleteAccount();

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(result);
                    }
                });
            }
        }).start();
    }

    /**
     * 删除账号
     *
     * @return
     */
    private static IMResult deleteAccount() {
        if (!checkConnection()) {
            exeConnection();
        }

        String result = XmppConnection.getInstance().deleteAccount();

        if (TextUtils.isEmpty(result)) {
            return new IMResult(IMCode.SUCCESS, "删除成功");
        } else {
            return new IMResult(IMCode.ERROR, "删除失败", result);
        }
    }

    // ========================================== 其他账号 ================================================

    public static void getUserInfo(final Activity currActivity, final String jid, final IMCallback imCallback) {

        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                IMResult imResult = new IMResult();

                final RosterEntry rosterEntry = XmppConnection.getInstance().getUserInfo(jid);
                if (rosterEntry != null) {
                    IMUser imUser = new IMUser();
                    imUser.setJid(rosterEntry.getJid().toString());
                    Localpart localpart = rosterEntry.getJid().getLocalpartOrNull();
                    imUser.setUserName(localpart.toString());

                    imUser.setName(rosterEntry.getName());
                    imUser.setGroupNames(rosterEntry.getGroups());

                    imResult = new IMResult(IMCode.SUCCESS, imUser);
                } else {
                    imResult = new IMResult(IMCode.ERROR, "查询信息为空");
                }

                final IMResult finalImResult = imResult;

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(finalImResult);
                    }
                });
            }
        }).start();
    }

    public static void addFriend(final Activity currActivity, final String userName, final String name, final IMCallback imCallback) {
        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                IMResult result;

                String resultVal = XmppConnection.getInstance().addUser(userName, name);
                if (TextUtils.isEmpty(resultVal)) {
                    result = new IMResult(IMCode.SUCCESS, "添加成功");
                } else {
                    result = new IMResult(IMCode.ERROR, "添加失败", resultVal);
                }

                final IMResult finalResult = result;

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(finalResult);
                    }
                });
            }
        }).start();
    }

    public static void deleteFriend(final Activity currActivity, final String userName, final String name, final IMCallback imCallback) {
        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                IMResult result;

                String resultVal = XmppConnection.getInstance().removeUser(userName);
                if (TextUtils.isEmpty(resultVal)) {
                    result = new IMResult(IMCode.SUCCESS, "删除成功");
                } else {
                    result = new IMResult(IMCode.ERROR, "删除失败", resultVal);
                }

                final IMResult finalResult = result;

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(finalResult);
                    }
                });
            }
        }).start();
    }

    /**
     * 获得好友信息
     *
     * @param currActivity
     * @param imCallback
     */
    public static void getFriends(final Activity currActivity, final IMCallback imCallback) {

        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                IMResult result;
                try {
                    ArrayList<IMUser> userList = new ArrayList<>();

                    List<RosterEntry> rosterEntry = XmppConnection.getInstance().getAllEntries();
                    if (rosterEntry != null) {

                        for (RosterEntry roster : rosterEntry) {
                            IMUser imUser = new IMUser();
                            imUser.setJid(roster.getJid().toString());
                            imUser.setName(roster.getName());
                            imUser.setItemType(roster.getType());

                            userList.add(imUser);
                        }
                    }

                    result = new IMResult(IMCode.SUCCESS, userList);
                } catch (Exception e) {
                    result = new IMResult(IMCode.ERROR, "获取失败", e.getMessage());
                }

                final IMResult finalResult = result;

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(finalResult);
                    }
                });
            }
        }).start();


    }

    public static void searchUser(final Activity currActivity, final String userName, final IMCallback imCallback) {
        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imCallback.onStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                IMResult result;
                try {
                    ArrayList<IMUser> results = XmppConnection.getInstance().searchUsers(userName);
                    result = new IMResult(IMCode.SUCCESS, results);
                } catch (Exception e) {
                    result = new IMResult(IMCode.ERROR, "获取失败", e.getMessage());
                }

                final IMResult finalResult = result;

                currActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imCallback.onEnd(finalResult);
                    }
                });
            }
        }).start();
    }

    public static boolean isConfig() {
        return XmppConnection.getInstance().isConfig();
    }
}
