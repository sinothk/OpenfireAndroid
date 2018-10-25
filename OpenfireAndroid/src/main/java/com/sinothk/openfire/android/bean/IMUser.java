package com.sinothk.openfire.android.bean;

import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.packet.RosterPacket;
import org.jxmpp.jid.BareJid;

import java.util.List;
import java.util.Set;

public class IMUser {
    private String jid;
    private boolean subscriptionPending;
    private RosterPacket.ItemType itemType;
    private boolean approved;
    private List<RosterGroup> groupNames;
    protected String name;
    private String userName;
    private String email;
    private String password;
    // ===============================

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
        return name;
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
}
