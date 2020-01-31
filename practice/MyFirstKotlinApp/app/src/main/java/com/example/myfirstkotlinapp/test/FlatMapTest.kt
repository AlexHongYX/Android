package com.example.myfirstkotlinapp.test

fun main(){
    val numbers = listOf(1,3,-4,2,-11)
    val positive = numbers.partition { it > 0 }
    println(positive)
}