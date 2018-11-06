package com.sinothk.openfire.android.demo.view.contacts.chatRoom

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMChatRoom
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.base.TitleBarActivity
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.activity_room_create.*
import org.jivesoftware.smackx.muc.MultiUserChat

class RoomCreateActivity : TitleBarActivity() {

    var needPwd = false
    var isPersistentRoom = true

    override fun getLayoutResId(): Int = R.layout.activity_room_create

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitleBar("创建房间", true, "提交", View.OnClickListener {

            val roomId: String = roomJidEt.text.toString()
            val roomName: String = roomNameEt.text.toString()
            val roomDesc: String = roomDescEt.text.toString()

            val room = IMChatRoom()

            if (TextUtils.isEmpty(roomId)) {
                ToastUtil.show("请输入唯一的聊天室ID")
                return@OnClickListener
            }

            if (TextUtils.isEmpty(roomName)) {
                ToastUtil.show("请输入聊天室名称")
                return@OnClickListener
            }

            if (TextUtils.isEmpty(roomDesc)) {
                ToastUtil.show("请输入聊天室描述")
                return@OnClickListener
            }

            room.roomId = roomId
            room.roomName = roomName
            room.roomDesc = roomDesc

            // 密码部分
            if (needPwd) {
                val roomPwd: String = roomPwdEt.text.toString()

                if (TextUtils.isEmpty(roomPwd)) {
                    ToastUtil.show("请输入密码")
                    return@OnClickListener
                }
                room.roomPwd = roomPwd
            }

            // 是否是永久房间
            room.isPersistentRoom = isPersistentRoom

            IMHelper.createChartRoom(this@RoomCreateActivity, room, object : IMCallback {
                override fun onStart() {

                }

                override fun onEnd(result: IMResult) {
                    if (result.code == IMCode.SUCCESS) {
                        ToastUtil.show("创建成功！")

                        val muc: MultiUserChat = result.data as MultiUserChat
                        RoomChatActivity.startRoomChat(this@RoomCreateActivity, muc, roomName)
                    } else {
                        ToastUtil.show(result.tip)
                    }
                }
            })
        })

        needPwdRg.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.needPwdBtn -> {
                    needPwd = true
                }
                R.id.notNeedPwdBtn -> {
                    needPwd = false
                }
            }
        }

        isPersistentRoomRg.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.isPersistentRoom -> {
                    isPersistentRoom = true
                }
                R.id.notPersistentRoomBtn -> {
                    isPersistentRoom = false
                }
            }

        }
    }
}
