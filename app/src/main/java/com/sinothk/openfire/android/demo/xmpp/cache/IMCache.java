package com.sinothk.openfire.android.demo.xmpp.cache;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.sinothk.dbHelper.DBHelper;
import com.sinothk.openfire.android.demo.model.bean.LastMessage;

import java.util.ArrayList;
import java.util.List;


public class IMCache {

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

                    "jid, currJid, avatar, name, msgTime, chatType, msgType, msgTxt, msgImg, msgLoc, msgVoice, msgVideo, msgStatus" +
                    ", msgUnread" +

                    " FROM LastMessage" +
                    " WHERE currJid=\"" + currUserJid + "\" ORDER BY msgTime DESC";

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

                    lastMsg.setChatType(dbModel.getInt("chatType"));

                    lastMsg.setMsgType(dbModel.getString("msgType"));
                    lastMsg.setMsgTxt(dbModel.getString("msgTxt"));
                    lastMsg.setMsgImg(dbModel.getString("msgImg"));
                    lastMsg.setMsgLoc(dbModel.getString("msgLoc"));
                    lastMsg.setMsgVoice(dbModel.getString("msgVoice"));
                    lastMsg.setMsgVideo(dbModel.getString("msgVideo"));

                    lastMsg.setMsgStatus(dbModel.getInt("msgStatus"));

                    lastMsg.setMsgUnread(dbModel.getInt("msgUnread"));

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
}
