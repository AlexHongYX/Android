package com.example.myfirstkotlinapp.test

import java.util.*
import kotlin.Comparator

fun getList(): List<Int> {
    val arrayList = arrayListOf(1, 5, 2)
    Collections.sort(arrayList, object :Comparator<Int>{
        override fun compare(o1: Int?, o2: Int?): Int {
            if(o1==null||o2==null){
                return 0
            }
            return o2-o1
        }
    })
    return arrayList
}

fun main(){
    println(getList())
}