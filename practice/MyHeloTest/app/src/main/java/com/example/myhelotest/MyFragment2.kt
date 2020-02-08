package com.example.myhelotest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyFragment2: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment2,container,false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view2)

        // 设置RecyclerView的布局->通过LinearLayoutManager->默认是垂直布局

        // 获取数据->通过ViewModel
        val model = activity?.let { ViewModelProviders.of(it).get(MyViewModel::class.java) }
        val fruitList = model?.getFruitsLiveData()?.value

        // 必须得使用LayoutManager指定RecyclerView的布局方式(默认是垂直布局)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        // 设置适配器
        val adapter = fruitList?.let { FruitAdapter2(it) }
        println(adapter?.mFruitList)
        recyclerView.adapter = adapter
        adapter?.notifyDataSetChanged()

        println("Fragment2")
        return view
    }
}