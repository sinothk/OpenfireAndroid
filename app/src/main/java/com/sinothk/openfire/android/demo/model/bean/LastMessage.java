package com.sinothk.openfire.android.demo.model.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.sinothk.openfire.android.IMHelper;
import com.sinothk.openfire.android.bean.IMConstant;

import org.jivesoftware.smack.packet.Message;

import java.util.Date;

@Table(name = "LastMessage")
public class LastMessage {

    @Id
    private String jid; // 对方jid
    private String currJid; // 当前用户jid

    private String avatar; // 显示头像
    private String name; // 显示名字
    private long msgTime;

    private int chatType; // 消息分类：单聊，群聊，通知等

    private String msgType; // 内容类型：文本，图片，文件，位置等。

    private String msgTxt;
    private String msgImg;
    private String msgLoc;
    private String msgVoice;
    private String msgVideo;

    private int msgStatus; // 消息状态：发送失败:-2，正在发送:-1，已发送：0，已接收：1，未读:2等

    private int msgUnread;

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getCurrJid() {
        return currJid;
    }

    public void setCurrJid(String currJid) {
        this.currJid = currJid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }

    public String getMsgImg() {
        return msgImg;
    }

    public void setMsgImg(String msgImg) {
        this.msgImg = msgImg;
    }

    public String getMsgLoc() {
        return msgLoc;
    }

    public void setMsgLoc(String msgLoc) {
        this.msgLoc = msgLoc;
    }

    public String getMsgVoice() {
        return msgVoice;
    }

    public void setMsgVoice(String msgVoice) {
        this.msgVoice = msgVoice;
    }

    public String getMsgVideo() {
        return msgVideo;
    }

    public void setMsgVideo(String msgVideo) {
        this.msgVideo = msgVideo;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public int getMsgUnread() {
        return msgUnread;
    }

    public void setMsgUnread(int msgUnread) {
        this.msgUnread = msgUnread;
    }

    @Override
    public String toString() {
        return "LastMessage{" +
                "jid='" + jid + '\'' +
                ", currJid='" + currJid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", msgTime=" + msgTime +
                ", chatType=" + chatType +
                ", msgType='" + msgType + '\'' +
                ", msgTxt='" + msgTxt + '\'' +
                ", msgImg='" + msgImg + '\'' +
                ", msgLoc='" + msgLoc + '\'' +
                ", msgVoice='" + msgVoice + '\'' +
                ", msgVideo='" + msgVideo + '\'' +
                ", msgStatus=" + msgStatus +
                ", msgUnread=" + msgUnread +
                '}';
    }

    /**
     * 根据服务器消息，构建本地显示消息
     *
     * @param message
     * @return
     */
    public static LastMessage createLastMsg(Message message) {

        if (message != null) {
            // 当前用户
            String currUserJid = IMHelper.getCurrUser().getJid();
//            String currUserName = IMHelper.getCurrUser().getUserName();
//            String currName = IMHelper.getCurrUser().getName();

            // 发送方
            String from = message.getFrom().toString();
            String fromJid = from.substring(0, from.indexOf("/"));
            String fromName = fromJid.substring(0, fromJid.indexOf("@"));

            // 接收方
            String toJid = message.getTo().toString();
            String toName = toJid.substring(0, toJid.indexOf("@"));

            LastMessage lastMsg = new LastMessage();

            if (currUserJid.equals(toJid)) {
                // 当前用户收到,取发送方信息设置
                lastMsg.setJid(fromJid);
                lastMsg.setCurrJid(currUserJid);

                lastMsg.setName(fromName);

            } else if (currUserJid.equals(fromJid)) {
                // 当前用户发送,取接收方信息设置
                lastMsg.setJid(toJid);
                lastMsg.setCurrJid(currUserJid);

                lastMsg.setName(toName);
            }

            // 消息内容
            lastMsg.setMsgType(IMConstant.ContentType.CONTENT_TEXT);
            lastMsg.setMsgTxt(message.getBody());

            // 聊天类型
            int chatType = IMConstant.Chat.CHAT_TYPE_SINGLE;
            if (!message.getType().name().equals("chat")) {
                chatType = IMConstant.Chat.CHAT_TYPE_ROOM;
            }
            lastMsg.setChatType(chatType);

            lastMsg.setMsgTime(new Date().getTime());
            lastMsg.setMsgUnread(9);

            return lastMsg;
        } else {
            return null;
        }
    }
}
