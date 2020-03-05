package com.example.viewpagertest

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyFragmentPagerAdapter(fm:FragmentManager): FragmentPagerAdapter(fm){

    private val PAGER_COUNT = 4
    private val myFragment1 = MyFragment1()
    private val myFragment2 = MyFragment2()
    private val myFragment3 = MyFragment3()
    private val myFragment4 = MyFragment4()


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getItem(position: Int): Fragment? {
        println("Adapter")
        var fragment: Fragment? = when(position){
            MainActivity.PAGE_ONE->
                myFragment1
            MainActivity.PAGE_TWO->
                myFragment2
            MainActivity.PAGE_THREE->
                myFragment3
            MainActivity.PAGE_FOUR->
                myFragment4
            else->
                null
        }
        return fragment
    }

    override fun getCount(): Int {
        return PAGER_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return super.getPageTitle(position)
    }
}