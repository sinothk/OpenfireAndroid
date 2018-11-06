package com.sinothk.openfire.android.bean;

public class IMChatRoom {

    private String roomJid;
    private String roomId;
    private String roomName;
    private String roomDesc;
    private String roomPwd;

    private boolean isPersistentRoom;

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

    public boolean isPersistentRoom() {
        return isPersistentRoom;
    }

    public void setPersistentRoom(boolean persistentRoom) {
        isPersistentRoom = persistentRoom;
    }

    @Override
    public String toString() {
        return "IMChatRoom{" +
                "roomName='" + roomName + '\'' +
                ", roomDesc='" + roomDesc + '\'' +
                ", roomPwd='" + roomPwd + '\'' +
                '}';
    }
}
