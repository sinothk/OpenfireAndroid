package com.sinothk.openfire.android.bean;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sinothk.openfire.android.BuildConfig;
import com.sinothk.openfire.android.IMHelper;

import java.util.Date;

public class IMMessage {

    @Deprecated
    private String jid;

    private String chatType;

    private String from;
    private String fromName;
    private String fromUserAvatar;
    private String to;
    private String toName;
    private String toUserAvatar;

    private String fromType;
    private long msgTime;

    private String contentType;
    private String msgTxt;

    public IMMessage() {
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    public static IMMessage createSendMsg(String chatTarget, String msgTxt) {
        IMMessage msg = new IMMessage();

        msg.setJid(chatTarget);
        msg.setChatType(IMConstant.ChatType.SINGLE);

        msg.setContentType(IMConstant.ContentType.TEXT);
        msg.setFromType(IMConstant.FromType.SEND);

        msg.setMsgTxt(msgTxt);

        return msg;
    }

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
                ", chatType='" + chatType + '\'' +
                ", from='" + from + '\'' +
                ", fromName='" + fromName + '\'' +
                ", fromUserAvatar='" + fromUserAvatar + '\'' +
                ", to='" + to + '\'' +
                ", toName='" + toName + '\'' +
                ", toUserAvatar='" + toUserAvatar + '\'' +
                ", fromType='" + fromType + '\'' +
                ", msgTime=" + msgTime +
                ", contentType='" + contentType + '\'' +
                ", msgTxt='" + msgTxt + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    /*
      ========================== 构建消息 ======================================
     */

    /**
     * 创建单聊文本消息
     *
     * @param singleJid
     * @param msg
     * @return
     */
    public static IMMessage createSingleTxtMsg(String singleJid, String msg) {

        IMMessage imMessage = new IMMessage();

        imMessage.setJid(singleJid);
        imMessage.setChatType(IMConstant.ChatType.SINGLE);
        imMessage.setContentType(IMConstant.ContentType.TEXT);
        imMessage.setMsgTxt(msg);
        imMessage.setMsgTime(new Date().getTime());

        imMessage.setTo(singleJid);
        imMessage.setToUserAvatar("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2594910732,993782407&fm=58");
        imMessage.setToName("迪丽热巴");

        IMUser imUser = IMHelper.getCurrUser();
        imMessage.setFrom(imUser.getJid());
        imMessage.setFromUserAvatar("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3771747993,2777558986&fm=58");
        imMessage.setFromName("薛之谦");

        return imMessage;
    }

    /**
     * 根据Message获得IMMessage
     *
     * @param messageBody
     * @return
     */
    public static IMMessage getIMMessageByMessage(String messageBody) {
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
