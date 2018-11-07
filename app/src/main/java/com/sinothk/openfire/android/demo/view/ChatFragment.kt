package com.sinothk.openfire.android.demo.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.xmpp.Watch.Watcher
import com.sinothk.openfire.android.demo.xmpp.XMChatMessageListener
import kotlinx.android.synthetic.main.chat_list_fragment.*
import org.jivesoftware.smack.packet.Message

class ChatFragment : Fragment(), Watcher {

    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.chat_list_fragment, container, false)
        }

        addWatcher()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
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

    override fun update(message: Message?) {
        Log.e("SessionFragmentMessage", message!!.body.toString())
    }
}