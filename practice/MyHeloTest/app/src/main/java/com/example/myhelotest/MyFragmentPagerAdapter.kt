package com.example.myhelotest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MyFragmentPagerAdapter(fm:FragmentManager): FragmentPagerAdapter(fm){

//    private val fragmentList:MutableList<Fragment> = mutableListOf()
//
//    init {
//        fragmentList.add(MyFragment1())
//        fragmentList.add(MyFragment2())
//        fragmentList.add(MyFragment3())
//    }
    private val myFragment1 = MyFragment1()
    private val myFragment2 = MyFragment2()
    private val myFragment3 = MyFragment3()

    override fun getItem(position: Int): Fragment? {
        println("Adapter")
        println("============")
        var fragment: Fragment? = when(position){
            MainActivity.PAGE_ZERO->
                myFragment1
            MainActivity.PAGE_ONE->
                myFragment2
            MainActivity.PAGE_TWO->
                myFragment3
            else->
                null
        }
        return fragment
    }

    override fun getCount(): Int {
        return 3
    }

}