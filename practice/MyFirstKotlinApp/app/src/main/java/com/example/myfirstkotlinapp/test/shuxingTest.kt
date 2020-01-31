package com.example.myfirstkotlinapp.test

class Address{
    var name: String = "Hello"
    var state: String? = null
}

fun main(){
    val address = Address()
    address.name = "HEHE"
    address.state = "HAHA"
    val result: Address = copy(address)
    println(result.name)
    println(result.state)
}

fun copy(address: Address): Address{
    val result = Address()
    result.name = address.name
    result.state = address.state
    return result
}

