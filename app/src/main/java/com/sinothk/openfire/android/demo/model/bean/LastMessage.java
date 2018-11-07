package com.sinothk.openfire.android.demo.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LastMessage {

    //id
    @Id(autoincrement = true)
    private Long key;

    @Unique
    private String jid;

    private int chatType;

    private String from;
    private String fromName;
    private String fromAvatar;

    private String fromType; // 是收到还是发出

    private String to;
    private String toName;
    private String toAvatar;

    private String contentType; // 内容类型
    private String msgTxt;
    private String msgImg;
    private String msgLoc;

    // 扩展
    private long receiveTime;
    private long sendTime;

    @Generated(hash = 1828687935)
    public LastMessage(Long key, String jid, int chatType, String from,
            String fromName, String fromAvatar, String fromType, String to,
            String toName, String toAvatar, String contentType, String msgTxt,
            String msgImg, String msgLoc, long receiveTime, long sendTime) {
        this.key = key;
        this.jid = jid;
        this.chatType = chatType;
        this.from = from;
        this.fromName = fromName;
        this.fromAvatar = fromAvatar;
        this.fromType = fromType;
        this.to = to;
        this.toName = toName;
        this.toAvatar = toAvatar;
        this.contentType = contentType;
        this.msgTxt = msgTxt;
        this.msgImg = msgImg;
        this.msgLoc = msgLoc;
        this.receiveTime = receiveTime;
        this.sendTime = sendTime;
    }
    @Generated(hash = 1719502128)
    public LastMessage() {
    }
    public Long getKey() {
        return this.key;
    }
    public void setKey(Long key) {
        this.key = key;
    }
    public String getJid() {
        return this.jid;
    }
    public void setJid(String jid) {
        this.jid = jid;
    }
    public int getChatType() {
        return this.chatType;
    }
    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
    public String getFrom() {
        return this.from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getFromName() {
        return this.fromName;
    }
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
    public String getFromAvatar() {
        return this.fromAvatar;
    }
    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }
    public String getFromType() {
        return this.fromType;
    }
    public void setFromType(String fromType) {
        this.fromType = fromType;
    }
    public String getTo() {
        return this.to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getToName() {
        return this.toName;
    }
    public void setToName(String toName) {
        this.toName = toName;
    }
    public String getToAvatar() {
        return this.toAvatar;
    }
    public void setToAvatar(String toAvatar) {
        this.toAvatar = toAvatar;
    }
    public String getContentType() {
        return this.contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getMsgTxt() {
        return this.msgTxt;
    }
    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }
    public String getMsgImg() {
        return this.msgImg;
    }
    public void setMsgImg(String msgImg) {
        this.msgImg = msgImg;
    }
    public String getMsgLoc() {
        return this.msgLoc;
    }
    public void setMsgLoc(String msgLoc) {
        this.msgLoc = msgLoc;
    }
    public long getReceiveTime() {
        return this.receiveTime;
    }
    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }
    public long getSendTime() {
        return this.sendTime;
    }
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
