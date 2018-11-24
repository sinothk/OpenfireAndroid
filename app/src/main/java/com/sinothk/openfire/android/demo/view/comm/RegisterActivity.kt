package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.jiangyy.easydialog.LoadingDialog
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.util.ActivityUtil
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*

/**

 * @ author LiangYT
 * @ create 2018/10/20 13:35
 * @ Describe
 */
class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        ActivityUtil.addActivity(this)
    }

    fun registerBtn(view: View) {

        val userName = userNameEt.text.toString()
        var name = nameEt.text.toString()
        val userPwd = userPwdEt.text.toString()

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            ToastUtil.show("输入有误")
            return
        }

        if (TextUtils.isEmpty(name)) {
            name = userName
        }

//        val loadingDialog = LoadingDialog.Builder(this@SignUpActivity)
//        loadingDialog.setTitle("注册中...")
//        IMHelper.signUp(this@SignUpActivity, userName, name, userPwd, object : IMCallback {
//
//            override fun onStart() {
//                loadingDialog.show()
//            }
//
//            override fun onEnd(result: IMResult) {
//                loadingDialog.dismiss()
//
//                if (result.code == IMCode.SUCCESS) {
//                    show(result.tip)
//                    finish()
//                } else {
//                    show(result.tip)
//                    logPrint(result.msg)
//                }
//            }
//        })

        val newUser = IMUser()
        newUser.userName = userName
        newUser.name = name
        newUser.password = userPwd

        val loadingDialog: LoadingDialog.Builder = LoadingDialog.Builder(this@RegisterActivity)
        loadingDialog.setTitle("注册中...")

//        IMHelper.signUp(this@RegisterActivity, newUser, object : IMCallback {
//
//            override fun onStart() {
//                loadingDialog.show()
//            }
//
//            override fun onEnd(result: IMResult) {
//                loadingDialog.dismiss()
//
//                if (result.code == IMCode.SUCCESS) {
//                    show(result.tip)
//                    finish()
//                } else {
//                    show(result.tip)
//                    logPrint(result.msg)
//                }
//            }
//        })
    }

    private fun show(tip: String) {
        Toast.makeText(this@RegisterActivity, tip, Toast.LENGTH_SHORT).show()
    }

    private fun logPrint(msg: String) {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var msgAll = logMsgTv.text.toString()
        msgAll += "\n" + sdf.format(Date()) + "_" + msg

        logMsgTv.text = msgAll
    }
}