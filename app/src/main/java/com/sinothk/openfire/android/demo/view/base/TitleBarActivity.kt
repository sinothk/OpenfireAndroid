package com.sinothk.openfire.android.demo.view.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.sinothk.openfire.android.demo.utils.ActivityUtil
import kotlinx.android.synthetic.main.title_layout.*


abstract class TitleBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        ActivityUtil.addActivity(this)
    }

    abstract fun getLayoutResId(): Int

    protected fun setTitleBar(titleTxt: String) {
        if (TextUtils.isEmpty(titleTxt)) {
            actionBarTitleTv.text = "无标题"
        } else {
            actionBarTitleTv.text = titleTxt
        }
    }

    protected fun setTitleBar(titleTxt: String, needBack: Boolean) {
        setTitleBar(titleTxt)

        if (needBack) {
            actionBarBackTv.visibility = View.VISIBLE
            actionBarBackTv.setOnClickListener { finish() }
        } else {
            actionBarBackTv.visibility = View.INVISIBLE
        }
    }

    protected fun setTitleBar(titleTxt: String, needBack: Boolean, moreTxt: String, moreListener: View.OnClickListener?) {
        setTitleBar(titleTxt, needBack)

        if (!TextUtils.isEmpty(moreTxt)) {
            actionBarMoreTv.visibility = View.VISIBLE
            actionBarMoreTv.text = moreTxt
        } else {
            actionBarMoreTv.visibility = View.INVISIBLE
            actionBarMoreTv.text = "操作"
        }

        if (moreListener != null) {
            actionBarMoreTv.setOnClickListener(moreListener)
        }
    }
}