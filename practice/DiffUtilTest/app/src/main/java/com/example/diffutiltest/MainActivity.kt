package com.example.diffutiltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var mDatas:MutableList<TestBean> = mutableListOf()
    private var mAdapter:DiffAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        val mRv: RecyclerView = findViewById(R.id.rv)
        mRv.layoutManager = LinearLayoutManager(this)
        mAdapter = DiffAdapter(mDatas)
        mRv.adapter = mAdapter
    }

    private fun initData() {
        mDatas.add(TestBean("张旭童1", "Android", R.drawable.pic1))
        mDatas.add(TestBean("张旭童2", "Java", R.drawable.pic2))
        mDatas.add(TestBean("张旭童3", "背锅", R.drawable.pic3))
        mDatas.add(TestBean("张旭童4", "手撕产品", R.drawable.pic4))
        mDatas.add(TestBean("张旭童5", "手撕测试", R.drawable.pic5))
    }

    /**
     * 模拟刷新操作
     */
    fun onRefresh(view: View){
        val newDatas = mutableListOf<TestBean>()
        mDatas.forEach{
            // clone一遍旧数据 ，模拟刷新操作
            newDatas.add(it.cloneObject())
        }
        // 模拟新增数据
        newDatas.add(TestBean("赵子龙", "帅", R.drawable.pic6))
        newDatas[0].desc = "Android+"
        //模拟修改数据
        newDatas[0].pic = R.drawable.pic7
        //模拟数据位移
        val testBean = newDatas[1]
        newDatas.remove(testBean)
        newDatas.add(testBean)
        //将新数据给Adapter
//        mDatas = newDatas
//        mAdapter?.mDatas = mDatas
        //以前大多数情况下只能这样
//        mAdapter?.notifyDataSetChanged()
        // 使用DiffUtil
        // 先得到DiffResult对象
        val diffResult = DiffUtil.calculateDiff(DiffCallBack(mDatas,newDatas),true)


        // 将新数据传给Adapter
        mDatas = newDatas
        mAdapter?.mDatas = mDatas

//        println(newDatas)
//        println(mAdapter?.mDatas)
        // 传入Adapter
        mAdapter?.let { diffResult.dispatchUpdatesTo(it) }
    }
}
