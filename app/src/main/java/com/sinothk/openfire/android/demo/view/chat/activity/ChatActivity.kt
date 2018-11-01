package com.sinothk.openfire.android.demo.view.chat.activity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMConstant
import com.sinothk.openfire.android.bean.IMMessage
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.base.TitleBarActivity
import com.sinothk.openfire.android.demo.view.chat.adapter.ChatAdapter
import kotlinx.android.synthetic.main.activity_chat.*
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.Message

class ChatActivity : TitleBarActivity() {
    // 进入部分
    private var chatTarget: String? = null
    private var chatTitle: String? = null
    private var chatType = 1

    // 展示
    private val chatList: ArrayList<IMMessage> = ArrayList()
    var chatAdapter: ChatAdapter? = null

    override fun getLayoutResId(): Int = R.layout.activity_chat

    companion object {
        /**
         * 开启单聊
         */
        fun startSingle(activity: Activity, chatTarget: String, chatTitle: String) {
            IntentUtil.openActivity(activity, ChatActivity::class.java)
                    .putStringExtra(IMConstant.Chat.CHAT_TARGET, chatTarget)
                    .putStringExtra(IMConstant.Chat.CHAT_TITLE, chatTitle)
                    .putIntExtra(IMConstant.Chat.CHAT_TYPE, IMConstant.Chat.CHAT_TYPE_SINGLE)
                    .start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatTarget = intent.getStringExtra(IMConstant.Chat.CHAT_TARGET)
        chatTitle = intent.getStringExtra(IMConstant.Chat.CHAT_TITLE)
        chatType = intent.getIntExtra(IMConstant.Chat.CHAT_TYPE, IMConstant.Chat.CHAT_TYPE_SINGLE)

        // 设置标题
        setTitleBar(chatTitle!!, true, "更多", View.OnClickListener { })

//        if (chatType == CHAT_TYPE_SINGLE) {
//            // 单聊
//
//        } else {
//            // 群聊
//            setTitleBar("", true, "更多", View.OnClickListener { })
//        }

        initView()
        initData()
    }

    private fun initData() {
        IMHelper.getChatList(chatList)

        chatAdapter = ChatAdapter(this)

        contentListView.adapter = chatAdapter
        chatAdapter!!.setData(contentListView, chatList)
    }

    private fun initView() {
        val chatManager: ChatManager = IMHelper.getChatManager()//取得聊天管理器
        // 接收监听
        chatManager.addIncomingListener { entityBareJid, message, chat ->
            if (message != null) {
                pintLog(message)

                val receiveMsg = IMMessage()

                receiveMsg.jid = entityBareJid.toString()

                receiveMsg.fromType = IMConstant.FromType.RECEIVE

                receiveMsg.contentType = IMConstant.ContentType.CONTENT_TEXT
                receiveMsg.msgTxt = message.body
                receiveMsg.from = ""

                chatList.add(receiveMsg)
                chatAdapter!!.setData(contentListView, chatList)
            }
        }

        // 发送按钮
        sendBtn.setOnClickListener {
            val msgTxt: String = inputMsg.text.toString()
            if (TextUtils.isEmpty(msgTxt)) return@setOnClickListener

            val msgBody: IMMessage = IMMessage.createSendMsg(chatTarget, msgTxt)
            // 发送
            IMHelper.send(msgBody)

            // 更新
            refreshView(msgBody)

            inputMsg.setText("")
        }
    }

    /**
     * 发送后更新界面
     */
    private fun refreshView(msgBody: IMMessage) {
        chatList.add(msgBody)
        chatAdapter!!.setData(contentListView, chatList)
    }

    private fun pintLog(message: Message) {
        System.out.println("来自 --> " + message.from)
        System.out.println("发给 --> " + message.to)
        System.out.println("内容 --> " + message.body)
    }
}
