package com.sinothk.openfire.android.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.sinothk.openfire.android.demo.utils.ActivityUtil
import com.sinothk.openfire.android.demo.view.chat.ChatFragment
import com.sinothk.openfire.android.demo.view.contacts.ContactsFragment
import com.sinothk.openfire.android.demo.view.mine.MineFragment
import com.sinothk.tab.weiXin.WxTabMenuMainAdapter
import kotlinx.android.synthetic.main.activity_main.*


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

        // 未读数据提示
        alphaIndicator!!.getTabView(0).showNumber(144)
        alphaIndicator!!.getTabView(1).showNumber(100)
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