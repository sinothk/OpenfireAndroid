package com.sinothk.openfire.android;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.sinothk.openfire.android.bean.IMStatus;
import com.sinothk.openfire.android.bean.IMUser;
import com.sinothk.openfire.android.patterns.Watch.CreateWatched;
import com.sinothk.openfire.android.patterns.Watch.Watched;
import com.sinothk.openfire.android.patterns.Watch.Watcher;
import com.sinothk.openfire.android.xmpp.SmackConst;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class SmackConnection implements IncomingChatMessageListener, OutgoingChatMessageListener, ChatManagerListener {
    private Context mContext;

    public void init(final Context mContext) {
        this.mContext = mContext;

        connectionListener = new ConnectionListener() {
            @Override
            public void authenticated(XMPPConnection connection, boolean resume) {
                sConnectionState = ConnectionState.CONNECTED;
                Log.i(TAG, "authenticated()");
            }

            @Override
            public void connected(XMPPConnection connection) {
                sConnectionState = ConnectionState.CONNECTED;
                Log.i(TAG, "connected()");
            }

            @Override
            public void connectionClosed() {
                sConnectionState = ConnectionState.DISCONNECTED;
                Log.i(TAG, "connectionClosed()");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                sConnectionState = ConnectionState.DISCONNECTED;
                Log.i(TAG, "connectionClosedOnError()");

                if (connection.isConnected()) {
                    connection.disconnect();
                }
                reConnectTimer = new Timer();
                reConnectTimer.schedule(new ReConnectTimer(mContext), delay);
                Intent intent = new Intent(SmackConst.ACTION_ERROR_DISCONNECTED);
                mContext.sendBroadcast(intent);
            }

            @Override
            public void reconnectingIn(int seconds) {
                sConnectionState = ConnectionState.RECONNECTING;
                Log.i(TAG, "reconnectingIn()");
            }

            @Override
            public void reconnectionSuccessful() {
                sConnectionState = ConnectionState.CONNECTED;
                Log.i(TAG, "reconnectionSuccessful()");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                sConnectionState = ConnectionState.DISCONNECTED;
                Log.i(TAG, "reconnectionFailed()");
            }
        };
    }

    public void disconnect() {
        Log.i(TAG, "disconnect()");
        if (connection != null) {
            connection.disconnect();
//            sendCancleBroadcast();//发送退出登录广播
        }

        connection = null;
//        if(mReceiver != null){
////            mService.unregisterReceiver(mReceiver);
//            try {
//                mService.unregisterReceiver(mReceiver);
//            } catch (IllegalArgumentException e) {
//                if (e.getMessage().contains("Receiver not registered")) {
//                    // Ignore this exception. This is exactly what is desired
//                    e.printStackTrace();
//                } else {
//                    // unexpected, re-throw
//                    throw e;
//                }
//            }
//            mReceiver = null;
//        }
    }

    /**
     * 获得当前用户信息
     *
     * @return
     */
    public IMUser getCurrUserInfo() {

        IMUser imUser = new IMUser();

        try {
            AccountManager accountManager = AccountManager.getInstance(connection);

            String username = accountManager.getAccountAttribute("username");
            String jid = createJid(username);
            String name = accountManager.getAccountAttribute("name");
            String password = accountManager.getAccountAttribute("password");
            String email = accountManager.getAccountAttribute("email");
//            String registered = accountManager.getAccountAttribute("registered");

            imUser.setJid(jid);
            imUser.setUserName(username);
            imUser.setName(name);
            imUser.setPassword(password);
            imUser.setEmail(email);

            // 获取头像
//            imUser.setUserAvatar(getUserImage(jid));

        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            Log.e("Account", "连接服务器失败");
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            Log.e("Account", "该账户已存在");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            Log.e("Account", "服务器连接失败");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return imUser;
    }

    public String createJid(String username) {
        if (!username.contains("@")) {
            username += "@" + getConnection().getXMPPServiceDomain();
        }
        return username;
    }

    public static enum ConnectionState {
        CONNECTED, CONNECTING, RECONNECTING, DISCONNECTED;
    }

    public static ConnectionState sConnectionState = ConnectionState.DISCONNECTED;

    private static final String TAG = "SmackConnection";

    private XMPPTCPConnection connection;
    private ConnectionListener connectionListener;

    private String SERVER_NAME;
    private String SERVER_HOST;
    private int SERVER_PORT;

    private volatile static SmackConnection singleton;

    public static SmackConnection getInstance() {
        if (singleton == null) {
            synchronized (SmackConnection.class) {
                if (singleton == null) {
                    singleton = new SmackConnection();
                }
            }
        }
        return singleton;
    }

    /**
     * 执行连接
     *
     * @return
     */
    public XMPPTCPConnection connect() {
        String[] serverConfig = IMCache.getServerConfig();
        SERVER_NAME = serverConfig[0];
        SERVER_HOST = serverConfig[1];
        SERVER_PORT = Integer.valueOf(serverConfig[2]);

        if (TextUtils.isEmpty(SERVER_NAME) || TextUtils.isEmpty(SERVER_HOST) || SERVER_PORT == 0) {
            return null;
        }

        try {
            SmackConfiguration.DEBUG = true;

            XMPPTCPConnectionConfiguration.Builder config = createBuilder();
            connection = new XMPPTCPConnection(config.build());

            // 添加连接监听,为了重连！
//            connectionListener = new XMConnectionListener();
            if (connectionListener != null) {
                connection.addConnectionListener(connectionListener);
            }

            // 连接到服务器
            connection.connect();
        } catch (XMPPException | SmackException | IOException | InterruptedException e) {
            e.printStackTrace();
            connection = null;
        }

        return connection;
    }

    public boolean isConnection() {
        if (connection != null && connection.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAuthenticated() {
        if (connection != null && connection.isConnected() && connection.isAuthenticated()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 配置信息
     *
     * @return
     * @throws UnknownHostException
     */
    private XMPPTCPConnectionConfiguration.Builder createBuilder() throws UnknownHostException, XmppStringprepException {
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();

        //设置openfire主机IP
        config.setHostAddress(InetAddress.getByName(SERVER_HOST));
//            config.setHost(serverIp);

        //设置openfire服务器名称
        config.setXmppDomain(SERVER_NAME);
//            config.setServiceName(serverName);

        //设置端口号：默认5222
        config.setPort(SERVER_PORT);
        //禁用SSL连接
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).setCompressionEnabled(false);
        //设置Debug
//        config.setDebuggerEnabled(true);
//        config.setDebuggerEnabled(true);
        //设置离线状态
        config.setSendPresence(false);
        //设置开启压缩，可以节省流量
        config.setCompressionEnabled(true);

        // 关闭一些！
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        //不需要经过同意才可以添加好友
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
        return config;
    }

    /**
     * 创建连接
     */
    public AbstractXMPPConnection getConnection() {
        if (connection == null || !connection.isConnected()) {
            connection = connect();
            return connection;
        } else {
            return connection;
        }
    }

    /**
     * 更改用户状态
     */
    public String setPresence(int code) {

        XMPPConnection con = getConnection();

        if (con == null)
            return "连接已断开";

        try {
            Presence presence;

            switch (code) {
                case IMStatus.USER_STATUS_ONLINE:// 0
                    presence = new Presence(Presence.Type.available);
                    con.sendStanza(presence);
                    Log.v("state", "设置在线");
                    break;
                case IMStatus.USER_STATUS_ACTIVE:// 1
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.chat);
                    con.sendStanza(presence);
                    Log.v("state", "设置Q我吧");
                    break;
                case IMStatus.USER_STATUS_BUSY: // 2
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.dnd);
                    con.sendStanza(presence);
                    Log.v("state", "设置忙碌");
                    break;
                case IMStatus.USER_STATUS_LEAVE:// 3
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.away);
                    con.sendStanza(presence);
                    Log.v("state", "设置离开");
                    break;

//                case 4:
//                    Roster roster = connection.getRoster();
//                    Collection<RosterEntry> entries = roster.getEntries();
//                    for (RosterEntry entry : entries) {
//                        presence = new Presence(Presence.Type.unavailable);
//                        presence.setPacketID(Packet.ID_NOT_AVAILABLE);
//                        presence.setFrom(connection.getUser());
//                        presence.setTo(entry.getUser());
//                        connection.sendPacket(presence);
//                        System.out.println(presence.toXML());
//                    }
//                    // 向同一用户的其他客户端发送隐身状态
//                    presence = new Presence(Presence.Type.unavailable);
//                    presence.setPacketID(Packet.ID_NOT_AVAILABLE);
//                    presence.setFrom(connection.getUser());
//                    presence.setTo(StringUtils.parseBareAddress(connection.getUser()));
//                    connection.sendPacket(presence);
//                    Log.v("state", "设置隐身");
//                    break;

                case IMStatus.USER_STATUS_OFFLINE: // 5
                    presence = new Presence(Presence.Type.unavailable);
                    con.sendStanza(presence);
                    Log.v("state", "设置离线");
                    break;

                default:
                    break;
            }

            return "";
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 登录
     *
     * @param account  登录帐号
     * @param password 登录密码
     * @return true登录成功
     */
    public String login(String account, String password) {
        try {
            if (getConnection() == null)
                return "连接已断开";

            // 登录
            connection.login(account, password);

            if (connection.isAuthenticated()) {//登录成功
                PingManager.setDefaultPingInterval(10);//Ping every 10 seconds
                PingManager myPingManager = PingManager.getInstanceFor(connection);

                //Set PingListener here to catch connect status
                myPingManager.registerPingFailedListener(new PingFailedListener() {
                    @Override
                    public void pingFailed() {
                        Log.i(TAG, "pingFailed()");
                        if (connection.isConnected()) {
                            connection.disconnect();
                        }
                        reConnectTimer = new Timer();
                        reConnectTimer.schedule(new ReConnectTimer(mContext), delay);

//                        Intent intent = new Intent(SmackConst.ACTION_ERROR_DISCONNECTED);
//                        mService.sendBroadcast(intent);
                    }
                });

//                setupSendMessageReceiver();

                // 消息收发
                ChatManager.getInstanceFor(connection).addIncomingListener(this);
                ChatManager.getInstanceFor(connection).addOutgoingListener(this);

                //Set ChatListener here to catch roster change and rebuildRoster
//                Roster.getInstanceFor(connection).addRosterListener(this);

//                sendLoginBroadcast(true);
            } else {
                connection.disconnect();
                Log.i(TAG, "Authentication failure");
//                sendLoginBroadcast(false);
            }

            // 更改在线状态
            setPresence(IMStatus.USER_STATUS_ONLINE);
            return "";
        } catch (XMPPException | IOException | SmackException | InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_WIFI;
        }

        return NETWORN_NONE;
    }

    private Timer reConnectTimer;
    private int delay = 10000;

    //pingFailed时启动重连线程
    class ReConnectTimer extends TimerTask {
        Context mContext;

        public ReConnectTimer(Context context) {
            mContext = context;
        }

        @Override
        public void run() {
            // 无网络连接时,直接返回
            if (getNetworkState(mContext) == NETWORN_NONE) {
                Log.i(TAG, "无网络连接，" + delay / 1000 + "s后重新连接");
                reConnectTimer.schedule(new ReConnectTimer(mContext), delay);
                //reConnectTimer.cancel();
                return;
            }
            // 连接服务器
            try {
                connection.connect();
                if (!connection.isAuthenticated()) {
                    connection.login();
                    reConnectTimer.cancel();
                }
                Log.i(TAG, "重连成功");
                Intent intent = new Intent(SmackConst.ACTION_RECONNECT_SUCCESS);
                mContext.sendBroadcast(intent);

            } catch (Exception e) {
                Log.i(TAG, "重连失败，" + delay / 1000 + "s后重新连接");
                e.printStackTrace();
                reConnectTimer.schedule(new ReConnectTimer(mContext), delay);
            }
        }
    }

    // ============================================================================================
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

    private void dealWithNotify(Message message) {
        // 1.把消息体保存于消息表中去
        // 2.生成会话列表并且保存于会话表中去
        // 3.通知“会话页面”和“聊天页面”，消息来了
        watched.notifyWatchers(message);
    }

    /**
     * 单聊:收到
     *
     * @param entityBareJid
     * @param message
     * @param chat
     */
    @Override
    public void newIncomingMessage(EntityBareJid entityBareJid, Message message, Chat chat) {
        Log.i(TAG, message.getBody());
        dealWithNotify(message);// 单聊收到的消息
    }

    /**
     * 单聊:发出
     *
     * @param entityBareJid
     * @param message
     * @param chat
     */
    @Override
    public void newOutgoingMessage(EntityBareJid entityBareJid, Message message, Chat chat) {
        Log.i(TAG, message.getBody());

        dealWithNotify(message);// 单聊发出去的消息
    }

    @Override
    public void chatCreated(org.jivesoftware.smack.chat.Chat chat, boolean b) {
        chat.addMessageListener(new ChatMessageListener() {
            @Override
            public void processMessage(org.jivesoftware.smack.chat.Chat chat, Message message) {
                Log.i(TAG, "processMessage()");

                dealWithNotify(message);// 所有群组收发的消息

                if (message.getType().equals(Message.Type.chat) || message.getType().equals(Message.Type.normal)) {
                    if (message.getBody() != null) {
//                        Intent intent = new Intent(SmackConst.ACTION_NEW_MESSAGE);
//                        intent.setPackage(mContext.getPackageName());
//                        intent.putExtra(SmackConst.BUNDLE_MESSAGE_BODY, message.getBody());
//                        intent.putExtra(SmackConst.BUNDLE_FROM_JID, message.getFrom());
//                        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//                        mContext.sendBroadcast(intent);
                        Log.i(TAG, "processMessage() BroadCast send&" + message.getFrom() + ":" + message.getBody());
                    }
                }
            }
        });
    }
}
