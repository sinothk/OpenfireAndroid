//package com.sinothk.openfire.android;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.text.TextUtils;
//
//import com.alibaba.fastjson.JSON;
//import com.sinothk.openfire.android.bean.IMChatRoom;
//import com.sinothk.openfire.android.bean.IMMessage;
//import com.sinothk.openfire.android.bean.IMCode;
//import com.sinothk.openfire.android.bean.IMConstant;
//import com.sinothk.openfire.android.bean.IMResult;
//import com.sinothk.openfire.android.bean.IMUser;
//import com.sinothk.openfire.android.inters.IMCallback;
//import com.sinothk.openfire.android.keep.ConnectionService;
//import com.sinothk.openfire.android.xmpp.XmppConnection;
//
//import org.jivesoftware.smack.chat2.Chat;
//import org.jivesoftware.smack.chat2.ChatManager;
//import org.jivesoftware.smack.roster.RosterEntry;
//import org.jivesoftware.smack.roster.RosterGroup;
//import org.jivesoftware.smackx.muc.HostedRoom;
//import org.jivesoftware.smackx.muc.MultiUserChat;
//import org.jxmpp.jid.parts.Localpart;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * @ author LiangYT
// * @ create 2018/10/18 15:17
// * @ Describe
// */
//public class IMHelper {
//
//    private static String TAG = IMHelper.class.getSimpleName();
//
//    // =================================连接部分==============================================
//    public static void init(String server_name, String server_ip, int server_port) {
//        XmppConnection.init(server_name, server_ip, server_port);
//    }
//
//    public static XmppConnection getConnection() {
//        return XmppConnection.getInstance();
//    }
//
//    /**
//     * 判断服务器配置是否可用
//     *
//     * @return
//     */
//    public static boolean isConfig() {
//        return XmppConnection.getInstance().isConfig();
//    }
//
//    /**
//     * 判断是否已连接
//     */
//    public static boolean checkConnection() {
//        return XmppConnection.getInstance().checkConnection();
//    }
//
//    /**
//     * 判断连接是否通过了身份验证
//     * 即是否已登录
//     *
//     * @return
//     */
//    public static boolean isAuthenticated() {
//        return XmppConnection.getInstance().isAuthenticated();
//    }
//
//    /**
//     * 连接
//     *
//     * @return
//     */
//    public static IMResult exeConnection() {
//        if (XmppConnection.getInstance().checkConnection() || XmppConnection.getInstance().isAuthenticated()) {
//            return new IMResult(IMCode.SUCCESS, "当前已是连接状态");
//        }
//
//        if (null == XmppConnection.getInstance().getConnection()) {
//            return new IMResult(IMCode.ERROR, "连接异常");
//        } else {
//            return new IMResult(IMCode.SUCCESS, "连接成功");
//        }
//    }
//
//
//    // ===============================当前用户部分====================================
//
//    /**
//     * 用户登录
//     *
//     * @param userName
//     * @param pwd
//     * @return
//     */
//    public static IMResult login(String userName, String pwd) {
//        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
//            return new IMResult(IMCode.ERROR, "登录失败", "参数有误");
//        }
//
//        if (isAuthenticated()) {
//            disconnect();
//        }
//
//        if (!checkConnection()) {
//            exeConnection();
//        }
//
//        String result = XmppConnection.getInstance().login(userName, pwd);
//
//        if (TextUtils.isEmpty(result)) {
//            IMUser imUser = getCurrUser();
//            return new IMResult(IMCode.SUCCESS, imUser);
//        } else {
//            disconnect();
//
//            if (result.contains("not-authorized")) {
//                return new IMResult(IMCode.ERROR, "账号或密码错误", result);
//            } else {
//                return new IMResult(IMCode.ERROR, "登录失败", result);
//            }
//        }
//    }
//
//
//    /**
//     * 用户登录
//     *
//     * @param userName
//     * @param pwd
//     * @return
//     */
//    public static void login(final Activity currActivity, final String userName, final String pwd, final IMCallback callback) {
//
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                callback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                final IMResult result = login(userName, pwd);
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        callback.onEnd(result);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 自动登录
//     *
//     * @param currActivity
//     * @param callback
//     */
//    public static void autoLogin(final Activity currActivity, final IMCallback callback) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final IMResult result = autoLogin();
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        callback.onEnd(result);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 自动登录
//     *
//     * @return
//     */
//    public static IMResult autoLogin() {
//        final String userName = IMCache.getUserName();
//        final String pwd = IMCache.getUserPwd();
//
//        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
//            return new IMResult(IMCode.ERROR, "登录失败", "登录参数为空");
//        }
//
//        if (!checkConnection()) {
//            exeConnection();
//        }
//
//        // 登录
//        String result = XmppConnection.getInstance().login(userName, pwd);
//        if (TextUtils.isEmpty(result)) {
//            IMUser imUser = getCurrUser();
//            return new IMResult(IMCode.SUCCESS, imUser);
//        } else {
//            disconnect();
//
//            if (result.contains("not-authorized")) {
//                return new IMResult(IMCode.ERROR, "账号或密码错误", result);
//            } else {
//                return new IMResult(IMCode.ERROR, "登录失败", result);
//            }
//        }
//    }
//
//    /**
//     * 退出
//     *
//     * @return
//     */
//    public static IMResult disconnect() {
//        try {
//            XmppConnection.getInstance().closeConnection();
//
//            return new IMResult(IMCode.SUCCESS, "退出成功");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new IMResult(IMCode.ERROR, "退出失败", e.getMessage());
//        }
//    }
//
//    /**
//     * 退出登录
//     *
//     * @param mActivity
//     * @param imCallback
//     */
//    public static void logout(final Activity mActivity, final IMCallback imCallback) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                final IMResult result = logout();
//
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(result);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    private static IMResult logout() {
//        try {
//            XmppConnection.getInstance().logout();
//
//            return new IMResult(IMCode.SUCCESS, "退出成功");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new IMResult(IMCode.ERROR, "退出失败", e.getMessage());
//        }
//    }
//
//    /**
//     * 注册
//     *
//     * @param account
//     * @param userPwd
//     * @return
//     */
//    private static IMResult signUp(String account, String userPwd) {
//        if (isAuthenticated()) {
//            disconnect();
//        }
//
//        if (!checkConnection()) {
//            exeConnection();
//        }
//
//        String result = XmppConnection.getInstance().register(account, userPwd);
//
//        if (TextUtils.isEmpty(result)) {
//            return new IMResult(IMCode.SUCCESS, "注册成功");
//        } else {
//            disconnect();
//
//            if (result.contains("conflict")) {
//                return new IMResult(IMCode.ERROR, "账号已存在", result);
//            } else {
//                return new IMResult(IMCode.ERROR, "注册失败", result);
//            }
//        }
//    }
//
////    /**
////     * 注册
////     *
////     * @param currActivity
////     * @param userName
////     * @param userPwd
////     * @param imCallback
////     */
////    public static void signUp(final Activity currActivity, final String userName, final String userPwd, final IMCallback imCallback) {
////
////        currActivity.runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                imCallback.onStart();
////            }
////        });
////
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////
////                final IMResult result = signUp(userName, userPwd);
////
////                currActivity.runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        imCallback.onEnd(result);
////                    }
////                });
////            }
////        }).start();
////    }
//
//    /**
//     * 注册
//     *
//     * @param currActivity
//     * @param newUser
//     * @param imCallback
//     */
//    public static void signUp(final Activity currActivity, final IMUser newUser, final IMCallback imCallback) {
//
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                final IMResult result = signUp(newUser);
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(result);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 注册
//     *
//     * @param newUser
//     * @return
//     */
//    private static IMResult signUp(IMUser newUser) {
//        if (isAuthenticated()) {
//            disconnect();
//        }
//
//        if (!checkConnection()) {
//            exeConnection();
//        }
//
//        String result = XmppConnection.getInstance().register(newUser);
//
//        if (TextUtils.isEmpty(result)) {
//            return new IMResult(IMCode.SUCCESS, "注册成功");
//        } else {
//            disconnect();
//
//            if (result.contains("conflict")) {
//                return new IMResult(IMCode.ERROR, "账号已存在", result);
//            } else {
//                return new IMResult(IMCode.ERROR, "注册失败", result);
//            }
//        }
//    }
//
//    public static IMUser getCurrUser() {
//        try {
//            return XmppConnection.getInstance().getCurrUserInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new IMUser();
//    }
//
//    /**
//     * 修改密码
//     *
//     * @param currActivity
//     * @param newPwd
//     * @param imCallback
//     */
//    public static void changePassword(final Activity currActivity, final String newPwd, final IMCallback imCallback) {
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                final IMResult result = changePassword(newPwd);
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(result);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 修改密码
//     *
//     * @param newPwd
//     * @return
//     */
//    private static IMResult changePassword(String newPwd) {
//        if (!checkConnection()) {
//            exeConnection();
//        }
//
//        String result = XmppConnection.getInstance().changePassword(newPwd);
//
//        if (TextUtils.isEmpty(result)) {
//            return new IMResult(IMCode.SUCCESS, "修改密码成功");
//        } else {
//            return new IMResult(IMCode.ERROR, "修改密码失败", result);
//        }
//    }
//
//    /**
//     * 设置用户状态
//     *
//     * @param currActivity
//     * @param status
//     * @param imCallback
//     */
//    public static void setUserStatus(final Activity currActivity, final int status, final IMCallback imCallback) {
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                final IMResult result = setUserStatus(status);
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(result);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 设置用户状态
//     *
//     * @param status
//     * @return
//     */
//    private static IMResult setUserStatus(int status) {
//        if (!checkConnection()) {
//            exeConnection();
//        }
//
//        String result = XmppConnection.getInstance().setPresence(status);
//
//        if (TextUtils.isEmpty(result)) {
//            return new IMResult(IMCode.SUCCESS, "设置成功");
//        } else {
//            return new IMResult(IMCode.ERROR, "设置失败", result);
//        }
//    }
//
//    /**
//     * 删除账号
//     *
//     * @param currActivity
//     * @param imCallback
//     */
//    public static void deleteAccount(final Activity currActivity, final IMCallback imCallback) {
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                final IMResult result = deleteAccount();
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(result);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 删除账号
//     *
//     * @return
//     */
//    private static IMResult deleteAccount() {
//        if (!checkConnection()) {
//            exeConnection();
//        }
//
//        String result = XmppConnection.getInstance().deleteAccount();
//
//        if (TextUtils.isEmpty(result)) {
//            return new IMResult(IMCode.SUCCESS, "删除成功");
//        } else {
//            return new IMResult(IMCode.ERROR, "删除失败", result);
//        }
//    }
//
//    // ========================================== 其他账号 ================================================
//
//    public static void getUserInfo(final Activity currActivity, final String jid, final IMCallback imCallback) {
//
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                IMResult imResult = new IMResult();
//
//                final RosterEntry rosterEntry = XmppConnection.getInstance().getUserInfo(jid);
//                if (rosterEntry != null) {
//                    IMUser imUser = new IMUser();
//
//                    imUser.setJid(rosterEntry.getJid().toString());
//
//                    Localpart localpart = rosterEntry.getJid().getLocalpartOrNull();
//                    imUser.setUserName(localpart.toString());
//
//                    imUser.setName(rosterEntry.getName());
//                    imUser.setGroupNames(rosterEntry.getGroups());
//
//                    imResult = new IMResult(IMCode.SUCCESS, imUser);
//                } else {
//                    imResult = new IMResult(IMCode.ERROR, "查询信息为空");
//                }
//
//                final IMResult finalImResult = imResult;
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(finalImResult);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 添加好友
//     *
//     * @param currActivity
//     * @param userName
//     * @param name
//     * @param imCallback
//     */
//    public static void addFriend(final Activity currActivity, final String userName, final String name, final IMCallback imCallback) {
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                IMResult result;
//
//                String resultVal = XmppConnection.getInstance().addUser(userName, name);
//                if (TextUtils.isEmpty(resultVal)) {
//                    result = new IMResult(IMCode.SUCCESS, "添加成功");
//                } else {
//                    result = new IMResult(IMCode.ERROR, "添加失败", resultVal);
//                }
//
//                final IMResult finalResult = result;
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(finalResult);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 删除好友关系
//     *
//     * @param currActivity
//     * @param userName
//     * @param name
//     * @param imCallback
//     */
//    public static void deleteFriend(final Activity currActivity, final String userName, final String name, final IMCallback imCallback) {
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                IMResult result;
//
//                String resultVal = XmppConnection.getInstance().removeUser(userName);
//                if (TextUtils.isEmpty(resultVal)) {
//                    result = new IMResult(IMCode.SUCCESS, "删除成功");
//                } else {
//                    result = new IMResult(IMCode.ERROR, "删除失败", resultVal);
//                }
//
//                final IMResult finalResult = result;
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(finalResult);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 获得好友信息
//     *
//     * @param currActivity
//     * @param imCallback
//     */
//    public static void getFriends(final Activity currActivity, final IMCallback imCallback) {
//
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                IMResult result;
//                try {
//                    ArrayList<IMUser> userList = new ArrayList<>();
//
//                    List<RosterEntry> rosterEntry = XmppConnection.getInstance().getAllEntries();
//                    if (rosterEntry != null) {
//
//                        for (RosterEntry roster : rosterEntry) {
//                            IMUser imUser = new IMUser();
//
//                            String jid = roster.getJid().toString();
//                            if (TextUtils.isEmpty(jid)) {
//                                continue;
//                            } else {
//                                imUser.setJid(jid);
//                            }
//
//                            String name = roster.getName();
//                            if (TextUtils.isEmpty(name)) {
//                                imUser.setName(jid.substring(0, jid.indexOf("@")));
//                            } else {
//                                imUser.setName(name);
//                            }
//
//                            imUser.setItemType(roster.getType());
//
//                            // 获取头像
//                            imUser.setUserAvatar(XmppConnection.getInstance().getUserImage(jid));
//
//                            userList.add(imUser);
//                        }
//                    }
//
//                    result = new IMResult(IMCode.SUCCESS, userList);
//                } catch (Exception e) {
//                    result = new IMResult(IMCode.ERROR, "获取失败", e.getMessage());
//                }
//
//                final IMResult finalResult = result;
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(finalResult);
//                    }
//                });
//            }
//        }).start();
//
//
//    }
//
//    /**
//     * 搜索用户
//     *
//     * @param currActivity
//     * @param userName
//     * @param imCallback
//     */
//    public static void searchUser(final Activity currActivity, final String userName, final IMCallback imCallback) {
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                IMResult result;
//                try {
//                    ArrayList<IMUser> results = XmppConnection.getInstance().searchUsers(userName);
//                    result = new IMResult(IMCode.SUCCESS, results);
//                } catch (Exception e) {
//                    result = new IMResult(IMCode.ERROR, "获取失败", e.getMessage());
//                }
//
//                final IMResult finalResult = result;
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(finalResult);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    public static void findMyGroups(final Activity currActivity, final String userName, final IMCallback imCallback) {
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                IMResult result;
//                try {
//                    result = findMyGroups();
//                } catch (Exception e) {
//                    result = new IMResult(IMCode.ERROR, "获取失败", e.getMessage());
//                }
//
//                final IMResult finalResult = result;
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(finalResult);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    private static IMResult findMyGroups() {
//        try {
//            List<RosterGroup> list = XmppConnection.getInstance().getGroups();
//            return new IMResult(IMCode.SUCCESS, list);
//        } catch (Exception e) {
//            return new IMResult(IMCode.ERROR, "获取失败", e.getMessage());
//        }
//    }
//
//    public static void updateUser(final Activity currActivity, IMUser userInfo, final IMCallback imCallback) {
////        currActivity.runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                imCallback.onStart();
////            }
////        });
////
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                IMResult result;
////                try {
////
////                    ArrayList<IMUser> results = XmppConnection.getInstance().searchUsers(userName);
////
////                    result = new IMResult(IMCode.SUCCESS, results);
////                } catch (Exception e) {
////                    result = new IMResult(IMCode.ERROR, "获取失败", e.getMessage());
////                }
////
////                final IMResult finalResult = result;
////
////                currActivity.runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        imCallback.onEnd(finalResult);
////                    }
////                });
////            }
////        }).start();
//    }
//
//    // ======================================消息发送部分==================================================
////    public static Chat createFriendChat(String chatTarget) {
//////        return XmppConnection.getInstance().getFriendChat(chatTarget);
//////    }
//////
//////    @Deprecated
//////    public static void sendTxtMsg(Chat chat, String msg) {
//////        try {
//////            XmppConnection.getInstance().sendSingleMessage(chat, msg);
//////        } catch (Exception e) {
//////            e.printStackTrace();
//////        }
//////    }
//
//    public static ChatManager getChatManager() {
//        return XmppConnection.getInstance().getChatManager();
//    }
//
//    /**
//     * 获得历史聊天记录
//     *
//     * @param chatList
//     * @return
//     */
//    public static ArrayList<IMMessage> getChatList(ArrayList<IMMessage> chatList) {
//        for (int i = 0; i < 5; i++) {
//            IMMessage message = new IMMessage();
//
//            if (i % 2 == 0) {
//                message.setFromType(IMConstant.FromType.SEND);
//            } else {
//                message.setFromType(IMConstant.FromType.RECEIVE);
//            }
//
//            message.setContentType(IMConstant.ContentType.CONTENT_TEXT);
//            message.setMsgTxt("聊天内容_" + i);
//
//            message.setFromJid("");
//
//            chatList.add(message);
//        }
//
//        return chatList;
//    }
//
//    /**
//     * 发送文本消息
//     *
//     * @param msgBody
//     */
//    @Deprecated
//    public static void sendTxtMessage(IMMessage msgBody) {
//        try {
//            XmppConnection.getInstance().sendTxtMessage(msgBody.getJid(), msgBody.getMsgTxt());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * ========================================= 房间 ===============================================================
//     */
//    /**
//     * 创建房间
//     *
//     * @param currActivity
//     * @param imChatRoom
//     * @param imCallback
//     */
//    public static void createChartRoom(final Activity currActivity, final IMChatRoom imChatRoom, final IMCallback imCallback) {
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final IMResult result = createChartRoom(imChatRoom);
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(result);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    /**
//     * 创建房间
//     *
//     * @param imChatRoom
//     * @return
//     */
//    private static IMResult createChartRoom(IMChatRoom imChatRoom) {
//
//        MultiUserChat muc = XmppConnection.getInstance().createChatRoom(imChatRoom);
//
//        if (muc != null) {
//            return new IMResult(IMCode.SUCCESS, muc);
//        } else {
//            return new IMResult(IMCode.ERROR, "获取失败");
//        }
//    }
//
//    /**
//     * 发布房间消息
//     *
//     * @param multiUserChat
//     * @param msgBody
//     */
//    public static void sendRoom(MultiUserChat multiUserChat, IMMessage msgBody) {
//        XmppConnection.getInstance().sendGroupMessage(multiUserChat, msgBody.getMsgTxt());
//    }
//
//    /**
//     * 发布房间消息
//     *
//     * @param roomName
//     * @param msgBody
//     */
//    public static void sendRoom(String roomName, IMMessage msgBody) {
//        XmppConnection.getInstance().sendGroupMessage(roomName, msgBody.getMsgTxt());
//    }
//
//    /**
//     * 获得所有聊天室
//     *
//     * @param currActivity
//     * @param imCallback
//     */
//    public static void getHostAllRooms(final Activity currActivity, final IMCallback imCallback) {
//        currActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imCallback.onStart();
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                IMResult result;
//                try {
//
//                    result = getHostAllRooms();
//
//                } catch (Exception e) {
//                    result = new IMResult(IMCode.ERROR, "获取失败", e.getMessage());
//                }
//
//                final IMResult finalResult = result;
//
//                currActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imCallback.onEnd(finalResult);
//                    }
//                });
//            }
//        }).start();
//    }
//
//    private static IMResult getHostAllRooms() {
//        try {
//            ArrayList<IMChatRoom> list = XmppConnection.getInstance().getHostAllRooms();
//            return new IMResult(IMCode.SUCCESS, list);
//        } catch (Exception e) {
//            return new IMResult(IMCode.ERROR, "获取失败", e.getMessage());
//        }
//    }
//
//
//    public static void roomLeave(MultiUserChat multiUserChat) {
//        if (multiUserChat == null) {
//            return;
//        }
//
//        XmppConnection.getInstance().roomLeave(multiUserChat);
//    }
//
//    public static int getRoomMemberSize(MultiUserChat multiUserChat) {
//        try {
//            return multiUserChat.getOutcasts().size();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    /**
//     * 进入无密码聊天室
//     *
//     * @param roomJid
//     * @param myNickname
//     * @return
//     */
//    public static MultiUserChat joinMultiUserChat(String roomJid, String myNickname) {
//        return XmppConnection.getInstance().joinMultiUserChat(roomJid, myNickname);
//    }
//
//    /**
//     * 进入有密码聊天室
//     *
//     * @param roomJid
//     * @param myNickname
//     * @param roomPwd
//     * @return
//     */
//    public static MultiUserChat joinMultiUserChat(String roomJid, String myNickname, String roomPwd) {
//        return XmppConnection.getInstance().joinMultiUserChat(roomJid, myNickname, roomPwd);
//    }
//
//    /**
//     * 自动登录
//     *
//     * @return
//     */
//    public static boolean isAutoLogin() {
//        return false;
//    }
//
//    public static boolean openConnection() {
//        return XmppConnection.getInstance().getConnection() != null;
//    }
//
//    public static Chat getFriendChat(String jid) {
//        return XmppConnection.getInstance().getFriendChat(jid);
//    }
//
//    /**
//     * 发送消息
//     *
//     * @param imMessage
//     */
//    public static void send(IMMessage imMessage) {
//
//        if (!XmppConnection.getInstance().isAuthenticated()) {
//            autoLogin();
//        }
//
//        if (IMConstant.ChatType.SINGLE.equals(imMessage.getChatType())) {
//
//            switch (imMessage.getContentType()) {
//                case IMConstant.ContentType.TEXT:
//                    XmppConnection.getInstance().sendSingleTxtMessage(imMessage.getToJid(), JSON.toJSONString(imMessage));
//                    break;
//                case IMConstant.ContentType.IMAGE:
//
//                    break;
//                case IMConstant.ContentType.FILE:
//
//                    break;
//                case IMConstant.ContentType.LOCATION:
//
//                    break;
//            }
//        } else if (IMConstant.ChatType.ROOM.equals(imMessage.getChatType())) {
//
//        } else if (IMConstant.ChatType.GROUP.equals(imMessage.getChatType())) {
//
//        }
//    }
//
//    /**
//     * ============================  启动服务  ========================================
//     */
//
//    public static void startKeepAliveService(Activity mActivity) {
//        //连续启动Service
//        Intent intentConnection = new Intent(mActivity, ConnectionService.class);
//        mActivity.startService(intentConnection);
//    }
//
//    public static void stopKeepAliveService(Activity mActivity) {
//        //停止Service
//        if (mActivity != null) {
//            // 停止服务，同时取消闹钟定时
//            Intent intentConnection = new Intent(mActivity, ConnectionService.class);
//            mActivity.stopService(intentConnection);
//        }
//    }
//}
