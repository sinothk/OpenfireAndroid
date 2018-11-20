package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.jiangyy.easydialog.LoadingDialog
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.StringUtil
import com.sinothk.comm.utils.ViewUtil
import com.sinothk.openfire.android.IMCache
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMCode
import com.sinothk.openfire.android.bean.IMResult
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.MainActivity
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.inters.IMCallback
import com.sinothk.openfire.android.util.ActivityUtil
import kotlinx.android.synthetic.main.activity_login.*


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

        val nameStr: String = IMCache.getUserName()
        val pwdStr: String = IMCache.getUserPwd()

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

                    val imUser: IMUser = result.data as IMUser
                    IMCache.setUserInfo(imUser)

                    IMCache.setUserJid(imUser.jid)
                    IMCache.setUserName(userName)
                    IMCache.setUserPwd(userPwd)

                    IMCache.setAutoLogin(true)

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

            val serverConfig: Array<String> = IMCache.getServerConfig()

            if (TextUtils.isEmpty(serverConfig[0]) || TextUtils.isEmpty(serverConfig[1]) || TextUtils.isEmpty(serverConfig[2])) {
                IntentUtil.openActivity(this, ConfigServerActivity::class.java).finish(true).start()
            } else {
                IMHelper.init(serverConfig[0], serverConfig[1], Integer.parseInt(serverConfig[2]))
            }
        }
    }
}