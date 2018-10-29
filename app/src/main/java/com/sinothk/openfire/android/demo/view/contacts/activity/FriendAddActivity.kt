package com.sinothk.openfire.android.demo.view.contacts.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.model.bean.UserBean
import com.sinothk.openfire.android.demo.view.base.TitleBarActivity
import com.sinothk.openfire.android.demo.view.contacts.adapter.FriendAddAdapter
import com.sinothk.openfire.android.inters.IMCallback
import com.sinothk.widget.loadingRecyclerView.LoadingRecyclerView
import kotlinx.android.synthetic.main.activity_friend_add.*

class FriendAddActivity : TitleBarActivity() {
    var adapter: FriendAddAdapter? = null

    override fun getLayoutResId(): Int = R.layout.activity_friend_add

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitleBar("新的朋友", true)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(recyclerView.getListViewLine(this, R.drawable.divider_line))
        recyclerView.setPullRefreshEnabled(false)
        recyclerView.setLoadingMoreEnabled(false)

        // 头部
        setHeaderView(recyclerView)

        adapter = FriendAddAdapter(R.layout.contacts_list_item)
        recyclerView.adapter = adapter

        adapter!!.setOnItemClickListener { position: Int, any: Any ->
            val user: UserBean = any as UserBean
            IntentUtil.openActivity(this@FriendAddActivity, FriendInfoActivity::class.java)
                    .putSerializableExtra("user", user)
                    .start()
        }
    }

//    private fun getFriendsData() {
//        IMHelper.getFriends(this, object : IMCallback {
//            override fun onStart() {
//            }
//
//            override fun onEnd(result: IMResult) {
//                var contacts = ArrayList<UserBean>()
//
//                if (result.code == IMCode.SUCCESS) {
//
//                    val userList: ArrayList<IMUser> = result.data as ArrayList<IMUser>
//                    for (imUser in userList) {
//                        val user = UserBean()
//                        user.code = imUser.jid.toString()
//                        user.name = imUser.name
//
//                        contacts.add(user)
//                    }
//
//                    UserBean.sort(contacts)
//                    adapter?.setData(contacts)
//                } else {
//
//                }
//            }
//        })
//    }

    /**
     *  头部信息
     */
    private fun setHeaderView(recyclerView: LoadingRecyclerView?) {
        val headerView: View = LayoutInflater.from(this).inflate(R.layout.friend_add_header, null)
        recyclerView?.addHeaderView(headerView)

        val contentEt: EditText = headerView.findViewById(R.id.contentEt)
        val searchBtn: TextView = headerView.findViewById(R.id.searchBtn)
        searchBtn.setOnClickListener {
            val content = contentEt.text.toString()
            doSearch(content)
        }
    }

    private fun doSearch(content: String) {

        IMHelper.searchUser(this, content, object : IMCallback {
            override fun onStart() {
            }

            override fun onEnd(result: IMResult) {
                val contacts = ArrayList<UserBean>()

                if (result.code == IMCode.SUCCESS) {

                    val userList: ArrayList<IMUser> = result.data as ArrayList<IMUser>
                    for (imUser in userList) {
                        val user = UserBean()
                        user.jid = imUser.jid.toString()
                        user.userName = imUser.userName
                        user.name = imUser.name
                        user.email = imUser.email

                        contacts.add(user)
                    }

                    UserBean.sort(contacts)
                    adapter?.setData(contacts)
                } else {

                }
            }
        })
    }
}
