package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.openfire.android.IMCache
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.demo.MainActivity
import com.sinothk.openfire.android.demo.R
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
            val startTime: Long = Date().time

            // 获取服务器配置
            val serverConfig: Array<String> = IMCache.getServerConfig()

            if (TextUtils.isEmpty(serverConfig[0]) || TextUtils.isEmpty(serverConfig[1]) || TextUtils.isEmpty(serverConfig[2])) {
                IntentUtil.openActivity(this, ConfigServerActivity::class.java).finish(true).start()

            } else {

                // 配置连接参数
                IMHelper.init(serverConfig[0], serverConfig[1], Integer.parseInt(serverConfig[2]))
                if (IMCache.isAutoLogin()) {

                    IMHelper.openConnection()

                    val endTime: Long = Date().time
                    val takeTime: Long = endTime - startTime
                    if (takeTime < 2000) {
                        Thread.sleep(2000 - takeTime)
                    }

                    IntentUtil.openActivity(this, MainActivity::class.java).finish(true).start()
                } else {
                    IntentUtil.openActivity(this, LoginActivity::class.java).finish(true).start()
                }




            }
        }.start()
    }
}