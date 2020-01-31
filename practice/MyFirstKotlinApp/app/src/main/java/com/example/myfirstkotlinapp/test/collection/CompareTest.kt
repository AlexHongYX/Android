package com.example.myfirstkotlinapp.test.collection

import java.util.*


//data class Person(var name:String,var age:Int){}


fun main(){
//    var list:MutableList<Person> = mutableListOf(Person("张三",14),
//                                   Person("李四",7),
//                                   Person("王五",7),
//                                   Person("赵七",24),
//                                   Person("狗八",17))
//    println(list)
//    println("==================================")

    val arrayList = arrayListOf(1, 5, 2)
    arrayList.sortWith(Comparator{ x, y-> y-x})
    Collections.sort(arrayList,{x,y->x-y})


    println(arrayList)
}