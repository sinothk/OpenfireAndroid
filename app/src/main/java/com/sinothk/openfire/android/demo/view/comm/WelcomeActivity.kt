package com.sinothk.openfire.android.demo.view.comm

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.sinothk.comm.utils.IntentUtil
import com.sinothk.openfire.android.demo.R

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

            IntentUtil.openActivity(this, SignInActivity::class.java).finish(true).start()

        }, 3000)
    }

}