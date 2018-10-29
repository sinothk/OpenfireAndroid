package com.sinothk.openfire.android.demo;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.sinothk.comm.utils.PreferUtil;
import com.sinothk.comm.utils.ToastUtil;
import com.sinothk.openfire.android.IMHelper;
import com.sinothk.openfire.android.demo.model.StringValue;
import com.sinothk.openfire.android.xmpp.XmppConnection;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.jetbrains.annotations.Nullable;
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

        PreferUtil.init(this);
        ToastUtil.init(this);


    }
}
