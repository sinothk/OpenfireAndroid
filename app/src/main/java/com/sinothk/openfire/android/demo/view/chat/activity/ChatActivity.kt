package com.sinothk.openfire.android.demo.view.chat.activity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.base.TitleBarActivity
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : TitleBarActivity() {

    private var chatTarget: String? = null
    private var chatTitle: String? = null
    private var chatType = 1

    override fun getLayoutResId(): Int = R.layout.activity_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatTarget = intent.getStringExtra(CHAT_TARGET)
        chatTitle = intent.getStringExtra(CHAT_TITLE)
        chatType = intent.getIntExtra(CHAT_TYPE, CHAT_TYPE_SINGLE)

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
    }

    private fun initView() {

        sendBtn.setOnClickListener {

            val msg: String = inputMsg.text.toString()

            if (TextUtils.isEmpty(msg)) {
                return@setOnClickListener
            }

            ToastUtil.show(msg)
        }
    }

    companion object {
        fun startSingle(activity: Activity, chatTarget: String, chatTitle: String) {
            IntentUtil.openActivity(activity, ChatActivity::class.java)
                    .putStringExtra(CHAT_TARGET, chatTarget)
                    .putStringExtra(CHAT_TITLE, chatTitle)
                    .putIntExtra(ChatActivity.CHAT_TYPE, ChatActivity.CHAT_TYPE_SINGLE)
                    .start()
        }

        val CHAT_TARGET: String = "chatTarget"
        val CHAT_TITLE: String = "chatTitle"
        val CHAT_TYPE: String = "CHAT_TYPE"

        val CHAT_TYPE_SINGLE: Int = 1
        val CHAT_TYPE_GROUP: Int = 3
    }
}
