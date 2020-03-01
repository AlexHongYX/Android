package com.example.listviewtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

data class Fruit(val name:String,val imageId:Int)

class MainActivity : AppCompatActivity() {

    val fruitList = mutableListOf<Fruit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFruits()    // 初始化水果数据
//        调用自定义适配器
        val adapter = FruitAdapter(this,R.layout.fruit_item,fruitList)
//        找到ListView控件
        val listView:ListView = this.findViewById(R.id.list_view)
//        给ListView控件添加自定义适配器
        listView.adapter = adapter
//        调用系统适配器
//        val adapter = ArrayAdapter<String>(
//            this,android.R.layout.simple_list_item_1,data)
//        val listView:ListView = findViewById(R.id.list_view)
//        listView.adapter = adapter

//        注册监听器
        listView.setOnItemClickListener(){
            parent, view, position, id ->
                val fruit:Fruit = fruitList[position]
            Toast.makeText(this,fruit.name,Toast.LENGTH_SHORT).show()
        }
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
