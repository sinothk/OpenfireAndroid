package com.sinothk.openfire.android.demo.view.contacts.chatRoom

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.StringUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMConstant
import com.sinothk.openfire.android.bean.IMMessage
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.base.activity.TitleBarActivity
import com.sinothk.openfire.android.demo.view.chat.adapter.ChatAdapter
import kotlinx.android.synthetic.main.activity_chat.*
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.muc.MultiUserChat

class RoomChatActivity : TitleBarActivity() {
    private var roomMemberNum = 0
    // 进入部分
    private var chatTarget: String? = null
    private var chatTitle: String? = null
    private var chatType = 1

    // 展示
    private val chatList: ArrayList<IMMessage> = ArrayList()
    private var chatAdapter: ChatAdapter? = null

    override fun getLayoutResId(): Int = R.layout.activity_chat_room

    companion object {
        private var multiUserChat: MultiUserChat? = null
        private var roomName: String? = null

        fun startRoomChat(activity: Activity, muc: MultiUserChat, roomName: String) {

            this.multiUserChat = muc
            this.roomName = roomName

            IntentUtil.openActivity(activity, RoomChatActivity::class.java)
                    .putStringExtra(IMConstant.Chat.CHAT_TITLE, roomName)
                    .putIntExtra(IMConstant.Chat.CHAT_TYPE, IMConstant.Chat.CHAT_TYPE_ROOM)
                    .start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatTarget = intent.getStringExtra(IMConstant.Chat.CHAT_TARGET)
        chatTitle = intent.getStringExtra(IMConstant.Chat.CHAT_TITLE)
        chatType = intent.getIntExtra(IMConstant.Chat.CHAT_TYPE, IMConstant.Chat.CHAT_TYPE_SINGLE)

        roomMemberNum = IMHelper.getRoomMemberSize(multiUserChat)

        // 设置标题
        setTitleBar(StringUtil.getNotNullValue(chatTitle) + "($roomMemberNum)", true, "更多", View.OnClickListener {
            IntentUtil.openActivity(this@RoomChatActivity, RoomListActivity::class.java).start()
        })

        initView()
        initListener()
        initData()
    }

    private fun initData() {
        IMHelper.getChatList(chatList)

        chatAdapter = ChatAdapter(this)

        contentListView.adapter = chatAdapter
        chatAdapter!!.setData(contentListView, chatList)
    }

    private fun initView() {
        // 发送按钮
        sendBtn.setOnClickListener {
            val msgTxt: String = inputMsg.text.toString()
            if (TextUtils.isEmpty(msgTxt)) return@setOnClickListener

            // 发送
            val msgBody: IMMessage = IMMessage.createRoomSendMsg(msgTxt)
            IMHelper.sendRoom(multiUserChat, msgBody)

            // 发送后更新界面
            chatList.add(msgBody)
            chatAdapter!!.setData(contentListView, chatList)

            inputMsg.setText("")
        }

        //
    }

//    /**
//     * 离开聊天室
//     */
//    override fun onDestroy() {
//        super.onDestroy()
////        IMHelper.roomLeave(multiUserChat)
//    }

    private fun initListener() {
        //添加群消息监听器
        multiUserChat?.addMessageListener { message ->

            if (message != null) {
                if (isISend(message)) {
                    return@addMessageListener
                }

                val receiveMsg = IMMessage()

                receiveMsg.chatType = IMConstant.Chat.CHAT_TYPE_GROUP //message.type.name

                receiveMsg.jid = message.from.toString()

                receiveMsg.fromType = IMConstant.FromType.RECEIVE

                receiveMsg.contentType = IMConstant.ContentType.CONTENT_TEXT
                receiveMsg.msgTxt = message.body
                receiveMsg.from = ""

                chatList.add(receiveMsg)
                chatAdapter!!.setData(contentListView, chatList)
            }
        }


    }

    /**
     * 判断是不是自己发出的
     */
    private fun isISend(message: Message): Boolean {

        val currUserName: String = IMHelper.getCurrUser().userName
        val currName: String = IMHelper.getCurrUser().name

        if (message.from.toString().contains(currUserName) || message.from.toString().contains(currName)) {
            return true
        }
        return false
    }
}
