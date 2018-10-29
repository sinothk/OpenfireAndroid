package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.PreferUtil
import com.sinothk.comm.utils.StringUtil
import com.sinothk.comm.utils.ViewUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.xmpp.XmppConnection
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.demo.MainActivity
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.utils.ActivityUtil
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
import java.util.*
import com.jiangyy.easydialog.LoadingDialog
import com.sinothk.openfire.android.demo.model.StringValue


/**

 * @ author LiangYT
 * @ create 2018/10/20 13:34
 * @ Describe
 */
class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ActivityUtil.addActivity(this)

        val nameStr: String = PreferUtil.get("userName", "") as String
        val pwdStr: String = PreferUtil.get("userPwd", "") as String

        userNameEt.setText(StringUtil.getNotNullValue(nameStr))
        userPwdEt.setText(StringUtil.getNotNullValue(pwdStr))

        ViewUtil.focusMoveToEnd(userNameEt)
        ViewUtil.focusMoveToEnd(userPwdEt)

        avatarIv.setOnClickListener() {
            IntentUtil.openActivity(this@SignInActivity, ConfigServerActivity::class.java).start()
        }
    }

    private fun logPrint(msg: String) {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var msgAll = logMsgTv.text.toString()
        msgAll += "\n" + sdf.format(Date()) + "_" + msg

        logMsgTv.text = msgAll
    }

    fun loginBtn(view: View) {

        val userName = userNameEt.text.toString()
        val userPwd = userPwdEt.text.toString()

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            Toast.makeText(this, "输入内容不正确", Toast.LENGTH_SHORT).show()
            return
        }

        val loadingDialog = LoadingDialog.Builder(this@SignInActivity)
        loadingDialog.setTitle("正在登录...")

        IMHelper.login(this@SignInActivity, userName, userPwd, object : IMCallback {
            override fun onStart() {
                loadingDialog.show()
            }

            override fun onEnd(result: IMResult) {
                loadingDialog.dismiss()

                if (result.code == IMCode.SUCCESS) {
                    PreferUtil.set("userName", userName)
                    PreferUtil.set("userPwd", userPwd)

                    IntentUtil.openActivity(this@SignInActivity, MainActivity::class.java).finish(true).start()
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

    fun registerBtn(view: View) {
        IntentUtil.openActivity(this@SignInActivity, SignUpActivity::class.java).start()
    }


    override fun onResume() {
        super.onResume()

        if (!IMHelper.isConfig()) {
            val serverName = PreferUtil.get(StringValue.SERVER_NAME, "") as String
            val serverIp = PreferUtil.get(StringValue.SERVER_IP, "") as String
            val serverPort = PreferUtil.get(StringValue.SERVER_PORT, "") as String

            if (TextUtils.isEmpty(serverName) || TextUtils.isEmpty(serverIp) || TextUtils.isEmpty(serverPort)) {
                IntentUtil.openActivity(this, ConfigServerActivity::class.java).finish(true).start()
            }else{
                IMHelper.init(serverName, serverIp, Integer.parseInt(serverPort))
            }
        }
    }
}