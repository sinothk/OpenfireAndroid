package com.sinothk.openfire.android.demo.view.contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.model.bean.UserBean
import com.sinothk.openfire.android.demo.view.contacts.activity.FriendAddActivity
import com.sinothk.openfire.android.demo.view.contacts.activity.FriendInfoActivity
import com.sinothk.openfire.android.demo.view.contacts.adapter.ContactsAdapter
import com.sinothk.openfire.android.inters.IMCallback
import com.sinothk.widget.loadingRecyclerView.LoadingRecyclerView
import kotlinx.android.synthetic.main.contacts_list_fragment.*
import kotlin.collections.ArrayList


/**

 * @ author LiangYT
 * @ create 2018/10/21 2:21
 * @ Describe
 */
class ContactsFragment : Fragment() {
    var adapter: ContactsAdapter? = null
    private val mLetters = arrayOf("#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

    private var rootView: View? = null
    private var contacts = ArrayList<UserBean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.contacts_list_fragment, container, false)
        }
        return rootView
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(recyclerView.getListViewLine(activity, R.drawable.divider_line))
        recyclerView.setPullRefreshEnabled(false)
        recyclerView.setLoadingMoreEnabled(false)

        // 头部
        setHeaderView(recyclerView)

        adapter = ContactsAdapter(R.layout.contacts_list_item)
        recyclerView.adapter = adapter

        adapter!!.setOnItemClickListener { position: Int, any: Any ->
            val user: UserBean = any as UserBean
            IntentUtil.openActivity(activity, FriendInfoActivity::class.java)
                    .putSerializableExtra("user", user)
                    .startInFragment(this@ContactsFragment)
        }

        // ==================================================================================
        sideBar.setIndexItems(mLetters)
        sideBar.setOnTouchingLetterChangedListener { index ->
            for (i in contacts.indices) {

                if ("#" == index) {
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                    return@setOnTouchingLetterChangedListener
                } else {
                    if (contacts[i].index == index) {
                        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(i, 0)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        getFriendsData()
    }


    private fun getFriendsData() {
        IMHelper.getFriends(activity, object : IMCallback {
            override fun onStart() {
            }

            override fun onEnd(result: IMResult) {
                var contacts = ArrayList<UserBean>()

                if (result.code == IMCode.SUCCESS) {

                    val userList: ArrayList<IMUser> = result.data as ArrayList<IMUser>
                    for (imUser in userList) {
                        val user = UserBean()
                        user.jid = imUser.jid.toString()
                        user.name = imUser.name

                        contacts.add(user)
                    }

                    UserBean.sort(contacts)
                    adapter?.setData(contacts)
                } else {

                }
            }
        })
    }

//    private fun getFriendData(): ArrayList<UserBean> {
//
//        val users = ArrayList<UserBean>()
//        users.add(UserBean("汕头", "女", 31))
//        users.add(UserBean("北方", "男", 30))
//        users.add(UserBean("安庆", "男", 23))
//        users.add(UserBean("金继刀", "男", 45))
//        users.add(UserBean("张京", "男", 35))
//        users.add(UserBean("按理", "男", 55))
//        users.add(UserBean("LiangYT", "男", 55))
//        users.add(UserBean("111", "男", 55))
//
//        UserBean.sort(users)
//
//        return users
//    }

    /**
     *  头部信息
     */
    private fun setHeaderView(recyclerView: LoadingRecyclerView?) {
        val headerView: View = LayoutInflater.from(activity).inflate(R.layout.contacts_header, null)
        recyclerView?.addHeaderView(headerView)

        val addFriendLayout: RelativeLayout = headerView.findViewById(R.id.addFriendLayout)
        addFriendLayout.setOnClickListener {
            IntentUtil.openActivity(activity, FriendAddActivity::class.java).start()
        }
    }
}