package com.sinothk.openfire.android.demo.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jiangyy.easydialog.CommonDialog
import com.jiangyy.easydialog.LoadingDialog
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.StringUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.IMCache
import com.sinothk.openfire.android.SmackHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMStatus
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.util.ActivityUtil
import com.sinothk.openfire.android.demo.view.comm.LoginActivity
import com.sinothk.openfire.android.demo.view.comm.ChangePwdActivity
import com.sinothk.openfire.android.demo.view.mine.activity.UserInfoActivity
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : Fragment(), View.OnClickListener {

    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showUserInfo()
        setListener()
    }

    private fun setListener() {
        userQrCodeIv.setOnClickListener(this)
        userInfoLayout.setOnClickListener(this)
        changePwdItem.setOnClickListener(this)

        setOnlineItem.setOnClickListener(this)
        setActiveItem.setOnClickListener(this)
        setLeaveItem.setOnClickListener(this)
        setOffLineItem.setOnClickListener(this)

        logout.setOnClickListener(this)
        delCurrAccount.setOnClickListener(this)


    }

    /**
     * 显示用户信息
     */
    private fun showUserInfo() {
        val userInfo: IMUser = SmackHelper.getCurrUser()

        val userName: String = StringUtil.getNotNullValue(userInfo.userName, "未知")
        userNameIv.text = userName
        nameIv.text = StringUtil.getNotNullValue(userInfo.name, userName)

        if (userInfo.userAvatar != null) {
            userAvatarIv.setImageDrawable(userInfo.userAvatar)
        }
    }

    override fun onClick(v: View?) {
//        when (v) {
//            userInfoLayout -> {
//                IntentUtil.openActivity(activity, UserInfoActivity::class.java).start()
//            }
//
//            userQrCodeIv -> {
//                ToastUtil.show("开发中")
//            }
//
//            changePwdItem -> {// 修改密码
//                IntentUtil.openActivity(activity, ChangePwdActivity::class.java).start()
//            }
//
//            setOnlineItem -> {// 设置在线
//                SmackHelper.setUserStatus(activity, IMStatus.USER_STATUS_ONLINE, object : IMCallback {
//                    override fun onStart() {
//                    }
//
//                    override fun onEnd(result: IMResult) {
//                        if (result.code == IMCode.SUCCESS) {
//                            ToastUtil.show(result.tip)
//                        } else {
//                            ToastUtil.show(result.tip)
//                        }
//                    }
//                })
//            }
//
//            setActiveItem -> {// 设置活跃
//                IMHelper.setUserStatus(activity, IMStatus.USER_STATUS_ACTIVE, object : IMCallback {
//                    override fun onStart() {
//                    }
//
//                    override fun onEnd(result: IMResult) {
//                        if (result.code == IMCode.SUCCESS) {
//                            ToastUtil.show(result.tip)
//                        } else {
//                            ToastUtil.show(result.tip)
//                        }
//                    }
//                })
//            }
//
//            setLeaveItem -> {// 设置离开
//                IMHelper.setUserStatus(activity, IMStatus.USER_STATUS_LEAVE, object : IMCallback {
//                    override fun onStart() {
//                    }
//
//                    override fun onEnd(result: IMResult) {
//                        if (result.code == IMCode.SUCCESS) {
//                            ToastUtil.show(result.tip)
//                        } else {
//                            ToastUtil.show(result.tip)
//                        }
//                    }
//                })
//            }
//
//            setOffLineItem -> {// 设置离线
//                IMHelper.setUserStatus(activity, IMStatus.USER_STATUS_OFFLINE, object : IMCallback {
//                    override fun onStart() {
//                    }
//
//                    override fun onEnd(result: IMResult) {
//                        if (result.code == IMCode.SUCCESS) {
//                            ToastUtil.show(result.tip)
//                        } else {
//                            ToastUtil.show(result.tip)
//                        }
//                    }
//                })
//            }
//
//            logout -> {
//                val loadingDialog = LoadingDialog.Builder(activity)
//                loadingDialog.setTitle("正在退出...")
//
//                IMHelper.logout(activity, object : IMCallback {
//                    override fun onStart() {
//                        loadingDialog.show()
//                    }
//
//                    override fun onEnd(result: IMResult) {
//                        loadingDialog.dismiss()
//
//                        if (result.code == IMCode.SUCCESS) {
//
//                            IMCache.setAutoLogin(false)
//                            IMHelper.stopKeepAliveService(activity)
//
//                            ActivityUtil.finishAllActivity()
//                            IntentUtil.openActivity(activity, LoginActivity::class.java).start()
//                        } else {
//                            ToastUtil.show(result.tip)
//                        }
//                    }
//                })
//            }
//
//            delCurrAccount -> {
//                CommonDialog.Builder(activity)
//                        .setTitle("提示")
//                        .setMessage("确定要删除账号吗?")
//                        .setPositiveButton("注销账号") {
//
//                            IMHelper.deleteAccount(activity, object : IMCallback {
//                                override fun onStart() {
//                                }
//
//                                override fun onEnd(result: IMResult) {
//
//                                    if (result.code == IMCode.SUCCESS) {
//                                        ActivityUtil.finishAllActivity()
//                                        IntentUtil.openActivity(activity, LoginActivity::class.java).start()
//                                    } else {
//                                        ToastUtil.show(result.tip)
//                                    }
//                                }
//                            })
//                        }
//                        .setNegativeButton("取消", null).show()
//            }
//        }
    }
}
