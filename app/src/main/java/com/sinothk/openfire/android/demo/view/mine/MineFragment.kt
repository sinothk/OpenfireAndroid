package com.sinothk.openfire.android.demo.view.mine


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jiangyy.easydialog.LoadingDialog
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.StringUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMStatus
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.utils.ActivityUtil
import com.sinothk.openfire.android.demo.view.comm.SignInActivity
import com.sinothk.openfire.android.demo.view.comm.ChangePwdActivity
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : Fragment(), View.OnClickListener {

    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfo: IMUser = IMHelper.getCurrUser()
        userNameIv.text = StringUtil.getNotNullValue(userInfo.userName, "未知用户")

        changePwdItem.setOnClickListener(this)

        setOnlineItem.setOnClickListener(this)
        setActiveItem.setOnClickListener(this)
        setLeaveItem.setOnClickListener(this)
        setOffLineItem.setOnClickListener(this)

        logout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            changePwdItem -> {// 修改密码
                IntentUtil.openActivity(activity, ChangePwdActivity::class.java).start()
            }

            setOnlineItem -> {// 设置在线
                IMHelper.setUserStatus(activity, IMStatus.USER_STATUS_ONLINE, object : IMCallback {
                    override fun onStart() {
                    }

                    override fun onEnd(result: IMResult) {
                        if (result.code == IMCode.SUCCESS) {
                            ToastUtil.show(result.tip)
                        } else {
                            ToastUtil.show(result.tip)
                        }
                    }
                })
            }

            setActiveItem -> {// 设置活跃
                IMHelper.setUserStatus(activity, IMStatus.USER_STATUS_ACTIVE, object : IMCallback {
                    override fun onStart() {
                    }

                    override fun onEnd(result: IMResult) {
                        if (result.code == IMCode.SUCCESS) {
                            ToastUtil.show(result.tip)
                        } else {
                            ToastUtil.show(result.tip)
                        }
                    }
                })
            }

            setLeaveItem -> {// 设置离开
                IMHelper.setUserStatus(activity, IMStatus.USER_STATUS_LEAVE, object : IMCallback {
                    override fun onStart() {
                    }

                    override fun onEnd(result: IMResult) {
                        if (result.code == IMCode.SUCCESS) {
                            ToastUtil.show(result.tip)
                        } else {
                            ToastUtil.show(result.tip)
                        }
                    }
                })
            }

            setOffLineItem -> {// 设置离线
                IMHelper.setUserStatus(activity, IMStatus.USER_STATUS_OFFLINE, object : IMCallback {
                    override fun onStart() {
                    }

                    override fun onEnd(result: IMResult) {
                        if (result.code == IMCode.SUCCESS) {
                            ToastUtil.show(result.tip)
                        } else {
                            ToastUtil.show(result.tip)
                        }
                    }
                })
            }

            logout -> {
                val loadingDialog = LoadingDialog.Builder(activity)
                loadingDialog.setTitle("修改中...")

                IMHelper.logout(activity, object : IMCallback {
                    override fun onStart() {
                        loadingDialog.show()
                    }

                    override fun onEnd(result: IMResult) {
                        loadingDialog.dismiss()

                        if (result.code == IMCode.SUCCESS) {
                            ActivityUtil.finishAllActivity()
                            IntentUtil.openActivity(activity, SignInActivity::class.java).start()
                        } else {
                            ToastUtil.show(result.tip)
                        }
                    }
                })
            }
        }
    }
}
