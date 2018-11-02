package com.sinothk.openfire.android.inters;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.Jid;

public class GroupMessageListener implements MessageListener {
    @Override
    public void processMessage(Message message) {
        Jid from = message.getFrom();
        String body = message.getBody();
        System.out.println("jid:"+from+",body:"+body);
    }
}
