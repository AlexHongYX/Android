package com.example.diffutiltest

class TestBean(iname:String,idesc:String,ipic:Int): Cloneable{
    var name = iname
    var desc = idesc
    var pic = ipic

    fun cloneObject(): TestBean {
        return super.clone() as TestBean
    }


}