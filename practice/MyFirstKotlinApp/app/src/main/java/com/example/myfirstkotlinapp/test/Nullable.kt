package com.example.myfirstkotlinapp.test

fun sendMessageToClient(
    client: Client?, message: String?, mailer: Mailer
){

//    if(client == null||message== null){
//        return
//    }
//    var personalInfo:PersonalInfo? = client.personalInfo
//    if(personalInfo==null){return}
//    var email:String? = personalInfo.email
//    if(email==null){return}
//
//    mailer.sendMessage(email,message)
    var email = client?.personalInfo?.email
    if(email!=null&&message!=null){
        mailer.sendMessage(email,message)
    }
}

class Client (val personalInfo: PersonalInfo?)
class PersonalInfo (val email: String?)
interface Mailer {
    fun sendMessage(email: String, message: String)
}

fun main(){
    var name: String? = "ehhe"
    if(name!=null){

        var len = name.length
        println(len)
    }
}