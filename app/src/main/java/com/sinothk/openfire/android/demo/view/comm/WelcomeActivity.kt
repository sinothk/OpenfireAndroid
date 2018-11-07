package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.comm.utils.PreferUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.model.StringValue
import java.util.*

/**

 * @ author LiangYT
 * @ create 2018/10/20 13:37
 * @ Describe
 */
class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        Thread {
            val serverName: String = PreferUtil.get(StringValue.SERVER_NAME, "") as String
            val serverIp: String = PreferUtil.get(StringValue.SERVER_IP, "") as String
            val serverPort: String = PreferUtil.get(StringValue.SERVER_PORT, "") as String

            if (TextUtils.isEmpty(serverName) || TextUtils.isEmpty(serverIp) || TextUtils.isEmpty(serverPort)) {
                IntentUtil.openActivity(this, ConfigServerActivity::class.java).finish(true).start()

            } else {
                val startTime: Long = Date().time

                // 配置连接参数
                IMHelper.init(serverName, serverIp, Integer.parseInt(serverPort))
                // 打开连接
                IMHelper.openConnection()

                val endTime: Long = Date().time
                val takeTime: Long = endTime - startTime

                if (takeTime < 3000) {
                    Thread.sleep(3000 - takeTime)
                }

                IntentUtil.openActivity(this, LoginActivity::class.java).finish(true).start()
            }
        }.start()
    }
}