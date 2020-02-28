package com.ss.android.application.app.debug

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.i18n.business.debug.R
/**
 * 定义每个ViewPager的标准Fragment->含有一个RecyclerView
 */
class DebugStandardFragment(list: List<DebugDataModel>): Fragment(){

    private val mList = list

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.debug_standard_fragment,container,false)
        val recyclerView:RecyclerView? = mView?.findViewById(R.id.recycler_view)

        val layoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = layoutManager

        val mAdapter = DebugStandardAdapter(mList,context)

        recyclerView?.adapter = mAdapter
        return mView
    }
}
