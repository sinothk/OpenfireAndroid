package com.sinothk.openfire.android.demo.xmpp.Watch;

import org.jivesoftware.smack.packet.Message;

/**
 * 观察者接口
 */
public interface Watcher {

    /**
     * 接收被观察者变化的通知
     *
     * @param message message
     */
    void update(Message message);
}
