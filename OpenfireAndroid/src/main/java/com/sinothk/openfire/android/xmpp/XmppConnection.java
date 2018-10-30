package com.sinothk.openfire.android.xmpp;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.sinothk.openfire.android.IMHelper;
import com.sinothk.openfire.android.bean.IMStatus;
import com.sinothk.openfire.android.bean.IMUser;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ author LiangYT
 * @ create 2018/10/20 21:54
 * @ Describe
 */
public class XmppConnection {

    private volatile static XmppConnection singleton;

    public static XmppConnection getInstance() {
        if (singleton == null) {
            synchronized (XmppConnection.class) {
                if (singleton == null) {
                    singleton = new XmppConnection();
                }
            }
        }
        return singleton;
    }

    // 连接实体
    private static AbstractXMPPConnection connection;
    private static String TAG = IMHelper.class.getSimpleName();

    public static void init(String server_name, String server_ip, int server_port) {
        SERVER_NAME = server_name;
        SERVER_HOST = server_ip;
        SERVER_PORT = server_port;
    }

    //  ==== 连接部分 =================================================================
    private XMConnectionListener connectionListener;

    private static String SERVER_HOST = null;
    private static String SERVER_NAME = null;
    private static int SERVER_PORT = 0;

    /**
     * 创建连接
     */
    public AbstractXMPPConnection getConnection() {
        if (connection == null || !connection.isConnected()) {
            connection = openConnection();
            return connection;
        } else {
            return connection;
        }
    }

    /**
     * 判断是否已连接
     */
    public boolean checkConnection() {
        return null != connection && connection.isConnected();
    }

    public boolean checkConfig() {
        if (TextUtils.isEmpty(SERVER_NAME) || TextUtils.isEmpty(SERVER_HOST) || SERVER_PORT == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 打开连接
     *
     * @return
     */
    private AbstractXMPPConnection openConnection() {

        if (TextUtils.isEmpty(SERVER_NAME) || TextUtils.isEmpty(SERVER_HOST) || SERVER_PORT == 0) {
            return null;
        }

        try {
            SmackConfiguration.DEBUG = true;

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
            config.setDebuggerEnabled(true);
            //设置离线状态
            config.setSendPresence(false);
            //设置开启压缩，可以节省流量
            config.setCompressionEnabled(true);

            // 关闭一些！
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

            //不需要经过同意才可以添加好友
            Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);


            // 将相应机制隐掉
            //SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
            //SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");

            connection = new XMPPTCPConnection(config.build());

            connection.connect();// 连接到服务器
            return connection;

        } catch (XMPPException | SmackException | IOException | InterruptedException e) {
            e.printStackTrace();
            connection = null;
            return null;
        }
    }

    /**
     * 关闭连接
     */
    public void closeConnection() {
        if (connection != null) {
            // 移除连接监听
            connection.removeConnectionListener(connectionListener);

            if (connection.isConnected()) connection.disconnect();

            connection = null;
        }
        Log.i("XmppConnection", "关闭连接");
    }

    //  ==== 用户部分 =================================================================

    /**
     * 判断连接是否通过了身份验证
     * 即是否已登录
     *
     * @return
     */
    public boolean isAuthenticated() {
        return connection != null && connection.isConnected() && connection.isAuthenticated();
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

            getConnection().login(account, password);

            // 更改在线状态
            setPresence(IMStatus.USER_STATUS_ONLINE);

            // 添加连接监听
            connectionListener = new XMConnectionListener(account, password);
            getConnection().addConnectionListener(connectionListener);
            return "";
        } catch (XMPPException | IOException | SmackException | InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 退出登录
     */
    public void logout() {
        setPresence(IMStatus.USER_STATUS_OFFLINE);
        closeConnection();
    }

    /**
     * 注册
     *
     * @param account  注册帐号
     * @param password 注册密码
     * @return 注册成功 /注册失败
     */
    public String register(String account, String password) {
        if (getConnection() == null)
            return "连接已断开";

        try {
            AccountManager accountManager = AccountManager.getInstance(connection);
            if (accountManager.supportsAccountCreation()) {
                accountManager.sensitiveOperationOverInsecureConnection(true);
                accountManager.createAccount(Localpart.from(account), password);
                return "";
            } else {
                return "已关闭帐户创建功能";
            }
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException | XmppStringprepException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 注册
     *
     * @param newUser 实体
     * @return 注册成功 /注册失败
     */
    public String register(IMUser newUser) {
        if (getConnection() == null)
            return "连接已断开";

        try {
            AccountManager accountManager = AccountManager.getInstance(connection);
            if (accountManager.supportsAccountCreation()) {
                accountManager.sensitiveOperationOverInsecureConnection(true);

                Map<String, String> attributes = new HashMap<>();
                attributes.put("name", newUser.getName());

                accountManager.createAccount(Localpart.from(newUser.getUserName()), newUser.getPassword(), attributes);
                return "";
            } else {
                return "已关闭帐户创建功能";
            }
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException | XmppStringprepException e) {
            e.printStackTrace();
            return e.getMessage();
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
     * 修改密码
     *
     * @return true成功
     */
    public String changePassword(String pwd) {
        if (getConnection() == null)
            return "连接已断开";

        try {
            AccountManager accountManager = AccountManager.getInstance(connection);
            if (accountManager.supportsAccountCreation()) {
                accountManager.sensitiveOperationOverInsecureConnection(true);

                accountManager.changePassword(pwd);
                return "";
            } else {
                return "已关闭帐户创建功能";
            }
        } catch (SmackException | InterruptedException | XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 删除当前用户
     *
     * @return true成功
     */
    public String deleteAccount() {
        if (getConnection() == null)
            return "连接已断开";

        try {
            AccountManager.getInstance(connection).deleteAccount();
            return "";
        } catch (XMPPException | SmackException | InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        }
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

    /**
     * 查询用户
     *
     * @param userName userName
     * @return List<HashMap>
     */
    public ArrayList<IMUser> searchUsers(String userName) {
        if (getConnection() == null)
            return null;

        try {
            DomainBareJid jid = JidCreate.domainBareFrom("search." + getConnection().getServiceName());

            UserSearchManager usm = new UserSearchManager(getConnection());
            Form searchForm = usm.getSearchForm(jid);
            if (searchForm == null) {
                return null;
            }

            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", userName);

            ReportedData data = usm.getSearchResults(answerForm, jid);

            List<ReportedData.Row> rowList = data.getRows();

            ArrayList<IMUser> results = new ArrayList<>();
            for (ReportedData.Row row : rowList) {
                IMUser imUser = new IMUser();

                imUser.setJid(row.getValues("jid").toString().replace("[", "").replace("]", ""));

                // userName部分
                String userNameValue = row.getValues("Username").toString().replace("[", "").replace("]", "");
                imUser.setUserName(userNameValue);

                String name = row.getValues("Name").toString().replace("[", "").replace("]", "");
                if (TextUtils.isEmpty(name)) {
                    imUser.setName(userNameValue);
                } else {
                    imUser.setName(name);
                }

                imUser.setEmail(row.getValues("Email").toString().replace("[", "").replace("]", ""));

                results.add(imUser);
                // 若存在，则有返回,UserName一定非空，其他两个若是有设，一定非空
            }
            return results;

        } catch (SmackException | InterruptedException | XmppStringprepException | XMPPException e) {
            e.printStackTrace();
        }
        return null;
    }
    //========================================================

    /**
     * 获取所有组
     *
     * @return 所有组集合
     */
    public List<RosterGroup> getGroups() {
        if (getConnection() == null)
            return null;
        List<RosterGroup> groupList = new ArrayList<>();
        Collection<RosterGroup> rosterGroup = Roster.getInstanceFor(connection).getGroups();
        for (RosterGroup aRosterGroup : rosterGroup) {
            groupList.add(aRosterGroup);
        }
        return groupList;
    }

    /**
     * 获取某个组里面的所有好友
     *
     * @param groupName 组名
     * @return List<RosterEntry>
     */
    public List<RosterEntry> getEntriesByGroup(String groupName) {
        if (getConnection() == null)
            return null;
        List<RosterEntry> EntriesList = new ArrayList<>();
        RosterGroup rosterGroup = Roster.getInstanceFor(connection).getGroup(groupName);
        Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
        for (RosterEntry aRosterEntry : rosterEntry) {
            EntriesList.add(aRosterEntry);
        }
        return EntriesList;
    }

    /**
     * 获取所有好友信息
     *
     * @return List<RosterEntry>
     */
    public List<RosterEntry> getAllEntries() {
        if (!checkConnection())
            return new ArrayList<>();

        Collection<RosterEntry> rosterEntry = Roster.getInstanceFor(connection).getEntries();
        return new ArrayList<>(rosterEntry);
    }

    /**
     * 获取用户VCard信息
     *
     * @param jid jid
     * @return VCard
     */
    public VCard getUserVCard(String jid) {
        if (getConnection() == null)
            return null;
        VCard vcard = new VCard();
        try {
            vcard = VCardManager.getInstanceFor(getConnection()).loadVCard(JidCreate.entityBareFrom(jid));
        } catch (XmppStringprepException | SmackException | InterruptedException | XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        }

        return vcard;
    }

    /**
     * 获取用户头像信息
     *
     * @param user user
     * @return Drawable
     */
    public Drawable getUserImage(String user) {
        if (getConnection() == null)
            return null;

        ByteArrayInputStream bais = null;

        try {
            VCard vcard = new VCard();
            // 加入这句代码，解决No VCard for
            ProviderManager.addIQProvider("vCard", "vcard-temp", new org.jivesoftware.smackx.vcardtemp.provider.VCardProvider());

            if (user == null || user.equals("") || user.trim().length() <= 0) {
                return null;
            }

            try {
                VCardManager.getInstanceFor(getConnection()).loadVCard(JidCreate.entityBareFrom(user + "@" + getConnection().getServiceName()));

            } catch (XmppStringprepException | SmackException | InterruptedException | XMPPException.XMPPErrorException e) {
                e.printStackTrace();
            }

            if (vcard.getAvatar() == null)
                return null;

            bais = new ByteArrayInputStream(vcard.getAvatar());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return FormatTools.getInstance().InputStream2Drawable(bais);
    }

    /**
     * 添加一个分组
     *
     * @param groupName groupName
     * @return boolean
     */
    public boolean addGroup(String groupName) {
        if (getConnection() == null)
            return false;
        try {
            Roster.getInstanceFor(connection).createGroup(groupName);
            Log.v("addGroup", groupName + "創建成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除分组
     *
     * @param groupName groupName
     * @return boolean
     */
    public boolean removeGroup(String groupName) {
        return true;
    }

    /**
     * 添加好友 无分组
     *
     * @param userName userName
     * @param name     name
     * @return boolean
     */
    public String addUser(String userName, String name) {
        if (getConnection() == null)
            return "连接已断开";

        try {
            String jidStr = createJid(userName);
            Roster.getInstanceFor(connection).createEntry(JidCreate.entityBareFrom(jidStr), name, new String[]{"online"});
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 添加好友 有分组
     *
     * @param userName  userName
     * @param name      name
     * @param groupName groupName
     * @return boolean
     */
    public boolean addUser(String userName, String name, String groupName) {
        if (getConnection() == null)
            return false;
        try {
            Presence subscription = new Presence(Presence.Type.subscribed);
            subscription.setTo(JidCreate.entityBareFrom(userName));

            userName += "@" + getConnection().getServiceName();

            getConnection().sendStanza(subscription);

            Roster.getInstanceFor(connection).createEntry(JidCreate.entityBareFrom(userName), name, new String[]{groupName});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除好友
     *
     * @param userName userName
     * @return boolean
     */
    public String removeUser(String userName) {
        if (getConnection() == null)
            return "连接已断开";

        try {
            RosterEntry entry;
            if (userName.contains("@"))
                entry = Roster.getInstanceFor(connection).getEntry(JidCreate.entityBareFrom(userName));
            else
                entry = Roster.getInstanceFor(connection).getEntry(JidCreate.entityBareFrom(createJid(userName)));

            if (entry == null) {
                entry = Roster.getInstanceFor(connection).getEntry(JidCreate.entityBareFrom(userName));
            }

            Roster.getInstanceFor(connection).removeEntry(entry);

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    /**
     * 修改心情
     *
     * @param status
     */
    public void changeStateMessage(String status) {
        if (getConnection() == null)
            return;
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus(status);
        try {
            getConnection().sendStanza(presence);
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改用户头像
     *
     * @param file file
     */
    public boolean changeImage(File file) {
        if (getConnection() == null)
            return false;

        try {
            VCard vcard = new VCard();
            vcard.load(getConnection());

            byte[] bytes;

            bytes = getFileBytes(file);
            String encodedImage = StringUtils.encodeHex(bytes);
            vcard.setAvatar(bytes, encodedImage);
            vcard.setEncodedImage(encodedImage);
            vcard.setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>" + encodedImage + "</BINVAL>", true);

            ByteArrayInputStream bais = new ByteArrayInputStream(vcard.getAvatar());
            FormatTools.getInstance().InputStream2Bitmap(bais);

            vcard.save(getConnection());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 文件转字节
     *
     * @param file file
     * @return byte[]
     * @throws IOException
     */
    private byte[] getFileBytes(File file) throws IOException {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            int bytes = (int) file.length();
            byte[] buffer = new byte[bytes];
            int readBytes = bis.read(buffer);
            if (readBytes != buffer.length) {
                throw new IOException("Entire file not read");
            }
            return buffer;
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }

    /**
     * 初始化会议室列表
     */
    public List<HostedRoom> getHostRooms() {
        if (getConnection() == null)
            return null;
        Collection<HostedRoom> hostrooms;
        List<HostedRoom> roominfos = new ArrayList<>();
        try {
            hostrooms = MultiUserChatManager.getInstanceFor(getConnection()).getHostedRooms(
                    JidCreate.domainBareFrom(getConnection().getServiceName()));
            for (HostedRoom entry : hostrooms) {
                roominfos.add(entry);
                Log.i("room", "名字：" + entry.getName() + " - ID:" + entry.getJid());
            }
            Log.i("room", "服务会议数量:" + roominfos.size());
        } catch (XMPPException | XmppStringprepException | InterruptedException | SmackException e) {
            e.printStackTrace();
            return null;
        }
        return roominfos;
    }

    /**
     * 创建房间
     *
     * @param roomName 房间名称
     */
    public MultiUserChat createRoom(String roomName, String password) {
        if (getConnection() == null)
            return null;

        MultiUserChat muc = null;
        try {
            // 创建一个MultiUserChat
            muc = MultiUserChatManager.getInstanceFor(getConnection()).getMultiUserChat(
                    JidCreate.entityBareFrom(roomName + "@conference." + getConnection().getServiceName()));
            // 创建聊天室
            muc.create(Resourcepart.from(roomName));
            // 获得聊天室的配置表单
            Form form = muc.getConfigurationForm();
            // 根据原始表单创建一个要提交的新表单。
            Form submitForm = form.createAnswerForm();
            // 向要提交的表单添加默认答复
            for (FormField formField : form.getFields()) {
                if (FormField.Type.hidden == formField.getType()
                        && formField.getVariable() != null) {
                    // 设置默认值作为答复
                    submitForm.setDefaultAnswer(formField.getVariable());
                }
            }
            // 设置聊天室的新拥有者
            List<String> owners = new ArrayList<>();
            owners.add(getConnection().getUser().asEntityBareJidString());// 用户JID
            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
            // 设置聊天室是持久聊天室，即将要被保存下来
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            // 房间仅对成员开放
            submitForm.setAnswer("muc#roomconfig_membersonly", false);
            // 允许占有者邀请其他人
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
            if (!password.equals("")) {
                // 进入是否需要密码
                submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
                        true);
                // 设置进入密码
                submitForm.setAnswer("muc#roomconfig_roomsecret", password);
            }
            // 能够发现占有者真实 JID 的角色
            // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
            // 登录房间对话
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
            // 仅允许注册的昵称登录
            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
            // 允许使用者修改昵称
            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
            // 允许用户注册房间
            submitForm.setAnswer("x-muc#roomconfig_registration", false);
            // 发送已完成的表单（有默认值）到服务器来配置聊天室
            muc.sendConfigurationForm(submitForm);
        } catch (XMPPException | XmppStringprepException | SmackException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return muc;
    }

    /**
     * 加入会议室
     *
     * @param user      昵称
     * @param roomsName 会议室名
     */
    public MultiUserChat joinMultiUserChat(String user, String roomsName) {
        if (getConnection() == null)
            return null;
        try {
            // 使用XMPPConnection创建一个MultiUserChat窗口
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(getConnection()).getMultiUserChat(
                    JidCreate.entityBareFrom(roomsName + "@conference." + getConnection().getServiceName()));

            // 用户加入聊天室
            muc.join(Resourcepart.from(user));

            Log.i("MultiUserChat", "会议室【" + roomsName + "】加入成功........");
            return muc;
        } catch (XMPPException | XmppStringprepException | InterruptedException | SmackException e) {
            e.printStackTrace();
            Log.i("MultiUserChat", "会议室【" + roomsName + "】加入失败........");
            return null;
        }
    }

    /**
     * 发送群组聊天消息
     *
     * @param muc     muc
     * @param message 消息文本
     */
    public void sendGroupMessage(MultiUserChat muc, String message) {
        try {
            muc.sendMessage(message);
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询会议室成员名字
     *
     * @param muc
     */
    public List<String> findMulitUser(MultiUserChat muc) {
        if (getConnection() == null)
            return null;
        List<String> listUser = new ArrayList<>();
        List<EntityFullJid> it = muc.getOccupants();
        // 遍历出聊天室人员名称
        for (EntityFullJid entityFullJid : it) {
            // 聊天室成员名字
            String name = entityFullJid.toString();
            listUser.add(name);
        }
        return listUser;
    }

    /**
     * 创建聊天窗口
     *
     * @param JID JID
     * @return Chat
     */
    public Chat getFriendChat(String JID) {
        try {
            return ChatManager.getInstanceFor(XmppConnection.getInstance().getConnection()).chatWith(JidCreate.entityBareFrom(JID));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送单人聊天消息
     *
     * @param chat    chat
     * @param message 消息文本
     */
    public void sendSingleMessage(Chat chat, String message) {
        try {
            chat.send(message);
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发消息
     *
     * @param chat    chat
     * @param muc     muc
     * @param message message
     */
    public void sendMessage(Chat chat, MultiUserChat muc, String message) {
        if (chat != null) {
            sendSingleMessage(chat, message);
        } else if (muc != null) {
            sendGroupMessage(muc, message);
        }
    }

    /**
     * 发送文件
     *
     * @param user
     * @param filePath
     */
    public void sendFile(String user, String filePath) {
        if (getConnection() == null)
            return;
        // 创建文件传输管理器
        FileTransferManager manager = FileTransferManager.getInstanceFor(getConnection());

        // 创建输出的文件传输
        OutgoingFileTransfer transfer = null;
        try {
            transfer = manager.createOutgoingFileTransfer(JidCreate.entityFullFrom(user));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        // 发送文件
        try {
            if (transfer != null)
                transfer.sendFile(new File(filePath), "You won't believe this!");
        } catch (SmackException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取离线消息
     *
     * @return
     */
    public Map<String, List<HashMap<String, String>>> getHisMessage() {
        if (getConnection() == null)
            return null;
        Map<String, List<HashMap<String, String>>> offlineMsgs = null;

        try {
            OfflineMessageManager offlineManager = new OfflineMessageManager(getConnection());
            List<Message> messageList = offlineManager.getMessages();

            int count = offlineManager.getMessageCount();
            if (count <= 0)
                return null;
            offlineMsgs = new HashMap<>();

            for (Message message : messageList) {
                String fromUser = message.getFrom().toString();
                HashMap<String, String> history = new HashMap<>();
                history.put("useraccount", getConnection().getUser().asEntityBareJidString());
                history.put("friendaccount", fromUser);
                history.put("info", message.getBody());
                history.put("type", "left");
                if (offlineMsgs.containsKey(fromUser)) {
                    offlineMsgs.get(fromUser).add(history);
                } else {
                    List<HashMap<String, String>> temp = new ArrayList<HashMap<String, String>>();
                    temp.add(history);
                    offlineMsgs.put(fromUser, temp);
                }
            }
            offlineManager.deleteMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return offlineMsgs;
    }

    /**
     * 判断OpenFire用户的状态 strUrl :
     * url格式 - http://my.openfire.com:9090/plugins/presence
     * /status?jid=user1@SERVER_NAME&type=xml
     * 返回值 : 0 - 用户不存在; 1 - 用户在线; 2 - 用户离线
     * 说明 ：必须要求 OpenFire加载 presence 插件，同时设置任何人都可以访问
     */
    public int IsUserOnLine(String user) {

        String url = "http://" + SERVER_HOST + ":9090/plugins/presence/status?" +
                "jid=" + user + "@" + SERVER_NAME + "&type=xml";

        int shOnLineState = 0; // 不存在
        try {
            URL oUrl = new URL(url);
            URLConnection oConn = oUrl.openConnection();
            if (oConn != null) {
                BufferedReader oIn = new BufferedReader(new InputStreamReader(
                        oConn.getInputStream()));
                String strFlag = oIn.readLine();
                oIn.close();
                System.out.println("strFlag" + strFlag);
                if (strFlag.contains("type=\"unavailable\"")) {
                    shOnLineState = 2;
                }
                if (strFlag.contains("type=\"error\"")) {
                    shOnLineState = 0;
                } else if (strFlag.contains("priority") || strFlag.contains("id=\"")) {
                    shOnLineState = 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shOnLineState;
    }

    private String createJid(String username) {
        return username + "@" + getConnection().getServiceName();
    }

    public RosterEntry getUserInfo(String jid) {
        if (getConnection() == null)
            return null;

        try {
            RosterEntry entry = Roster.getInstanceFor(connection).getEntry(JidCreate.entityBareFrom(jid));
            return entry;
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isConfig() {
        return !TextUtils.isEmpty(SERVER_NAME) && !TextUtils.isEmpty(SERVER_HOST) && SERVER_PORT != 0;
    }
}
