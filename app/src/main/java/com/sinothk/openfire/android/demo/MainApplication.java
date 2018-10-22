package com.sinothk.openfire.android.demo;

import android.app.Application;

import com.sinothk.comm.utils.PreferUtil;
import com.sinothk.comm.utils.ToastUtil;
import com.sinothk.openfire.android.IMHelper;
import com.sinothk.openfire.android.xmpp.XmppConnection;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntries;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jxmpp.jid.Jid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ author LiangYT
 * @ create 2018/10/20 13:22
 * @ Describe
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        IMHelper.init("127.0.0.1", "192.168.2.135", 5222);
        IMHelper.init("127.0.0.1", "192.168.124.19", 5222);

        PreferUtil.init(this);
        ToastUtil.init(this);
    }


}
