package com.example.listviewtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

//最普通的方式
//class FruitAdapter(context:Context,textViewResourceId:Int,objects:List<Fruit>)
//    : ArrayAdapter<Fruit>(context,textViewResourceId,objects){
//
//    val resourceId:Int = textViewResourceId
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
////        getItem实际上就是在数据集合中使用get(position)查找
////        在数据集合中找到适配类型的对象
//        val fruit:Fruit? = getItem(position)
////        找到当前控件所对应的视图
//        val view:View = LayoutInflater.from(context).inflate(resourceId,parent,false)
////        以下几步操作就是使用找到的对象的相关属性来渲染找到的视图
////        获取当前视图的两个属性
//        val fruitImage:ImageView = view.findViewById(R.id.fruit_image)
//        val fruitName:TextView = view.findViewById(R.id.fruit_name)
////        使用对象的属性给视图的属性赋值
//        fruit?.imageId?.let { fruitImage.setImageResource(it) }
//        fruitName.text = fruit?.name
//        return view
//    }
//}

//Adapter优化
class FruitAdapter(context:Context,textViewResourceId:Int,objects:List<Fruit>)
    : ArrayAdapter<Fruit>(context,textViewResourceId,objects){

    val resourceId:Int = textViewResourceId

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val fruit:Fruit? = getItem(position)

//        一级缓存
        val view:View
//        二级缓存
        val viewHolder:ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId,parent,false)

            viewHolder = ViewHolder(view.findViewById(R.id.fruit_image),view.findViewById(R.id.fruit_name))

            view.tag = viewHolder

        }else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
//        通过ViewHolder类渲染视图
        fruit?.imageId?.let { viewHolder.fruitImage.setImageResource(it) }
        fruit?.name?.let { viewHolder.fruitName.setText(it) }
        return view
    }

    data class ViewHolder(val fruitImage:ImageView,val fruitName:TextView)
}