package com.sinothk.openfire.android;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

public class SmackChat {

    private volatile static SmackChat singleton;

    public static SmackChat getInstance() {
        if (singleton == null) {
            synchronized (SmackChat.class) {
                if (singleton == null) {
                    singleton = new SmackChat();
                }
            }
        }
        return singleton;
    }

    /**
     * 发送单人聊天消息
     *
     * @param jid    jid
     * @param msgTxt 消息文本
     */
    public void sendSingleTxtMessage(String jid, String msgTxt) {
        try {
            if (!jid.contains("@")) {
                jid = SmackConnection.getInstance().createJid(jid);
            }

            Message newMessage = new Message(JidCreate.from(jid), Message.Type.chat);
            newMessage.setBody(msgTxt);
            SmackConnection.getInstance().getConnection().sendStanza(newMessage);
        } catch (SmackException.NotConnectedException | InterruptedException | XmppStringprepException e) {
            e.printStackTrace();
        }
    }
}
