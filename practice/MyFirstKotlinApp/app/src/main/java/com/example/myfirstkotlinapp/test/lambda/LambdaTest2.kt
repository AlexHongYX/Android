package com.example.myfirstkotlinapp.test.lambda

fun buildMap(build:HashMap<Int,String>.()->Unit):Map<Int,String>{
    val hashMap = HashMap<Int,String>()
    hashMap.build()
    return hashMap
}

fun usage(): Map<Int, String> {
    return buildMap {
        put(0, "0")
        for (i in 1..10) {
            put(i, "$i")
        }
    }
}

fun main(){

}