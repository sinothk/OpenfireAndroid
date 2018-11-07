package com.sinothk.openfire.android.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.util.ActivityUtil
import com.sinothk.openfire.android.demo.view.ChatFragment
import com.sinothk.openfire.android.demo.view.ContactsFragment
import com.sinothk.openfire.android.demo.view.MineFragment
import com.sinothk.openfire.android.demo.xmpp.XMChatMessageListener
import com.sinothk.tab.weiXin.WxTabMenuMainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.title_layout.*
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smackx.muc.MultiUserChat
import java.util.*


/**

 * @ author LiangYT
 * @ create 2018/10/20 14:50
 * @ Describe
 */
class MainActivity : AppCompatActivity() {

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

        initView()
        initIM()
    }

    private fun initView() {
        val fragments = ArrayList<Fragment>()
        fragments.add(ChatFragment())
        fragments.add(ContactsFragment())
        fragments.add(MineFragment())

        val mainAdapter = WxTabMenuMainAdapter(supportFragmentManager, alphaIndicator, fragments)
        mViewPager.adapter = mainAdapter
        mViewPager.addOnPageChangeListener(mainAdapter)

        alphaIndicator!!.setViewPager(mViewPager)

        actionBarTitleTv.text = "消息"
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

        // 未读数据提示
        alphaIndicator!!.getTabView(0).showNumber(144)
        alphaIndicator!!.getTabView(1).showNumber(36)
        alphaIndicator!!.getTabView(2).showPoint()
    }

    private fun initIM() {
        // 聊天监听类：单聊
        initChatManager()
        // 聊天监听类：群聊
        initGroupChatManager()
    }

    // 聊天监听类
    private fun initChatManager() {
        // 单聊
        val cm: ChatManager = IMHelper.getChatManager()
        cm.addIncomingListener(XMChatMessageListener())
        cm.addOutgoingListener(XMChatMessageListener())
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
}