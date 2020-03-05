package com.example.myhelotest

import android.os.Bundle
import android.util.Log
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

        // 设置按钮更改集合内容顺序
        val button: Button = view.findViewById(R.id.button)
        button.setOnClickListener {
            val fruitList = liveData?.value
            val fruit = fruitList?.first()
            val newList = fruitList?.drop(1) as MutableList
            println(fruit?.name+":"+fruit?.imageId)
            newList.add(fruit!!)
            model.setFruitsLiveData(newList)
        }
        // 设置适配器
        val mAdapter = FruitAdapter()
//        println(adapter?.mFruitList)
        println("Fragment1")
        // 设置LiveData改变后的响应事件->搭配DiffUtil动态定向局部刷新
        liveData?.observe(this, Observer {
            /**
             *  区分第一次渲染还是其他次修改
             *      既然是MVVM分层:
             *          Fragment视图层->只需要显示数据与视图的匹配即可->不需要关心数据从何而来
             *          LiveData的监听器->负责感知数据的变化，并告知视图层(Fragment)数据的变化
             */
            // 无论是LiveData从0->1还是进行局部变化->实际上都是集合的变化->全放在DiffUtil中执行即可
            // 使用DiffUtil->传入新老集合->老集合(原本adapter中的集合)，新集合(当前LiveData的集合)->只不过在从0->1的过程中，老集合为空集合
            val diffResult = DiffUtil.calculateDiff(DiffCallBack(mAdapter.mFruitList,it))
            // 将新数据集传给adapter
            mAdapter.mFruitList = it
            diffResult.dispatchUpdatesTo(mAdapter)
        })
        recyclerView.adapter = mAdapter
        return view
    }


}