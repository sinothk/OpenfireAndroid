package com.sinothk.openfire.android.demo.xmpp.Watch;

import org.jivesoftware.smack.packet.Message;

/**
 * 观察者实现类
 */
public class CreateWatcher implements Watcher {
    @Override
    public void update(Message message) {
        System.out.println(message.toString());
    }
}
