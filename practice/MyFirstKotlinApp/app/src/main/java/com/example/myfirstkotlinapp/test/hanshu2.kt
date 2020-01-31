package com.example.myfirstkotlinapp.test

fun<T,R> Collection<T>.fold(
    initial:R,
    combine:(acc:R,nextElement:T)->R
): R{
    var accumulator: R = initial
    for(element : T in this){
        accumulator = combine(accumulator,element)
    }
    return accumulator
}

fun main(){
    val items = listOf(1,2,3,4,5)

    items.fold(0){
        acc:Int , i:Int ->
        print("acc = $acc,i = $i")
        val result = acc + i
        println("result=$result")
        // 最后一个表达式的值就是返回值
        result
    }
}