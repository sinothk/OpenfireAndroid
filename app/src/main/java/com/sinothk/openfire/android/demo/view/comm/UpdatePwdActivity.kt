package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.jiangyy.easydialog.LoadingDialog
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.utils.ActivityUtil
import com.sinothk.openfire.android.demo.view.base.TitleBarActivity
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.activity_change_pwd.*

class UpdatePwdActivity : TitleBarActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_change_pwd


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitleBar("修改密码", true, "保存", View.OnClickListener {
            doUpdatePwd()
        })
    }

    private fun doUpdatePwd() {
        val oldPwd = oldPwdEt.text.toString()
        val newPwd = newPwdEt.text.toString()
        val confirmPwd = confirmPwdEt.text.toString()

        if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmPwd)) {
            ToastUtil.show("输入不正确")
            return
        }

        if (newPwd != confirmPwd) {
            ToastUtil.show("新密码与确认密码不一致")
            return
        }

        val loadingDialog = LoadingDialog.Builder(this@UpdatePwdActivity)
        loadingDialog.setTitle("正在加载ing...")

        IMHelper.changePassword(this@UpdatePwdActivity, newPwd, object : IMCallback {
            override fun onStart() {
                loadingDialog.show()
            }

            override fun onEnd(result: IMResult?) {
                loadingDialog.dismiss()

                if (result?.code == IMCode.SUCCESS) {
                    ActivityUtil.finishAllActivity()
                    IntentUtil.openActivity(this@UpdatePwdActivity, SignInActivity::class.java).start()
                } else {
                    ToastUtil.show("修改密码失败")
                }
            }
        })
    }
}