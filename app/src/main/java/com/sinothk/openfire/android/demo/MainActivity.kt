package com.sinothk.openfire.android.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.sinothk.openfire.android.demo.utils.ActivityUtil
import com.sinothk.openfire.android.demo.view.chat.ChatFragment
import com.sinothk.openfire.android.demo.view.contacts.ContactsFragment
import com.sinothk.openfire.android.demo.view.mine.MineFragment
import com.sinothk.tab.weiXin.WxTabMenuMainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.title_layout.*


/**

 * @ author LiangYT
 * @ create 2018/10/20 14:50
 * @ Describe
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityUtil.addActivity(this)

        val fragments = ArrayList<Fragment>()
        fragments.add(ChatFragment())
        fragments.add(ContactsFragment())
        fragments.add(MineFragment())

        val mainAdapter = WxTabMenuMainAdapter(supportFragmentManager, alphaIndicator, fragments)
        mViewPager.adapter = mainAdapter
        mViewPager.addOnPageChangeListener(mainAdapter)

        alphaIndicator!!.setViewPager(mViewPager)

        actionBarTitleTv.text = "消息"
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        actionBarTitleTv.text = "消息"
                    }
                    1 -> {
                        actionBarTitleTv.text = "通讯录"
                    }
                    2 -> {
                        actionBarTitleTv.text = "我的"
                    }
                }
            }
        })

        // 未读数据提示
        alphaIndicator!!.getTabView(0).showNumber(144)
        alphaIndicator!!.getTabView(1).showNumber(36)
        alphaIndicator!!.getTabView(2).showPoint()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}