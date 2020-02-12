package com.example.tablayouttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myTab:TabLayout = findViewById(R.id.my_tab)
        val myVP:ViewPager = findViewById(R.id.my_vp)

        val titles = mutableListOf<String>()
        val fragments = mutableListOf<Fragment>()

        titles.add("选项卡1")
        titles.add("选项卡2")
        titles.add("选项卡3")
        titles.add("选项卡4")

        fragments.add(MyFragment1())
        fragments.add(MyFragment2())
        fragments.add(MyFragment3())
        fragments.add(MyFragment4())

        myVP.adapter = MyFragmentPagerAdapter(supportFragmentManager,fragments,titles)

        myTab.setupWithViewPager(myVP)


    }
}
