package com.sinothk.openfire.android.bean;

public class IMMessage {

    private String jid;
    private int chatType;

    private String from;
    private String fromUserName;
    private String fromUserAvatar;

    private String to;
    private String toUserName;
    private String toUserAvatar;

    private String fromType;

    private String contentType;
    private String msgTxt;

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
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

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
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

    public static IMMessage createSendMsg(String chatTarget, String msgTxt) {
        IMMessage msg = new IMMessage();

        msg.setJid(chatTarget);
        msg.setChatType(IMConstant.Chat.CHAT_TYPE_SINGLE);

        msg.setContentType(IMConstant.ContentType.CONTENT_TEXT);
        msg.setFromType(IMConstant.FromType.SEND);

        msg.setMsgTxt(msgTxt);

        return msg;
    }
}
