package com.sinothk.openfire.android.demo.view.contacts.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.sinothk.comm.utils.StringUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.model.bean.UserBean
import com.sinothk.openfire.android.demo.view.base.TitleBarActivity
import com.sinothk.openfire.android.inters.IMCallback
import com.sinothk.widget.scrollActionbar.scrollView.ObservableScrollView
import com.sinothk.widget.scrollActionbar.uitls.StatusBarUtil
import kotlinx.android.synthetic.main.activity_friend_info.*

class FriendInfoActivity : TitleBarActivity() {

    private var upUser: UserBean? = null

    override fun getLayoutResId(): Int = R.layout.activity_friend_info

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.setImmersionStatusBar(this)

        upUser = intent.getSerializableExtra("user") as UserBean
        setTitleBar(upUser!!.name, true)

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

        getUserInf(upUser!!.jid)
    }

    private fun getUserInf(jid: String?) {
        IMHelper.getUserInfo(this, jid, object : IMCallback {
            override fun onStart() {
            }

            override fun onEnd(result: IMResult) {

                if (result.code == IMCode.SUCCESS) {
                    val user: IMUser = result.data as IMUser
                    user.friendship = IMUser.Friendship.FRIEND
                    showUserInfo(user)
                } else {
                    upUser!!.imUser.friendship = IMUser.Friendship.NONE
                    showUserInfo(upUser!!.imUser)
                }
            }
        })
    }

    private fun showUserInfo(userInfo: IMUser) {
        val userName: String = StringUtil.getNotNullValue(userInfo.userName, "未知")
        val name: String = StringUtil.getNotNullValue(userInfo.name, userName)

        setTitleBar(name, true)

        userNameTv.text = userName

        emailTv.text = StringUtil.getNotNullValue(userInfo.email, "无")

        // 处理好友关系操作
        showFriendship(userInfo.friendship == IMUser.Friendship.FRIEND)
        // 开始聊天
        chatBtn.setOnClickListener {

        }
        // 删除好友
        delFriendBtn.setOnClickListener {
            IMHelper.deleteFriend(this@FriendInfoActivity, userName, name, object : IMCallback {
                override fun onStart() {

                }

                override fun onEnd(result: IMResult) {
                    ToastUtil.show(result.tip)

                    if (result.code == IMCode.SUCCESS) {
                        showFriendship(false)
                    } else {
                        showFriendship(true)
                    }
                }
            })
        }
        // 添加好友
        addFriendBtn.setOnClickListener {
            IMHelper.addFriend(this@FriendInfoActivity, userName, name, object : IMCallback {
                override fun onStart() {

                }

                override fun onEnd(result: IMResult) {
                    ToastUtil.show(result.tip)

                    if (result.code == IMCode.SUCCESS) {
                        showFriendship(true)
                    } else {
                        showFriendship(false)
                    }
                }
            })
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
