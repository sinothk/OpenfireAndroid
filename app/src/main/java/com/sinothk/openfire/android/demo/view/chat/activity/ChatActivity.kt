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

    var userInfo: IMUser? = null
    var chatType = 1

    override fun getLayoutResId(): Int = R.layout.activity_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatType = intent.getIntExtra(CHAT_TYPE, CHAT_TYPE_SINGLE)
        userInfo = intent.getSerializableExtra(CHAT_INFO) as IMUser?

        if (chatType == CHAT_TYPE_SINGLE) {
            // 单聊
            setTitleBar(userInfo!!.name, true, "更多", View.OnClickListener { })
        } else {
            // 群聊
            setTitleBar("", true, "更多", View.OnClickListener { })
        }

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
        fun startSingle(activity: Activity, userInfo: IMUser) {
            IntentUtil.openActivity(activity, ChatActivity::class.java)
                    .putSerializableExtra(CHAT_INFO, userInfo)
                    .putIntExtra(ChatActivity.CHAT_TYPE, ChatActivity.CHAT_TYPE_SINGLE)
                    .start()
        }

        val CHAT_TYPE: String = "CHAT_TYPE"
        val CHAT_INFO: String = "CHAT_INFO"
        val CHAT_TYPE_SINGLE: Int = 1
        val CHAT_TYPE_GROUP: Int = 3
    }
}
