package com.example.tablayouttest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyFragmentPagerAdapter(fm:FragmentManager, list:List<Fragment>,titles:List<String> ): FragmentPagerAdapter(fm){

    private val mList = list
    private val mTitles = titles

    override fun getItem(position: Int): Fragment {
        return mList[position]
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }
}