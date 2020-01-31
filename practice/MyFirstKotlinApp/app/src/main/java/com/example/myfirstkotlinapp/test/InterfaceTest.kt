package com.example.myfirstkotlinapp.test

interface A{
    fun foo(){
        println("A")
    }
    fun bar()
}

interface B{
    fun foo(){
        println("B")
    }
    fun bar(){
        println("B")
    }
}

class C: A{
    override fun bar(){
        println("bar")
    }
}

class D:A,B{
    override fun foo(){
        super<A>.foo()
        super<B>.foo()
    }

    override fun bar(){
        super.bar()
    }
}

fun main(){
    var d = D()
    d.foo()
    d.bar()
}