package com.sinothk.openfire.android.demo.view.mine

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import com.sinothk.comm.utils.StringUtil
import com.sinothk.openfire.android.IMHelper
import com.sinothk.openfire.android.bean.IMUser
import com.sinothk.openfire.android.demo.R
import com.sinothk.openfire.android.demo.view.base.TitleBarActivity
import com.sinothk.widget.scrollActionbar.scrollView.ObservableScrollView
import com.sinothk.widget.scrollActionbar.uitls.StatusBarUtil
import kotlinx.android.synthetic.main.activity_user_info.*

class UserInfoActivity : TitleBarActivity() {

    var needUpadte = false

    var userInfo: IMUser? = null

    override fun getLayoutResId(): Int = R.layout.activity_user_info

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.setImmersionStatusBar(this)

        // 设置滚动
        scrollView.setOnScrollChanging(topImgBg, object : ObservableScrollView.OnOperateListener {
            override fun onStart() {
                titleBarLayout.setBackgroundColor(Color.argb(0, 4, 134, 93))//AGB由相关工具获得，或者美工提供
            }

            override fun onChanging(alpha: Float) {
                titleBarLayout.setBackgroundColor(Color.argb(alpha.toInt(), 4, 134, 93))
            }

            override fun onEnd() {
                titleBarLayout.setBackgroundColor(Color.argb(255, 4, 134, 93))
            }
        })

        initUserInfo()
    }

    private fun initUserInfo() {
        userInfo = IMHelper.getCurrUser()

        val userName: String = StringUtil.getNotNullValue(userInfo!!.userName, "未知")
        setTitleBar(StringUtil.getNotNullValue(userInfo!!.name, userName), true)
        userNameTv.text = userName

        emailTv.setText(StringUtil.getNotNullValue(userInfo!!.email, "无"))
    }

    override fun finish() {

        var emailVal: String = if (TextUtils.isEmpty(emailTv.text.toString())) {
            ""
        } else {
            emailTv.text.toString()
        }

        if (emailVal != userInfo!!.email) {
            userInfo!!.email = emailVal

            needUpadte = true
        }

        if (needUpadte) {
//            IMHelper.updateUser(this, userInfo, object : IMCallback {
//                override fun onStart() {
//                }
//
//                override fun onEnd(result: IMResult) {
//                    ToastUtil.show(result.tip)
//                }
//            })
        }

        super.finish()
    }
}
