package com.sinothk.openfire.android.demo.xmpp.Watch;

/**
 * 抽象主题角色
 * Created by Administrator on 2017/11/3.
 */

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者实现类
 */
public class CreateWatched implements Watched {

    //保存添加的观察者对象
    private List<Watcher> list = new ArrayList<>();

    @Override
    public void addWatcher(Watcher watcher) {
        list.add(watcher);
    }

    @Override
    public void removeWatcher(Watcher watcher) {
        list.remove(watcher);
    }

    @Override
    public void notifyWatchers(Message message) {
        //此处就是，当被观察者发生变化时，通知观察者进行响应
        for (Watcher watcher : list) {
            watcher.update(message);
        }
    }
}
