package com.example.myhelotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity(),ViewPager.OnPageChangeListener {

    // UI对象
    private var radioGroup: RadioGroup? = null
    private var followBtn: RadioButton? = null
    private var popularBtn: RadioButton? = null
    private var nearbyBtn: RadioButton? = null
    // viewpager及adapter
    private var vPager:ViewPager? = null
    private var adapter:MyFragmentPagerAdapter? = null
    // 代表页面的常量->静态
    companion object {
        val PAGE_ZERO = 0
        val PAGE_ONE = 1
        val PAGE_TWO = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 创建适配器
        adapter = MyFragmentPagerAdapter(supportFragmentManager)

        bindViews()
        // 设置默认选择follow
        popularBtn?.isChecked = true
    }

    /**
     * 渲染界面
     */
    private fun bindViews() {
        radioGroup = findViewById(R.id.radioGroup)
        followBtn = findViewById(R.id.follow_btn)
        popularBtn = findViewById(R.id.popular_btn)
        nearbyBtn = findViewById(R.id.nearby_btn)
        // 初始化viewpager
        vPager = findViewById(R.id.vPager)
        // 设置ViewPager适配器
        vPager?.adapter = adapter
        // 设置该vPage的初始选项->follow
        vPager?.currentItem = 1
        // 设置当前activity监听ViewPager的变化
        vPager?.addOnPageChangeListener(this)

        // 设置点击按钮变化-监听点击事件
        radioGroup?.setOnCheckedChangeListener {
                group, checkedId ->
                    // 根据ID修改vpager的显示页面
                    when(checkedId){
                        R.id.follow_btn->vPager?.currentItem = PAGE_ZERO
                        R.id.popular_btn->vPager?.currentItem = PAGE_ONE
                        R.id.nearby_btn->vPager?.currentItem = PAGE_TWO
                    }
        }
    }

    /**
     * 页面滚动状态改变后回调
     * 一旦通过滑动改变当前页面->就会调用该方法
     */
    override fun onPageScrollStateChanged(state: Int) {
        // start==2->页面滑动完毕
        if (state == 2){
            // 判断当前的currentItem，根据currentItem判断当前是那个页面，转换到该页面的按钮上(回调按钮的点击事件)
            when(vPager?.currentItem){
                PAGE_ZERO->followBtn?.isChecked = true
                PAGE_ONE->popularBtn?.isChecked = true
                PAGE_TWO->nearbyBtn?.isChecked = true
            }
        }
    }

    // 下面两个西安先不用具体实现，只需要实现OnPageScrollStateChanged
    /**
     * 页面滚动中回调
     */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    /**
     * 页面滚动结束后回调
     */
    override fun onPageSelected(position: Int) {
    }
}
