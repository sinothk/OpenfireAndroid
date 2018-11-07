package com.sinothk.openfire.android.demo.view.contacts.chatRoom

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jiangyy.easydialog.InputDialog
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMChatRoom
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.base.activity.TitleBarActivity
import com.sinothk.openfire.android.demo.view.contacts.adapter.RoomListAdapter
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.activity_friend_add.*
import org.jivesoftware.smackx.muc.MultiUserChat


class RoomListActivity : TitleBarActivity() {
    var adapter: RoomListAdapter? = null

    override fun getLayoutResId(): Int = R.layout.activity_chat_room_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitleBar("聊天室", true, "新增", View.OnClickListener {
            IntentUtil.openActivity(this@RoomListActivity, RoomCreateActivity::class.java).start()
        })

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(recyclerView.getListViewLine(this, R.drawable.divider_line))
        recyclerView.setPullRefreshEnabled(false)
        recyclerView.setLoadingMoreEnabled(false)

        adapter = RoomListAdapter(R.layout.room_list_item)
        recyclerView.adapter = adapter

        adapter!!.setOnItemClickListener { _: Int, any: Any ->

            // 开始进入聊天
            val currUser: IMUser = IMHelper.getCurrUser()
            val chatRoom: IMChatRoom = any as IMChatRoom

            if (!chatRoom.isPasswordProtected) {
                val muc: MultiUserChat = IMHelper.joinMultiUserChat(chatRoom.roomJid, currUser.name)
                RoomChatActivity.startRoomChat(this@RoomListActivity, muc, chatRoom.roomName)
            } else {
                InputDialog.Builder(this)
                        .setTitle("会议室密码")
                        .setHint("请输入密码进入")
                        .setLines(1)
                        .setPositiveButton("进入") { view ->
                            val pwdStr: String = view.tag.toString()
                            if (pwdStr.isEmpty()) {
                                ToastUtil.show("请输入密码")
                                return@setPositiveButton
                            }

                            val muc: MultiUserChat? = IMHelper.joinMultiUserChat(chatRoom.roomJid, currUser.name, pwdStr)
                            if (muc == null) {
                                ToastUtil.show("密码不正确")
                                return@setPositiveButton
                            }
                            RoomChatActivity.startRoomChat(this@RoomListActivity, muc, chatRoom.roomName)

                        }.setNegativeButton("取消", null).show()
            }
        }

        findGroupList()
    }

    private fun findGroupList() {

        IMHelper.getHostAllRooms(this, object : IMCallback {
            override fun onStart() {
            }

            override fun onEnd(result: IMResult) {
                if (result.code == IMCode.SUCCESS) {
                    val roomList: ArrayList<IMChatRoom> = result.data as ArrayList<IMChatRoom>
                    adapter?.setData(roomList)
                } else {

                }
            }
        })
    }
}
