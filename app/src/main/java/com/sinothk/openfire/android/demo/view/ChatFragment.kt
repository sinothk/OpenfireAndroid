package com.sinothk.openfire.android.demo.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sinothk.openfire.android.IMCache
import com.sinothk.openfire.android.SmackConnection
import com.sinothk.openfire.android.bean.IMConstant
import com.sinothk.openfire.android.bean.IMLastMessage
import com.sinothk.openfire.android.bean.IMMessage
import com.sinothk.openfire.android.demo.MainActivity
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.chat.activity.ChatActivity
import com.sinothk.openfire.android.demo.view.chat.adapter.ChatListAdapter
import com.sinothk.openfire.android.patterns.Watch.Watcher
import kotlinx.android.synthetic.main.chat_list_fragment.*
import org.jivesoftware.smack.packet.Message
import java.lang.Exception

class ChatFragment : Fragment(), Watcher {
    private var currUserJid: String? = null
    var adapter: ChatListAdapter? = null
    private var rootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currUserJid = IMCache.getUserJid()//IMHelper.getCurrUser().jid
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.chat_list_fragment, container, false)
        }

        // 增加XMPP消息观察者
        SmackConnection.addWatcher(this)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        adapter = ChatListAdapter(context)
        recyclerView.adapter = adapter

        adapter!!.setOnItemClickListener { _: Int, any: Any ->
            val lastMessage: IMLastMessage = any as IMLastMessage

            val toJid: String = lastMessage.jid
            val toName: String = lastMessage.name
            val toAvatar: String? = lastMessage.avatar

            // 更新单消息未读！
            Thread { IMCache.getInstance().clearUnread(activity, currUserJid, toJid) }.start()

            // 进入聊天
            startActivity(Intent(activity, ChatActivity::class.java)
                    .putExtra("toJid", toJid)
                    .putExtra("toName", toName)
                    .putExtra("toAvatar", toAvatar)
                    .putExtra("roomChatType", false))
        }
    }

    override fun onResume() {
        super.onResume()
        // 加载数据
        loadingData()
    }


    fun loadingData() {
        val lastMsg: ArrayList<IMLastMessage> = IMCache.findMyLastMsg(context, currUserJid)
        adapter!!.setData(lastMsg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 删除XMPP消息观察者
        SmackConnection.removeWatcher(this)
    }

    /**
     * 收到消息
     */
    override fun update(message: Message?) {
//        if (message == null) {
//            return
//        }
//
//        // 解析收到的消息
//        val imMessage: IMMessage = IMMessage.getIMMessageByMessageBody(message.body)
//        if (imMessage.fromJid == null || currUserJid == null) {
//            return
//        }
//
//        // 设置接收状态和未读状态
//        if (imMessage.fromJid == currUserJid) {
//            // 发出
//            imMessage.fromType = IMConstant.FromType.SEND
//            imMessage.msgStatus = IMConstant.MsgStatus.READ
//        } else {
//            // 收到消息
//            imMessage.fromType = IMConstant.FromType.RECEIVE
//            imMessage.msgStatus = IMConstant.MsgStatus.UNREAD
//        }
//
//        // 保存最后一条数据
//        try {
//            val lastMsg: IMLastMessage? = IMLastMessage.createLastMsg(imMessage)
//            IMCache.saveOrUpdateLastMsg(context, lastMsg)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        // 保存消息到数据库
//        try {
//            // 保存
//            IMCache.saveOrUpdateMsg(context, imMessage)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        // 保存后，再更新界面
//        try {
//            // 延迟，为了等消息保存完成后再查询
//            Thread.sleep(1000)
//
//            // 刷新界面
//            activity?.runOnUiThread {
//                // 更新数据
//                loadingData()
//
//                // 更新 MainActivity未读
//                refreshMainActivity()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    /**
     * 更新主页信息
     */
    private fun refreshMainActivity() {
        val mainActivity: MainActivity? = activity as MainActivity
        mainActivity?.refreshMainTab0()
    }
}