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
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.utils.ActivityUtil
import com.sinothk.openfire.android.demo.view.comm.SignInActivity
import com.sinothk.openfire.android.demo.view.comm.UpdatePwdActivity
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

        logout.setOnClickListener(this)
        updatePwdItem.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {

            updatePwdItem -> {// 修改密码
                IntentUtil.openActivity(activity, UpdatePwdActivity::class.java).start()
            }

            logout -> {
                val loadingDialog = LoadingDialog.Builder(activity)
                loadingDialog.setTitle("正在加载ing...")

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
