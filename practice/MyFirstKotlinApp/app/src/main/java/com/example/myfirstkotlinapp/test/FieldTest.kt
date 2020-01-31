package com.example.myfirstkotlinapp.test

open class Shape(var str:String){

    constructor(str:String,age:Int):this(str){
        println(str)
        println(age)
    }

    fun draw(){
        println("Shape draw")
    }

    fun fill(){
        println("Shape fill")
    }
}

class Circle(str:String): Shape(str){

    constructor(str:String,age:Int):this(str){
        println("Child")
    }

    fun draw1(){
        println("Circle Draw")
    }
}

fun main(){
    var circle = Circle("Hello",15)
}

open class Father{

    constructor(){
        print(" ")
    }

    open val ver:Int = 0
}

class Child : Father{

    constructor(){

    }

    override val ver:Int = 0
}