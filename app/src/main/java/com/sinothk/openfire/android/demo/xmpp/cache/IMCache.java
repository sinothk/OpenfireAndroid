package com.sinothk.openfire.android.demo.xmpp.cache;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.sinothk.comm.utils.LogUtil;
import com.sinothk.openfire.android.bean.IMMessage;
import com.sinothk.openfire.android.demo.BuildConfig;
import com.sinothk.openfire.android.demo.model.bean.LastMessage;
import com.sinothk.storage.dbhelper.DBHelper;

import java.util.ArrayList;
import java.util.List;


public class IMCache {

    private volatile static IMCache singleton;

    public static IMCache getInstance() {
        if (singleton == null) {
            synchronized (IMCache.class) {
                if (singleton == null) {
                    singleton = new IMCache();
                }
            }
        }
        return singleton;
    }

    /**
     * 保存最新消息
     *
     * @param mContext
     * @param lastMsg
     * @return
     */
    public static boolean saveOrUpdateLastMsg(Context mContext, LastMessage lastMsg) {
        if (mContext == null || lastMsg == null) {
            return false;
        }
        return DBHelper.with(mContext).saveOrUpdate(lastMsg);
    }


    /**
     * 获取当前登录人的最新消息列表
     *
     * @param mContext
     * @return
     */
    public static ArrayList<LastMessage> findMyLastMsg(Context mContext, String currUserJid) {
        if (mContext == null) {
            return new ArrayList<>();
        }

        try {

            String sql = "SELECT " +
                    "jid, " +
                    "currJid, " +
                    "avatar, " +
                    "name, " +
                    "msgTime, " +
                    "chatType, " +
                    "msgType, " +
                    "msgTxt, " +
                    "msgImg, " +
                    "msgLoc, " +
                    "msgVoice, " +
                    "msgVideo, " +
                    "msgStatus, " + // 消息状态：发送失败:-2，正在发送:-1，已读：0，未读：1，已发送:2，已接收:3等

                    "(SELECT COUNT(*) FROM IMMessage" +

                    " WHERE toJid = \"" + currUserJid + "\" AND fromJid = lm.jid AND msgStatus = 1) unreadNum" +

                    " FROM LastMessage lm" +

                    " WHERE currJid=\"" + currUserJid + "\" "
                    + " ORDER BY msgTime DESC";

            List<DbModel> dbModels = DBHelper.with(mContext).db().findDbModelAll(new SqlInfo(sql)); // 自定义sql查询

            ArrayList<LastMessage> lastMsgList = new ArrayList<>();
            if (dbModels != null && dbModels.size() > 0) {
                for (DbModel dbModel : dbModels) {
                    LastMessage lastMsg = new LastMessage();

                    lastMsg.setJid(dbModel.getString("jid"));
                    lastMsg.setCurrJid(dbModel.getString("currJid"));

                    lastMsg.setAvatar(dbModel.getString("avatar"));
                    lastMsg.setName(dbModel.getString("name"));
                    lastMsg.setMsgTime(dbModel.getLong("msgTime"));

                    lastMsg.setChatType(dbModel.getString("chatType"));

                    lastMsg.setMsgType(dbModel.getString("msgType"));
                    lastMsg.setMsgTxt(dbModel.getString("msgTxt"));
                    lastMsg.setMsgImg(dbModel.getString("msgImg"));
                    lastMsg.setMsgLoc(dbModel.getString("msgLoc"));
                    lastMsg.setMsgVoice(dbModel.getString("msgVoice"));
                    lastMsg.setMsgVideo(dbModel.getString("msgVideo"));

                    lastMsg.setMsgStatus(dbModel.getInt("msgStatus"));

                    lastMsg.setMsgUnread(dbModel.getInt("unreadNum"));

                    if (BuildConfig.DEBUG) {
                        LogUtil.e(IMCache.class, "sql = " + sql);

                        LogUtil.e(IMCache.class, "unreadNum = " + lastMsg.getMsgUnread());
                    }

                    lastMsgList.add(lastMsg);
                }

                return lastMsgList;
            }
            return new ArrayList<>();
        } catch (DbException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 保存消息
     *
     * @param context
     * @param msg
     * @return
     */
    public static boolean saveOrUpdateMsg(Context context, IMMessage msg) {
        if (context == null || msg == null) {
            return false;
        }
        return DBHelper.with(context).saveOrUpdate(msg);
    }

    /**
     * 获得聊天记录：
     *
     * @param mContext
     * @param toJid
     * @param currUserJid
     * @return
     */
    public static ArrayList<IMMessage> findChatMsg(Context mContext, String toJid, String currUserJid) {
        if (mContext == null) {
            return new ArrayList<>();
        }

        try {
            String sql = "SELECT " +

                    "msgId, " + // id

                    "fromJid, " +
                    "fromName, " +
                    "fromUserAvatar, " +
                    "toJid, " +
                    "toName, " +
                    "toUserAvatar, " +

                    "chatType, " + // 消息分类：单聊，群聊，通知等
                    "fromType, " + // 发送分类：发送，接收等
                    "msgTime, " + // 消息时间

                    "contentType, " + // 内容类型：文本，图片，文件，位置等。

                    "msgTxt, " +
                    "msgImg, " +
                    "msgLoc, " +
                    "msgVoice, " +
                    "msgVideo, " +

                    "msgStatus" +   // 消息状态：发送失败:-2，正在发送:-1，已读：0，未读：1，已发送:2，已接收:3等

                    " FROM IMMessage" +
                    " WHERE " +

                    "toJid = \"" + currUserJid + "\" AND fromJid = \"" + toJid + "\"" +

                    " OR toJid = \"" + toJid + "\" AND fromJid = \"" + currUserJid + "\"" +

                    " ORDER BY msgTime ASC";

            List<DbModel> dbModels = DBHelper.with(mContext).db().findDbModelAll(new SqlInfo(sql)); // 自定义sql查询
            ArrayList<IMMessage> msgList = new ArrayList<>();

            if (dbModels != null && dbModels.size() > 0) {
                for (DbModel dbModel : dbModels) {

                    IMMessage msg = new IMMessage();
                    msg.setMsgId(dbModel.getString("msgId"));

                    msg.setFromJid(dbModel.getString("fromJid"));
                    msg.setFromName(dbModel.getString("fromName"));
                    msg.setFromUserAvatar(dbModel.getString("fromUserAvatar"));

                    msg.setToJid(dbModel.getString("toJid"));
                    msg.setToName(dbModel.getString("toName"));
                    msg.setToUserAvatar(dbModel.getString("toUserAvatar"));

                    // 消息分类：单聊，群聊，通知等
                    msg.setChatType(dbModel.getString("chatType"));
                    msg.setFromType(dbModel.getString("fromType"));
                    msg.setMsgTime(dbModel.getLong("msgTime"));

                    // 内容类型：文本，图片，文件，位置等。
                    msg.setContentType(dbModel.getString("contentType"));

                    msg.setMsgTxt(dbModel.getString("msgTxt"));
                    msg.setMsgImg(dbModel.getString("msgImg"));
                    msg.setMsgLoc(dbModel.getString("msgLoc"));
                    msg.setMsgVoice(dbModel.getString("msgVoice"));
                    msg.setMsgVideo(dbModel.getString("msgVideo"));

                    msg.setMsgStatus(dbModel.getInt("msgStatus"));

                    msgList.add(msg);
                }
                return msgList;
            }
            return new ArrayList<>();
        } catch (DbException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public int findAllMsgUnread(Context mContext, String currJid) {
        try {
            String sql = "SELECT COUNT(*) allUnread FROM IMMessage WHERE toJid = \"" + currJid + "\" AND msgStatus = 1";
            List<DbModel> dbModels = DBHelper.with(mContext).db().findDbModelAll(new SqlInfo(sql));
            if (dbModels != null && dbModels.size() > 0) {
                DbModel dbModel = dbModels.get(0);
                int allUnread = dbModel.getInt("allUnread");

                if (BuildConfig.DEBUG) {
                    LogUtil.e(IMCache.class, "sql = " + sql);
                    LogUtil.e(IMCache.class, "allUnread = " + allUnread);
                }
                return allUnread;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
