package com.example.myhelotest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyFragment3: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment3,container,false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view2)

        // 设置RecyclerView的布局->通过LinearLayoutManager->默认是垂直布局

        // 必须得使用LayoutManager指定RecyclerView的布局方式(默认是垂直布局)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        // 获取数据->通过ViewModel
        val model = activity?.let { ViewModelProviders.of(it).get(MyViewModel::class.java) }
        val liveData = model?.getFruitsLiveData()
        val fruitList = liveData?.value

        // 设置适配器
        val mAdapter = fruitList?.let { FruitAdapter2(it) }
//        println(adapter?.mFruitList)
        recyclerView.adapter = mAdapter
//        adapter?.notifyDataSetChanged()

        // 设置响应
        liveData?.observe(this, Observer {
            val diffResult = fruitList?.let { DiffUtil.calculateDiff(DiffCallBack(fruitList,it)) }
            mAdapter?.mFruitList = it
            mAdapter?.let { it1 -> diffResult?.dispatchUpdatesTo(it1) }
        })
        return view
    }
}