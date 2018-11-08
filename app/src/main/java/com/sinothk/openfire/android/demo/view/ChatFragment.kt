package com.sinothk.openfire.android.demo.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.model.bean.LastMessage
import com.sinothk.openfire.android.demo.view.chat.activity.ChatActivity
import com.sinothk.openfire.android.demo.view.chat.adapter.ChatListAdapter
import com.sinothk.openfire.android.demo.xmpp.Watch.Watcher
import com.sinothk.openfire.android.demo.xmpp.XMChatMessageListener
import com.sinothk.openfire.android.demo.xmpp.cache.IMCache
import kotlinx.android.synthetic.main.chat_list_fragment.*
import org.jivesoftware.smack.packet.Message

class ChatFragment : Fragment(), Watcher {

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

        adapter = ChatListAdapter()
        recyclerView.adapter = adapter

        adapter!!.setOnItemClickListener { _: Int, any: Any ->
            val lastMessage: LastMessage = any as LastMessage

            val JID = lastMessage.jid
            startActivity(Intent(activity, ChatActivity::class.java)
                    .putExtra("SingleUserChatJID", JID)
                    .putExtra("ChatType", false))
        }

        // 加载数据
        loadingData()
    }

    private fun loadingData() {

        val currUserJid: String = IMHelper.getCurrUser().jid

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
        // 保存数据
        val lastMsg: LastMessage? = LastMessage.createLastMsg(message)
        IMCache.saveOrUpdateLastMsg(context, lastMsg)

        // 刷新界面
        activity?.runOnUiThread {
            loadingData()
        }
    }
}