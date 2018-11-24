package com.sinothk.openfire.android.demo.view.contacts.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
//import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.base.activity.TitleBarActivity
import com.sinothk.openfire.android.demo.view.contacts.adapter.RoomListAdapter
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.activity_friend_add.*

class GroupListActivity : TitleBarActivity() {
    var adapter: RoomListAdapter? = null

    override fun getLayoutResId(): Int = R.layout.activity_group_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitleBar("我的群组", true, "新增", View.OnClickListener { })

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(recyclerView.getListViewLine(this, R.drawable.divider_line))
        recyclerView.setPullRefreshEnabled(false)
        recyclerView.setLoadingMoreEnabled(false)

        adapter = RoomListAdapter(R.layout.room_list_item)
        recyclerView.adapter = adapter

        adapter!!.setOnItemClickListener { position: Int, any: Any ->
//            val user: UserBean = any as UserBean
//
//            val currUserName: String = IMHelper.getCurrUser().userName
//            if (currUserName == user.userName) {
//                IntentUtil.openActivity(this@GroupListActivity, UserInfoActivity::class.java).start()
//            } else {
//                IntentUtil.openActivity(this@GroupListActivity, FriendInfoActivity::class.java)
//                        .putSerializableExtra("user", user).start()
//            }
        }


        findGroupList()
    }

    private fun findGroupList() {

//        IMHelper.findMyGroups(this, "", object : IMCallback {
//            override fun onStart() {
//            }
//
//            override fun onEnd(result: IMResult) {
//                if (result.code == IMCode.SUCCESS) {
//
////                    val userList: ArrayList<IMUser> = result.data as ArrayList<IMUser>
////                    for (imUser in userList) {
////                        val user = UserBean()
////                        user.jid = imUser.jid.toString()
////                        user.userName = imUser.userName
////                        user.name = imUser.name
////                        user.email = imUser.email
////
////                        contacts.add(user)
////                    }
////
////                    UserBean.sort(contacts)
////                    adapter?.setData(contacts)
//                } else {
//
//                }
//            }
//        })
    }
}
