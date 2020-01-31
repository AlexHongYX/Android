package com.example.myfirstkotlinapp.test

class People{

    lateinit var age:HEH

    constructor(value:Int){
        this.allByDefault = value
    }

    fun setAge2(v:HEH) {
        this.age = HEH()
    }

    var allByDefault:Int = 0
        get():Int{
            if (field%2==0)
                return field/2
            else
                return field

        }
        set(value){
            println(value)
            field = value
        }
}

fun main(){
    var people = People(17)
    people.allByDefault = 16
    println(people.allByDefault)

}

class HEH{

}