package com.example.myfirstkotlinapp.test

fun main() {
}

class Person {

    init {
        print("AAA")
    }
    constructor(a:String,b:String)  {

    }

}

class Student{
    var name:String? = null
    var id:Int = 0

    constructor(name: String){
        this.name = name;
    }

    constructor(id:Int){
        this.id = id;
    }
}
