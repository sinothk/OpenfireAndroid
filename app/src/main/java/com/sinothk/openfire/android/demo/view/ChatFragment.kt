package com.sinothk.openfire.android.demo.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMConstant
import com.sinothk.openfire.android.bean.IMMessage
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.model.bean.LastMessage
import com.sinothk.openfire.android.demo.view.chat.activity.ChatActivity
import com.sinothk.openfire.android.demo.view.chat.adapter.ChatListAdapter
import com.sinothk.openfire.android.demo.xmpp.Watch.Watcher
import com.sinothk.openfire.android.demo.xmpp.XMChatMessageListener
import com.sinothk.openfire.android.demo.xmpp.cache.IMCache
import kotlinx.android.synthetic.main.chat_list_fragment.*
import org.jivesoftware.smack.packet.Message
import java.lang.Exception

class ChatFragment : Fragment(), Watcher {
    private var currUserJid: String? = null
    var adapter: ChatListAdapter? = null
    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.chat_list_fragment, container, false)
        }

        // 添加消息监听器
        addWatcher()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        adapter = ChatListAdapter(context)
        recyclerView.adapter = adapter

        adapter!!.setOnItemClickListener { _: Int, any: Any ->
            val lastMessage: LastMessage = any as LastMessage

            val jid: String = lastMessage.jid
            startActivity(Intent(activity, ChatActivity::class.java)
                    .putExtra("SingleUserChatJID", jid)
                    .putExtra("ChatType", false))
        }

        initData()

        // 加载数据
        loadingData()
    }

    private fun initData() {
        currUserJid = IMHelper.getCurrUser().jid
    }

    private fun loadingData() {
        val lastMsg: ArrayList<LastMessage> = IMCache.findMyLastMsg(context, currUserJid)
        adapter!!.setData(lastMsg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 删除XMPP消息观察者
        XMChatMessageListener.removeWatcher(this)
    }

    private fun addWatcher() {
        // 增加XMPP消息观察者
        XMChatMessageListener.addWatcher(this)
    }

    /**
     * 收到消息
     */
    override fun update(message: Message?) {
        if (message == null) {
            return
        }

        try { // 保存最后一条数据
            val lastMsg: LastMessage? = LastMessage.createLastMsg(message)
            IMCache.saveOrUpdateLastMsg(context, lastMsg)
            // 刷新界面
            activity?.runOnUiThread {
                loadingData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // ==========保存完整消息到数据库=================
        try {
            val imMessage: IMMessage = IMMessage.getIMMessageByMessage(message.body)

            if (imMessage.from == currUserJid) {
                imMessage.fromType = IMConstant.FromType.SEND
            } else {
                imMessage.fromType = IMConstant.FromType.RECEIVE
            }

            IMCache.saveOrUpdateMsg(context, imMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}