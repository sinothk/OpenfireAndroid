package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.os.Handler
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

        Handler().postDelayed({

            val serverName = PreferUtil.get(StringValue.SERVER_NAME, "") as String
            val serverIp = PreferUtil.get(StringValue.SERVER_IP, "") as String
            val serverPort = PreferUtil.get(StringValue.SERVER_PORT, "") as String

            if (TextUtils.isEmpty(serverName) || TextUtils.isEmpty(serverIp) || TextUtils.isEmpty(serverPort)) {
                IntentUtil.openActivity(this, ConfigServerActivity::class.java).finish(true).start()
            } else {
                // IMHelper.init("127.0.0.1", "192.168.2.135", 5222);
                // IMHelper.init("127.0.0.1", "192.168.124.19", 5222);

                IMHelper.init(serverName, serverIp, Integer.parseInt(serverPort))

                IntentUtil.openActivity(this, SignInActivity::class.java).finish(true).start()
            }
        }, 3000)
    }

}