package com.sinothk.openfire.android.bean;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.sinothk.openfire.android.BuildConfig;
import com.sinothk.openfire.android.IMHelper;

import org.jivesoftware.smack.packet.Message;

import java.util.Date;

@Table(name = "IMMessage")
public class IMMessage {

    @Deprecated
    private String jid;

    @Id
    private String msgId; // id

    private String chatType; // 消息分类：单聊，群聊，通知等

    private String fromJid;
    private String fromName;
    private String fromUserAvatar;

    private String toJid;
    private String toName;
    private String toUserAvatar;

    private String fromType; // 发送分类：发送，接收等
    private long msgTime;

    private String contentType;// 内容类型：文本，图片，文件，位置等。
    private String msgTxt;
    private String msgImg;
    private String msgLoc;
    private String msgVoice;
    private String msgVideo;

    private int msgStatus; // 消息状态：发送失败:-2，正在发送:-1，已发送：0，已接收：1，未读:2等

    public IMMessage() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    private String message;

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getFromJid() {
        return fromJid;
    }

    public void setFromJid(String fromJid) {
        this.fromJid = fromJid;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }

    public String getToJid() {
        return toJid;
    }

    public void setToJid(String toJid) {
        this.toJid = toJid;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToUserAvatar() {
        return toUserAvatar;
    }

    public void setToUserAvatar(String toUserAvatar) {
        this.toUserAvatar = toUserAvatar;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
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

    @Deprecated
    public static IMMessage createSendMsg(String chatTarget, String msgTxt) {
        IMMessage msg = new IMMessage();

        msg.setJid(chatTarget);
        msg.setChatType(IMConstant.ChatType.SINGLE);

        msg.setContentType(IMConstant.ContentType.TEXT);
        msg.setFromType(IMConstant.FromType.SEND);

        msg.setMsgTxt(msgTxt);

        return msg;
    }

    @Deprecated
    public static IMMessage createRoomSendMsg(String msgTxt) {

        IMMessage msg = new IMMessage();

        msg.setChatType(IMConstant.ChatType.ROOM);

        msg.setContentType(IMConstant.ContentType.TEXT);
        msg.setFromType(IMConstant.FromType.SEND);

        msg.setMsgTxt(msgTxt);

        return msg;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                "jid='" + jid + '\'' +
                ", msgId='" + msgId + '\'' +
                ", chatType='" + chatType + '\'' +
                ", fromJid='" + fromJid + '\'' +
                ", fromName='" + fromName + '\'' +
                ", fromUserAvatar='" + fromUserAvatar + '\'' +
                ", toJid='" + toJid + '\'' +
                ", toName='" + toName + '\'' +
                ", toUserAvatar='" + toUserAvatar + '\'' +
                ", fromType='" + fromType + '\'' +
                ", msgTime=" + msgTime +
                ", contentType='" + contentType + '\'' +
                ", msgTxt='" + msgTxt + '\'' +
                ", msgImg='" + msgImg + '\'' +
                ", msgLoc='" + msgLoc + '\'' +
                ", msgVoice='" + msgVoice + '\'' +
                ", msgVideo='" + msgVideo + '\'' +
                ", msgStatus=" + msgStatus +
                ", message='" + message + '\'' +
                '}';
    }

    /*
      ========================== 创建消息 ======================================
     */

    /**
     * 创建单聊文本消息
     *
     * @param toJid
     * @param toName
     * @param toAvatar
     * @param msg
     * @return
     */
    public static IMMessage createSingleTxtMsg(String toJid, String toName, String toAvatar, String msg) {

        IMMessage imMessage = new IMMessage();

//        imMessage.setJid(toJid);

        imMessage.setChatType(IMConstant.ChatType.SINGLE);
        imMessage.setContentType(IMConstant.ContentType.TEXT);
        imMessage.setMsgTxt(msg);
        // id和时间处理
        long msgTime = new Date().getTime();
        imMessage.setMsgTime(msgTime);
        imMessage.setMsgId(String.valueOf(msgTime));

        // 对方
        imMessage.setToJid(toJid);
        imMessage.setToUserAvatar(toAvatar);
        imMessage.setToName(toName);

        // 自己
        IMUser imUser = IMHelper.getCurrUser();
        imMessage.setFromJid(imUser.getJid());
        imMessage.setFromUserAvatar(imUser.getAvatar());
        imMessage.setFromName(imUser.getName());

        return imMessage;
    }

    // ========================================== 解析消息 ====================================================

    /**
     * 根据Message获得IMMessage
     *
     * @param messageBody
     * @return
     */
    public static IMMessage getIMMessageByMessageBody(String messageBody) {
        try {
            if (BuildConfig.DEBUG) {
                Log.i("IMMessage", messageBody);
            }
            return JSON.parseObject(messageBody, IMMessage.class);
        } catch (Exception e) {
            return null;
        }
    }
}
