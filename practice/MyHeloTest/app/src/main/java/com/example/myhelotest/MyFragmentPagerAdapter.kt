package com.example.myhelotest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MyFragmentPagerAdapter(fm:FragmentManager): FragmentPagerAdapter(fm){

    private val fragmentList:MutableList<Fragment> = mutableListOf()

    init {
        fragmentList.add(MyFragment1())
        fragmentList.add(MyFragment2())
        fragmentList.add(MyFragment3())
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

}