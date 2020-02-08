package com.example.helotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myhelotest.Fruit
import com.example.myhelotest.FruitAdapter

class MainActivity : AppCompatActivity() {

    val fruitList = mutableListOf<Fruit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view1)

        // 设置RecyclerView的布局->通过LinearLayoutManager->默认是垂直布局

        // 获取数据->通过ViewModel
//        val model = ViewModelProviders.of(this).get(MyViewModel::class.java)
//        val fruitList = model.getFruitsLiveData().value

        initFruits()

        val layoutManager = LinearLayoutManager(this)
//        设置为纵向的线性布局，相应的需要修改视图的布局，默认是垂直布局
//        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        // 设置适配器
        val adapter = FruitAdapter(fruitList)
        println(adapter.mFruitList)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun initFruits() {
        for(i in 0..1){
            val apple = Fruit("Apple",R.drawable.apple_pic)
            fruitList.add(apple)
            val banana = Fruit("Banana",R.drawable.banana_pic)
            fruitList.add(banana)
            val orange = Fruit("Orange",R.drawable.orange_pic)
            fruitList.add(orange)
            val watermelon = Fruit("Watermelon",R.drawable.watermelon_pic)
            fruitList.add(watermelon)
            val pear = Fruit("Pear",R.drawable.pear_pic)
            fruitList.add(pear)
            val grape = Fruit("Grape",R.drawable.grape_pic)
            fruitList.add(grape)
            val pineapple = Fruit("Pineapple",R.drawable.pineapple_pic)
            fruitList.add(pineapple)
            val strawberry = Fruit("Strawberry",R.drawable.strawberry_pic)
            fruitList.add(strawberry)
            val cherry = Fruit("Cherry",R.drawable.cherry_pic)
            fruitList.add(cherry)
            val mango = Fruit("Mango",R.drawable.mango_pic)
            fruitList.add(mango)
        }
    }
}
