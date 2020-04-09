package com.example.broadcastbestpractice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val accountEdit:EditText = findViewById(R.id.account)
        val passwordEdit:EditText = findViewById(R.id.password)
        val login: Button = findViewById(R.id.login)

        // 获取SharedPreferences对象
        val pref = getSharedPreferences("long_message", Context.MODE_PRIVATE)
        val rememberPass:CheckBox = findViewById(R.id.remember_pass)
        // 先获取SharedPreferences中的信息，看是否已存在账号密码
        val isRemember = pref.getBoolean("remember_password",false)
        // 如果账号密码已存在就显示出来(相当于是加载了以保存的信息)
        if (isRemember){
            // 将账号密码设置在文本框中
            val account = pref.getString("account","")
            val password = pref.getString("password","")
            accountEdit.setText(account)
            passwordEdit.setText(password)
            // 将复选框设置为true
            rememberPass.isChecked = true

        }

        login.setOnClickListener {
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            // 账号密码都为123456就为登陆成功
            if(account == "admin" && password == "admin"){
                // 如果复选框被选中，将信息保存到SharedPreferences对象中
                val editor = pref.edit()
                if (rememberPass.isChecked){
                    // 写入SharedPreferences对象
                    editor.putString("account",account)
                    editor.putString("password",password)
                    editor.putBoolean("remember_password",true)
                }else{
                    // 如果没被选中，则清除该文件的所有信息
                    editor.clear()
                }
                editor.apply()

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,"account or password is invalid",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
