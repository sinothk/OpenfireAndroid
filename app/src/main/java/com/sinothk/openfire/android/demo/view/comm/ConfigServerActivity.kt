package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.text.TextUtils
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.PreferUtil
import com.sinothk.comm.utils.ToastUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.model.StringValue
import com.sinothk.openfire.android.util.ActivityUtil
import com.sinothk.openfire.android.demo.view.base.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_config_server_ip.*
import java.lang.Exception

class ConfigServerActivity : TitleBarActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_config_server_ip


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitleBar("配置服务器", true)

        okBtn.setOnClickListener { doUpdatePwd() }

        try {
            val serverName = PreferUtil.get(StringValue.SERVER_NAME, "") as String
            val serverIp = PreferUtil.get(StringValue.SERVER_IP, "") as String
            val serverPort = PreferUtil.get(StringValue.SERVER_PORT, "") as String


            serverNameEt.setText(serverName)
            serverPortEt.setText(serverPort)

            if (!TextUtils.isEmpty(serverIp)) {
                val strArr = serverIp.split(".")
                ip0Et.setText(strArr[0])
                ip1Et.setText(strArr[1])
                ip2Et.setText(strArr[2])
                ip3Et.setText(strArr[3])
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun doUpdatePwd() {

        val serverName = serverNameEt.text.toString()
        val serverPort = serverPortEt.text.toString()

        val ip0 = ip0Et.text.toString()
        val ip1 = ip1Et.text.toString()
        val ip2 = ip2Et.text.toString()
        val ip3 = ip3Et.text.toString()

        if (TextUtils.isEmpty(serverName) || TextUtils.isEmpty(serverPort)
                || TextUtils.isEmpty(ip0) || TextUtils.isEmpty(ip1)
                || TextUtils.isEmpty(ip2) || TextUtils.isEmpty(ip3)) {

            ToastUtil.show("输入不正确")
            return
        }

        PreferUtil.set(StringValue.SERVER_NAME, serverName)
        PreferUtil.set(StringValue.SERVER_PORT, serverPort)

        val serverIp = "$ip0.$ip1.$ip2.$ip3"
        PreferUtil.set(StringValue.SERVER_IP, serverIp)

        // 立即更新,为了进入登录界面可以马上登录
        IMHelper.init(serverName, serverIp, Integer.parseInt(serverPort))

        ActivityUtil.finishAllActivity()
        IntentUtil.openActivity(this@ConfigServerActivity, LoginActivity::class.java).start()
    }
}