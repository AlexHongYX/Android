package com.example.activitytest

open class Parent{
    protected var rootViewGroup:MutableList<String> = mutableListOf()
}

class Child1 : Parent() {
    fun add(){
        rootViewGroup.add("child1")
    }
}

class Child2 : Parent(){
    fun add(){
        rootViewGroup.add("child2")
    }
}

fun main(){
    val child1 = Child1()
    val child2 = Child2()

    child1.add()
    child2.add()

}