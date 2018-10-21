package com.sinothk.openfire.android.demo.view.mine


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sinothk.comm.utils.StringUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.utils.ActivityUtil
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfo: IMUser = IMHelper.getCurrUser()
        userNameIv.text = StringUtil.getNotNullValue(userInfo.userName, "未知用户")

//        val nameStr: String = PreferUtil.get("userName", "") as String
//
//        Thread {
//
//            val entityFullJid: EntityFullJid = XmppConnection.getInstance().connection.user
//
//            val localpart: Localpart = entityFullJid.localpart
//
//
//            if (localpart == null) {
//            } else {
//
//            }
//        }.start()


        logout.setOnClickListener {

            IMHelper.logout(activity, object : IMCallback {
                override fun onStart() {

                }

                override fun onEnd(result: IMResult?) {
                    if (result?.code == IMCode.SUCCESS) {
                        ActivityUtil.finishAllActivity()
                    } else {

                    }
                }
            })
        }
    }
}
