package com.sinothk.openfire.android.demo.view.contacts.chatRoom

import android.os.Bundle
import android.view.View
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.base.TitleBarActivity

class RoomCreateActivity : TitleBarActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_room_create

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitleBar("创建房间", true, "提交", View.OnClickListener {

            IMHelper.createChartRoom()
        })


    }
}
