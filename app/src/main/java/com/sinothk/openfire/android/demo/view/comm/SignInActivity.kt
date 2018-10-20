package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
import java.util.*

/**

 * @ author LiangYT
 * @ create 2018/10/20 13:34
 * @ Describe
 */
class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        IMHelper.init("127.0.0.1", "192.168.2.135", 5222)

        IMHelper.exeConnection(this, object : IMCallback {
            override fun onStart() {

            }

            override fun onEnd(result: IMResult) {
                if (result.code == IMCode.SUCCESS) {
                    show(result.tip)
                    logPrint(result.tip)
                } else {
                    show(result.tip)
                    logPrint(result.msg)
                }
            }
        })
    }

    private fun logPrint(msg: String) {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var msgAll = logMsgTv.text.toString()
        msgAll += "\n" + sdf.format(Date()) + "_" + msg

        logMsgTv.text = msgAll
    }

    fun loginBtn(view: View) {

        val userName = userNameEt.getText().toString()
        val userPwd = userPwdEt.getText().toString()

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            Toast.makeText(this, "输入内容不正确", Toast.LENGTH_SHORT).show()
            return
        }

        IMHelper.login(this@SignInActivity, userName, userPwd, object : IMCallback {
            override fun onStart() {

            }

            override fun onEnd(result: IMResult) {
                if (result.code == IMCode.SUCCESS) {
                    show(result.tip)
                    logPrint(result.tip)
                } else {
                    show(result.tip)
                    logPrint(result.msg)
                }
            }
        })
    }

    private fun show(tip: String) {
        Toast.makeText(this@SignInActivity, tip, Toast.LENGTH_SHORT).show()
    }

    fun logoutBtn(view: View) {
        IMHelper.disconnect(this@SignInActivity, object : IMCallback {
            override fun onStart() {

            }

            override fun onEnd(result: IMResult) {
                if (result.code == IMCode.SUCCESS) {
                    show(result.tip)
                    logPrint(result.tip)
                } else {
                    show(result.tip)
                    logPrint(result.msg)
                }
            }
        })
    }

    fun registerBtn(view: View) {

        val userName = userNameEt.text.toString()
        val userPwd = userPwdEt.text.toString()

        IMHelper.signUp(this@SignInActivity, userName, userPwd, object : IMCallback {
            override fun onStart() {

            }

            override fun onEnd(result: IMResult) {
                if (result.code == IMCode.SUCCESS) {
                    show(result.tip)
                    logPrint(result.tip)
                } else {
                    show(result.tip)
                    logPrint(result.msg)
                }
            }
        })
    }

}