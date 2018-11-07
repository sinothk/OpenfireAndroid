package com.sinothk.openfire.android.demo.xmpp;

import com.sinothk.openfire.android.demo.xmpp.Watch.CreateWatched;
import com.sinothk.openfire.android.demo.xmpp.Watch.Watched;
import com.sinothk.openfire.android.demo.xmpp.Watch.Watcher;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

/**
 * 信息接收监听器
 * IncomingChatMessageListener 单聊消息监听
 * MessageListener 群聊消息监听
 *
 * @author huang
 */
public class XMChatMessageListener implements IncomingChatMessageListener,OutgoingChatMessageListener, MessageListener {

    private static Watched watched = new CreateWatched();// 被观察者

    /**
     * 添加观察者
     *
     * @param watcher watcher
     */
    public static void addWatcher(Watcher watcher) {
        watched.addWatcher(watcher);
    }

    /**
     * 删除观察者
     *
     * @param watcher watcher
     */
    public static void removeWatcher(Watcher watcher) {
        watched.removeWatcher(watcher);
    }

    @Override
    public void newIncomingMessage(EntityBareJid entityBareJid, Message message, Chat chat) {
        dealWithNotify(message);// 单聊收到的消息
    }

    @Override
    public void newOutgoingMessage(EntityBareJid entityBareJid, Message message, Chat chat) {
        dealWithNotify(message);// 单聊发出去的消息
    }

    @Override
    public void processMessage(Message message) {
        dealWithNotify(message);// 所有群组收发的消息
    }

    private void dealWithNotify(Message message) {
        // 1.把消息体保存于消息表中去

        // 2.生成会话列表并且保存于会话表中去

        // 3.通知“会话页面”和“聊天页面”，消息来了
        watched.notifyWatchers(message);
    }
}
