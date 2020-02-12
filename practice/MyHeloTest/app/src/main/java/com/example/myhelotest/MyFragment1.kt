package com.example.myhelotest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyFragment1: Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment1,container,false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view1)

        // 设置RecyclerView的布局->通过LinearLayoutManager->默认是垂直布局

        // 必须得使用LayoutManager指定RecyclerView的布局方式(默认是垂直布局)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        // 获取数据->通过ViewModel
        val model = activity?.let { ViewModelProviders.of(it).get(MyViewModel::class.java) }
        val liveData = model?.getFruitsLiveData()
        val fruitList = liveData?.value
//        println(fruitList)
        // 设置适配器
        val mAdapter = fruitList?.let { FruitAdapter(it) }
//        println(adapter?.mFruitList)
        recyclerView.adapter = mAdapter
        println("Fragment1")
        // 设置LiveData改变后的响应事件->搭配DiffUtil动态定向局部刷新
        liveData?.observe(this, Observer {
            // 使用DiffUtil
            val diffResult = fruitList?.let { DiffUtil.calculateDiff(DiffCallBack(fruitList,it)) }
            // 将新数据集传给adapter
            mAdapter?.mFruitList = it
            mAdapter?.let { it1 -> diffResult?.dispatchUpdatesTo(it1) }
        })
//        Log.d("onCreateView:","onCreateView")
        return view
    }


}