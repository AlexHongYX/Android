package com.example.recyclerviewtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Fruit(val name:String,val imageId:Int)

class MainActivity : AppCompatActivity() {

    val fruitList = mutableListOf<Fruit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFruits()
        val recyclerView:RecyclerView = findViewById(R.id.recycler_view)

        val animation = AnimationUtils.loadAnimation(this,R.anim.view_animation)

        val controller = LayoutAnimationController(animation)
        controller.delay = 0.5f
        controller.order = LayoutAnimationController.ORDER_NORMAL

        recyclerView.layoutAnimation = controller

//        由于RecyclerView支持纵向、横向、瀑布流布局
//        因此需要使用LayoutManager指定RecyclerView的布局方式(默认是垂直布局)
//        LinearLayoutManager采用就是线性布局(默认垂直)
        val layoutManager = LinearLayoutManager(this)
//        设置为纵向的线性布局，相应的需要修改视图的布局，默认是垂直布局
//        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        val adapter = FruitAdapter(fruitList)
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
