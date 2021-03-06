package com.sinothk.openfire.android.demo.view.contacts.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.sinothk.comm.utils.StringUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.base.activity.TitleBarActivity
import com.sinothk.openfire.android.demo.view.chat.activity.ChatActivity
import com.sinothk.openfire.android.inters.IMCallback
import com.sinothk.widget.scrollActionbar.scrollView.ObservableScrollView
import com.sinothk.widget.scrollActionbar.uitls.StatusBarUtil
import kotlinx.android.synthetic.main.activity_friend_info.*

class FriendInfoActivity : TitleBarActivity() {

    private var jid: String? = null
    private var name: String? = null

    override fun getLayoutResId(): Int = R.layout.activity_friend_info

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.setImmersionStatusBar(this)

        jid = intent.getStringExtra("jid")
        name = intent.getStringExtra("name")

        setTitleBar(name!!)

        // 设置滚动
        scrollView.setOnScrollChanging(topImgBg, object : ObservableScrollView.OnOperateListener {
            override fun onStart() {
                titleBarLayout.setBackgroundColor(Color.argb(0, 4, 134, 93))//AGB由相关工具获得，或者美工提供
            }

            override fun onChanging(alpha: Float) {
                titleBarLayout.setBackgroundColor(Color.argb(alpha.toInt(), 4, 134, 93))
            }

            override fun onEnd() {
                titleBarLayout.setBackgroundColor(Color.argb(255, 4, 134, 93))
            }
        })

        getUserInf(jid)

        initView()
    }

    private fun initView() {
    }

    private fun getUserInf(jid: String?) {

//        IMHelper.getUserInfo(this, jid, object : IMCallback {
//            override fun onStart() {
//            }
//
//            override fun onEnd(result: IMResult) {
//
//                if (result.code == IMCode.SUCCESS) {
//                    val user: IMUser = result.data as IMUser
//                    user.friendship = IMUser.Friendship.FRIEND
//                    showUserInfo(user)
//
//                } else {
//                    val imUser = IMUser()
//                    imUser.friendship = IMUser.Friendship.NONE
//                    imUser.jid = jid
//                    imUser.name = name
//
//                    showUserInfo(imUser)
//                }
//            }
//        })
    }

    private fun showUserInfo(userInfo: IMUser) {
        val userName: String = StringUtil.getNotNullValue(userInfo.userName, userInfo.jid.substring(0, userInfo.jid.indexOf("@")))
        val name: String = StringUtil.getNotNullValue(userInfo.name, userName)

        setTitleBar(name, true, "更多", View.OnClickListener {

        })

        userNameTv.text = userName

        emailTv.text = StringUtil.getNotNullValue(userInfo.email, "无")

        // 处理好友关系操作
        showFriendship(userInfo.friendship == IMUser.Friendship.FRIEND)

        // 开始聊天
        chatBtn.setOnClickListener {
            val toJid: String = userInfo.jid
            val toName: String = userInfo.name
            val toAvatar: String? = userInfo.avatar

            startActivity(Intent(this@FriendInfoActivity, ChatActivity::class.java)
                    .putExtra("toJid", toJid)
                    .putExtra("toName", toName)
                    .putExtra("toAvatar", toAvatar)
                    .putExtra("roomChatType", false))
        }

        // 删除好友
        delFriendBtn.setOnClickListener {
//            IMHelper.deleteFriend(this@FriendInfoActivity, userName, name, object : IMCallback {
//                override fun onStart() {
//
//                }
//
//                override fun onEnd(result: IMResult) {
//                    ToastUtil.show(result.tip)
//
//                    if (result.code == IMCode.SUCCESS) {
//                        showFriendship(false)
//                    } else {
//                        showFriendship(true)
//                    }
//                }
//            })
        }

        // 添加好友
        addFriendBtn.setOnClickListener {
//            IMHelper.addFriend(this@FriendInfoActivity, userName, name, object : IMCallback {
//                override fun onStart() {
//
//                }
//
//                override fun onEnd(result: IMResult) {
//                    ToastUtil.show(result.tip)
//
//                    if (result.code == IMCode.SUCCESS) {
//                        showFriendship(true)
//                    } else {
//                        showFriendship(false)
//                    }
//                }
//            })
        }
    }

    private fun showFriendship(isFriend: Boolean) {
        operationLayout.visibility = View.VISIBLE

        if (isFriend) {
            friendOperationLayout.visibility = View.VISIBLE
            addFriendBtn.visibility = View.GONE
        } else {
            // 非好友
            friendOperationLayout.visibility = View.GONE
            addFriendBtn.visibility = View.VISIBLE
        }
    }
}
