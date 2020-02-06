package com.example.viewpagertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity(),ViewPager.OnPageChangeListener {


    // UI对象
    private var txtTopbar: TextView? = null
    private var rgTabBar: RadioGroup? = null
    private var rbChannel: RadioButton? = null
    private var rbMessage: RadioButton? = null
    private var rbBetter: RadioButton? = null
    private var rbSetting: RadioButton? = null

    companion object{
        //几个代表页面的常量
        val PAGE_ONE = 0
        val PAGE_TWO = 1
        val PAGE_THREE = 2
        val PAGE_FOUR = 3
    }

    // viewPager及其适配器
    private var vpager: ViewPager? = null
    private var mAdapter: MyFragmentPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 创建适配器
        mAdapter = MyFragmentPagerAdapter(supportFragmentManager)

        bindViews()
        rbChannel?.isChecked = true
    }

    private fun bindViews() {
        txtTopbar = findViewById(R.id.txt_topbar)
        rgTabBar= findViewById(R.id.rg_tab_bar)
        rbChannel = findViewById(R.id.rb_channel)
        rbMessage = findViewById(R.id.rb_message)
        rbBetter = findViewById(R.id.rb_better)
        rbSetting = findViewById(R.id.rb_setting)
        // 初始化ViewPager
        vpager = findViewById(R.id.vpager)
        // 设置适配器
        vpager?.adapter = mAdapter
        vpager?.currentItem = 0
        vpager?.addOnPageChangeListener(this)
        // 设置选项
        rgTabBar?.setOnCheckedChangeListener {
                group, checkedId ->
                    when(checkedId){
                        R.id.rb_channel-> vpager?.currentItem = PAGE_ONE
                        R.id.rb_message-> vpager?.currentItem = PAGE_TWO
                        R.id.rb_better-> vpager?.currentItem = PAGE_THREE
                        R.id.rb_setting-> vpager?.currentItem = PAGE_FOUR
                    }
        }
    }

    // 重写ViewPagery页面切换处理方法
    override fun onPageScrollStateChanged(state: Int) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state === 2) {
            when (vpager?.currentItem) {
                PAGE_ONE -> rbChannel?.isChecked = true
                PAGE_TWO -> rbMessage?.isChecked = true
                PAGE_THREE -> rbBetter?.isChecked = true
                PAGE_FOUR -> rbSetting?.isChecked = true
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }
}
