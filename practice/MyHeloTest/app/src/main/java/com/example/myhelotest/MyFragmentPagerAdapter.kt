package com.example.myhelotest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MyFragmentPagerAdapter(fm:FragmentManager,fragments:List<Fragment>,titles:List<String>)
    : FragmentPagerAdapter(fm){

    private val mFragments = fragments
    private val mTitles = titles

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    /**
     * 设置ViewPager的标题
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

    //    override fun getItem(position: Int): Fragment? {
//        println("Adapter")
//        println("============")
//        var fragment: Fragment? = when(position){
//            MainActivity.PAGE_ZERO->
//                myFragment1
//            MainActivity.PAGE_ONE->
//                myFragment2
//            MainActivity.PAGE_TWO->
//                myFragment3
//            else->
//                null
//        }
//        return fragment
//    }
//
//    override fun getCount(): Int {
//        return 3
//    }

}