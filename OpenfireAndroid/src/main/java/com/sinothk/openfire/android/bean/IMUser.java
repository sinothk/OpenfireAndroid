package com.sinothk.openfire.android.bean;

import android.graphics.drawable.Drawable;

import com.sinothk.openfire.android.util.PinYinUtil;

import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.packet.RosterPacket;
import org.jxmpp.jid.BareJid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class IMUser implements Serializable {
    private String index;

    private String jid;
    protected String name;
    private String userName;
    private String password;
    private Drawable userAvatar;

    private String email;

    private boolean subscriptionPending;
    private RosterPacket.ItemType itemType;
    private boolean approved;
    private List<RosterGroup> groupNames;
    // ===============================

    /**
     * 关系：0.非好友；1.关注；2.粉丝；3.好友；4.拉黑对方；5.被对方拉黑；
     */
    private int friendship;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        if (name == null || "".equals(name)) {
            return userName;
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public boolean isSubscriptionPending() {
        return subscriptionPending;
    }

    public void setSubscriptionPending(boolean subscriptionPending) {
        this.subscriptionPending = subscriptionPending;
    }

    public RosterPacket.ItemType getItemType() {
        return itemType;
    }

    public void setItemType(RosterPacket.ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public List<RosterGroup> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(List<RosterGroup> groupNames) {
        this.groupNames = groupNames;
    }

    public int getFriendship() {
        return friendship;
    }

    public void setFriendship(int friendship) {
        this.friendship = friendship;
    }

    public Drawable getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(Drawable userAvatar) {
        this.userAvatar = userAvatar;
    }

    public static class Friendship {
        public static final int NONE = 0;
        public static final int LIKED = 1;
        public static final int FLOWN = 2;
        public static final int FRIEND = 3;

        //        NONE, LIKED(1), FLOWN(2);
//
//        Friendship() {
//        NONE = 0;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public static void sort(ArrayList<IMUser> users) {

        for (int i = 0; i < users.size(); i++) {
            String nameIndex = PinYinUtil.getChineseFirstSpell(users.get(i).getName());
            users.get(i).setIndex(nameIndex.substring(0, 1));
        }

        Collections.sort(users, new Comparator<IMUser>() {
            @Override
            public int compare(IMUser o1, IMUser o2) {
                return o1.index.compareTo(o2.index);
            }
        });
    }
}
