package com.sinothk.openfire.android;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.sinothk.openfire.android.bean.IMCode;
import com.sinothk.openfire.android.bean.IMConstant;
import com.sinothk.openfire.android.bean.IMMessage;
import com.sinothk.openfire.android.bean.IMResult;
import com.sinothk.openfire.android.bean.IMUser;
import com.sinothk.openfire.android.inters.IMCallback;

public class SmackHelper {

    public static void init(Context mContext) {
        SmackConnection.getInstance().init(mContext);
    }

    public static void connect() {
        SmackConnection.getInstance().connect();
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

    /**
     * 用户登录
     *
     * @param userName
     * @param pwd
     * @return
     */
    public static IMResult login(String userName, String pwd) {

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            return new IMResult(IMCode.ERROR, "登录失败", "参数有误");
        }

        if (SmackConnection.getInstance().isAuthenticated()) {
            SmackConnection.getInstance().disconnect();
            SmackConnection.getInstance().connect();
        }

        if (!SmackConnection.getInstance().isConnection()) {
            SmackConnection.getInstance().connect();
        }

        String result = SmackConnection.getInstance().login(userName, pwd);

        if (TextUtils.isEmpty(result)) {
            IMUser imUser = getCurrUser();
            return new IMResult(IMCode.SUCCESS, imUser);
        } else {
            SmackConnection.getInstance().disconnect();

            if (result.contains("not-authorized")) {
                return new IMResult(IMCode.ERROR, "账号或密码错误", result);
            } else {
                return new IMResult(IMCode.ERROR, "登录失败", result);
            }
        }
    }

    public static IMUser getCurrUser() {
        try {
            return SmackConnection.getInstance().getCurrUserInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new IMUser();
    }

    /**
     * 发送消息
     *
     * @param imMessage
     */
    public static void send(IMMessage imMessage) {

        if (!SmackConnection.getInstance().isAuthenticated()) {
//            autoLogin();
        }

        if (IMConstant.ChatType.SINGLE.equals(imMessage.getChatType())) {

            switch (imMessage.getContentType()) {
                case IMConstant.ContentType.TEXT:
                    SmackChat.getInstance().sendSingleTxtMessage(imMessage.getToJid(), JSON.toJSONString(imMessage));
                    break;
                case IMConstant.ContentType.IMAGE:

                    break;
                case IMConstant.ContentType.FILE:

                    break;
                case IMConstant.ContentType.LOCATION:

                    break;
            }
        } else if (IMConstant.ChatType.ROOM.equals(imMessage.getChatType())) {

        } else if (IMConstant.ChatType.GROUP.equals(imMessage.getChatType())) {

        }
    }
}
