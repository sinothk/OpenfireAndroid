package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.jiangyy.easydialog.LoadingDialog
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.utils.ActivityUtil
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*

/**

 * @ author LiangYT
 * @ create 2018/10/20 13:35
 * @ Describe
 */
class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        ActivityUtil.addActivity(this)
    }

    fun registerBtn(view: View) {

        val userName = userNameEt.text.toString()
        val userPwd = userPwdEt.text.toString()

        val loadingDialog = LoadingDialog.Builder(this@SignUpActivity)
        loadingDialog.setTitle("注册中...")

        IMHelper.signUp(this@SignUpActivity, userName, userPwd, object : IMCallback {
            override fun onStart() {
                loadingDialog.show()
            }

            override fun onEnd(result: IMResult) {
                loadingDialog.dismiss()

                if (result.code == IMCode.SUCCESS) {
                    show(result.tip)
                    finish()
                } else {
                    show(result.tip)
                    logPrint(result.msg)
                }
            }
        })
    }

    private fun show(tip: String) {
        Toast.makeText(this@SignUpActivity, tip, Toast.LENGTH_SHORT).show()
    }

    private fun logPrint(msg: String) {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var msgAll = logMsgTv.text.toString()
        msgAll += "\n" + sdf.format(Date()) + "_" + msg

        logMsgTv.text = msgAll
    }
}