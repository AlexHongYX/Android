package com.ss.android.application.app.debug

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class DebugFragmentPagerAdapter(fm:FragmentManager,fragments:MutableList<Fragment>,titles:MutableList<String>): FragmentPagerAdapter(fm){

    private val mFragment = fragments
    private val mTitles = titles

    override fun getItem(position: Int): Fragment {
        return mFragment[position]
    }

    override fun getCount(): Int {
        return mFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }
}
