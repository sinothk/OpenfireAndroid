package com.sinothk.openfire.android.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.KeyEvent
import com.jiangyy.easydialog.CommonDialog
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.openfire.android.IMCache
import com.sinothk.openfire.android.SmackConnection
import com.sinothk.openfire.android.bean.*
import com.sinothk.openfire.android.demo.view.ChatFragment
import com.sinothk.openfire.android.demo.view.ContactsFragment
import com.sinothk.openfire.android.demo.view.MineFragment
import com.sinothk.openfire.android.demo.view.comm.LoginActivity
import com.sinothk.openfire.android.inters.IMCallback
import com.sinothk.openfire.android.patterns.Watch.Watcher
import com.sinothk.openfire.android.util.ActivityUtil
import com.sinothk.openfire.android.util.IMUtil
import com.sinothk.tab.weiXin.WxTabMenuMainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.title_layout.*
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.muc.MultiUserChat
import java.lang.Exception
import java.util.*


/**

 * @ author LiangYT
 * @ create 2018/10/20 14:50
 * @ Describe
 */
class MainActivity : AppCompatActivity(), Watcher {

    private var currUser: IMUser? = null
    private var currUserJid: String? = null

    private var chatFragment: ChatFragment? = null

    // 模拟Home键
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityUtil.addActivity(this)

//        IMHelper.startKeepAliveService(this)

        initData()
        initView()
    }

    private fun initData() {
        currUser = IMCache.getUserInfo()
        currUserJid = IMCache.getUserJid()
    }

    override fun onResume() {
        super.onResume()
//        checkIM()
        updateUnread()
    }

    private fun initView() {

        chatFragment = ChatFragment()

        val fragments = ArrayList<Fragment>()
        fragments.add(chatFragment!!)
        fragments.add(ContactsFragment())
        fragments.add(MineFragment())

        val mainAdapter = WxTabMenuMainAdapter(supportFragmentManager, alphaIndicator, fragments)
        mViewPager.adapter = mainAdapter
        mViewPager.addOnPageChangeListener(mainAdapter)

        alphaIndicator!!.setViewPager(mViewPager)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        actionBarTitleTv.text = "消息"
                    }
                    1 -> {
                        actionBarTitleTv.text = "通讯录"
                    }
                    2 -> {
                        actionBarTitleTv.text = "我的"
                    }
                }
            }
        })

        actionBarTitleTv.text = "消息"

        // 增加XMPP消息观察者
        SmackConnection.addWatcher(this)
    }

//    private fun checkIM() {
//        if (!IMUtil.checkNetwork(this)) {
//            return
//        }
//
//        if (IMHelper.isAuthenticated()) {
//            isOnConnectOk()
//        } else {
//            IMHelper.autoLogin(this@MainActivity, object : IMCallback {
//                override fun onStart() {
//                }
//
//                override fun onEnd(result: IMResult) {
//                    if (result.code == IMCode.SUCCESS) {
//                        isOnConnectOk()
//                    } else {
//                        CommonDialog.Builder(this@MainActivity)
//                                .setTitle("提示")
//                                .setMessage("登录超时，请重新登录?")
//                                .setPositiveButton("重新登录") {
//
//                                    IMCache.setAutoLogin(false)
//
//                                    ActivityUtil.finishAllActivity()
//                                    IntentUtil.openActivity(this@MainActivity, LoginActivity::class.java).start()
//
//                                }.setNegativeButton("退出应用") {
//                                    IMCache.setAutoLogin(false)
//                                    ActivityUtil.finishAllActivity()
//                                }.show()
//                    }
//                }
//            })
//        }
//    }

//    private var chatManager: ChatManager? = null

    private fun isOnConnectOk() {
        // 聊天监听类：单聊
//        if (chatManager == null) {
//            chatManager = IMHelper.getChatManager()
//        }
//
//
//        chatManager?.addIncomingListener(XMChatMessageListener())
//        chatManager?.addOutgoingListener(XMChatMessageListener())

        // 聊天监听类：群聊
        initGroupChatManager()
    }

    // 群聊的聊天室列表
    private var multiUserChatList: MutableList<MultiUserChat> = ArrayList()

    // 群聊，加入聊天室，并且监听聊天室消息
    private fun initGroupChatManager() {
//        for (hostedRoomStr in Arrays.asList(Constant.roomNameList)) {
//            multiUserChatList.add(XmppConnection.getInstance().joinMultiUserChat("xiaoHuang--$hostedRoomStr", hostedRoomStr))
//        }
//        for (multiUserChat in multiUserChatList) {
//            multiUserChat.addMessageListener(XMChatMessageListener())
//        }
    }

    // 未读数据提示
    private fun updateUnread() {
        refreshMainTab0()
    }

    // 更新Tab0提示数据
    fun refreshMainTab0() {

        if (currUser == null || TextUtils.isEmpty(currUser?.jid)) return

        // 消息未读数量
        val msgUnreadNum: Int = IMCache.getInstance().findAllMsgUnread(this@MainActivity, currUser?.jid)
        runOnUiThread { alphaIndicator!!.getTabView(0).showNumber(msgUnreadNum) }
    }

    // =================================================

    /**
     * 收到消息
     */
    override fun update(message: Message?) {
        if (message == null) {
            return
        }

        // 解析收到的消息
        val imMessage: IMMessage = IMMessage.getIMMessageByMessageBody(message.body)
        if (imMessage.fromJid == null || currUserJid == null) {
            return
        }

        // 设置接收状态和未读状态
        if (imMessage.fromJid == currUserJid) {
            // 发出
            imMessage.fromType = IMConstant.FromType.SEND
            imMessage.msgStatus = IMConstant.MsgStatus.READ
        } else {
            // 收到消息
            imMessage.fromType = IMConstant.FromType.RECEIVE
            imMessage.msgStatus = IMConstant.MsgStatus.UNREAD
        }

        // 保存最后一条数据
        try {
            val lastMsg: IMLastMessage? = IMLastMessage.createLastMsg(imMessage)
            IMCache.saveOrUpdateLastMsg(this, lastMsg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            // 保存消息到数据库
            IMCache.saveOrUpdateMsg(this, imMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 保存后，再更新界面
        try {
            // 延迟，为了等消息保存完成后再查询
            Thread.sleep(1000)

            // 刷新界面
            runOnUiThread {
                // 更新数据
                chatFragment?.loadingData()

                // 更新 MainActivity未读
                refreshMainTab0()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}