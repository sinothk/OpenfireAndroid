package com.sinothk.openfire.android.bean;

public class IMChatRoom {

    private String roomJid;
    private String roomId;
    private String roomName;
    private String roomDesc;
    private String roomPwd;
    /**
     * 是否密码保护
     */
    private boolean passwordProtected;
    private boolean persistent; // 密码保护

    public IMChatRoom() {
    }

    public IMChatRoom(String roomName, String roomDesc) {
        this.roomName = roomName;
        this.roomDesc = roomDesc;
    }

    public IMChatRoom(String roomName, String roomDesc, String roomPwd) {
        this.roomName = roomName;
        this.roomDesc = roomDesc;
        this.roomPwd = roomPwd;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomJid() {
        return roomJid;
    }

    public void setRoomJid(String roomJid) {
        this.roomJid = roomJid;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomPwd() {
        return roomPwd;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public void setRoomPwd(String roomPwd) {
        this.roomPwd = roomPwd;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    @Override
    public String toString() {
        return "IMChatRoom{" +
                "roomJid='" + roomJid + '\'' +
                ", roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", roomDesc='" + roomDesc + '\'' +
                ", roomPwd='" + roomPwd + '\'' +
                ", passwordProtected=" + passwordProtected +
                ", persistent=" + persistent +
                '}';
    }
}
