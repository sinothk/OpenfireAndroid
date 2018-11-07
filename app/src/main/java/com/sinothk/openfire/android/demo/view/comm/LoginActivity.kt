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
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.demo.MainActivity
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.util.ActivityUtil
import com.sinothk.openfire.android.inters.IMCallback
import kotlinx.android.synthetic.main.activity_login.*
import com.jiangyy.easydialog.LoadingDialog
import com.sinothk.openfire.android.demo.model.StringValue


/**

 * @ author LiangYT
 * @ create 2018/10/20 13:34
 * @ Describe
 */
class LoginActivity : AppCompatActivity() {

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

        avatarIv.setOnClickListener {
            IntentUtil.openActivity(this@LoginActivity, ConfigServerActivity::class.java).start()
        }
    }

    fun loginBtn(view: View) {

        val userName: String = userNameEt.text.toString()
        val userPwd: String = userPwdEt.text.toString()

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            Toast.makeText(this, "输入内容不正确", Toast.LENGTH_SHORT).show()
            return
        }

        val loadingDialog = LoadingDialog.Builder(this@LoginActivity)
        loadingDialog.setTitle("正在登录...")

        IMHelper.login(this@LoginActivity, userName, userPwd, object : IMCallback {

            override fun onStart() {
                loadingDialog.show()
            }

            override fun onEnd(result: IMResult) {

                loadingDialog.dismiss()

                if (result.code == IMCode.SUCCESS) {
                    PreferUtil.set("userName", userName)
                    PreferUtil.set("userPwd", userPwd)

                    IntentUtil.openActivity(this@LoginActivity, MainActivity::class.java).finish(true).start()
                } else {
                    show(result.tip)
                }
            }
        })
    }

    private fun show(tip: String) {
        Toast.makeText(this@LoginActivity, tip, Toast.LENGTH_SHORT).show()
    }

    fun registerBtn(view: View) {
        IntentUtil.openActivity(this@LoginActivity, RegisterActivity::class.java).start()
    }


    override fun onResume() {
        super.onResume()

        if (!IMHelper.isConfig()) {
            val serverName = PreferUtil.get(StringValue.SERVER_NAME, "") as String
            val serverIp = PreferUtil.get(StringValue.SERVER_IP, "") as String
            val serverPort = PreferUtil.get(StringValue.SERVER_PORT, "") as String

            if (TextUtils.isEmpty(serverName) || TextUtils.isEmpty(serverIp) || TextUtils.isEmpty(serverPort)) {
                IntentUtil.openActivity(this, ConfigServerActivity::class.java).finish(true).start()
            } else {
                IMHelper.init(serverName, serverIp, Integer.parseInt(serverPort))
            }
        }
    }
}