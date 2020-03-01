package com.example.recyclerviewtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

//主构造函数接收数据集合，将数据集合读入该适配器
class FruitAdapter(fruitList:List<Fruit>) :RecyclerView.Adapter<FruitAdapter.ViewHolder>(){

    /**
     * ViewHolder相当于是做了View类的缓存
     *  该类的属性保存View视图的相关控件->对应fruit_item.xml设置的控件
     *  好处：使用ViewHolder类访问控件，避免每次都使用findViewXXX()访问控件
     */
    class ViewHolder(view:View) :RecyclerView.ViewHolder(view){
        val fruitImage:ImageView = view.findViewById(R.id.fruit_image)
        val fruitName:TextView = view.findViewById(R.id.fruit_name)

//        添加View对象，用ViewHolder封装View，用于点击事件的操作
        val fruitView:View = view
    }

//    将要展示的数据源列表赋值给全局变量mFruitList
    val mFruitList:List<Fruit> = fruitList

    /**
     * 创建ViewHolder实例
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        获取fruit_item.xml(自定义布局)所对应的View对象
        val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fruit_item,parent,false)
//        创建该View对象对应的ViewHolder对象
        val holder = ViewHolder(view)
//        设置该holder所封装的view的相关点击事件
//        点击整个控件的View
        holder.fruitView.setOnClickListener{
            val position:Int = holder.adapterPosition
            val fruit:Fruit = mFruitList[position]
            Toast.makeText(it.context,"you clicked view "+fruit.name,Toast.LENGTH_SHORT).show()
        }
//        点击控件中的Image(图片)属性
        holder.fruitImage.setOnClickListener{
            val position:Int = holder.adapterPosition
            val fruit:Fruit = mFruitList[position]
            Toast.makeText(it.context,"you clicked image "+fruit.name,Toast.LENGTH_SHORT).show()
        }
        return holder
    }

    /**
     * 对RecycleView每一项的数据进行赋值(当子项被滚动到屏幕内时执行)，与ListView类似
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        获取当前子项对象
        val fruit:Fruit = mFruitList.get(position)
//        通过ViewHolder使用对象属性来渲染其所对应的布局
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.setText(fruit.name)
    }

    /**
     * 一种重载，由于布局的复用的，有可能只有部分需要进行修改(payLoads)，因此可以引入该参数
     */
//    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
//        super.onBindViewHolder(holder, position, payloads)
//    }

    /**
     * 返回RecyclerView一共有多少子项，返回数据源长度即可
     */
    override fun getItemCount(): Int {
        return mFruitList.size
    }
}