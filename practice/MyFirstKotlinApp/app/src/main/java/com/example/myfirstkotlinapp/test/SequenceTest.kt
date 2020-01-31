package com.example.myfirstkotlinapp.test

fun main(){

    val sequence = sequenceOf(1, 2, 3, 4, 5, 6)
    val result = sequence
        .map{ println("In Map $it"); it * 2 }
        .filter { println("In Filter $it");it % 3  == 0 }
    println(result.first())
}

