package com.example.myfirstkotlinapp.test

fun main(){
    val list = listOf(1, 2, 3, 4, 5, 6)
    val (match,rest) = list.partition {
        it % 2 == 0
    }

    println(match)
    println(rest)
}
