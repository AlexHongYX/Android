package com.example.myfirstkotlinapp.test.lambda

fun task(): List<Boolean> {
    val isEven: Int.() -> Boolean = { this % 2 == 0 }
    val isOdd: Int.() -> Boolean = { this % 2 != 0 }

    return listOf(42.isOdd(), 239.isOdd(), 294823098.isEven())
}

fun main(){
    val s = buildString {
        this.append("Numbers: ")
        for(i in 1..3){
            this.append(i)
        }
    }
    println(s)
}

fun buildString(build: StringBuilder.()->Unit):String {
    val stringBuilder = java.lang.StringBuilder()
    stringBuilder.build()
    return stringBuilder.toString()
}

