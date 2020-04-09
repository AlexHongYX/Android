package com.example.contactstest

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // 数据集合
    private val contactsList = mutableListOf<String>()

    // ListView的适配器
    private var adapter:ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 获取ListView并设置适配器
        val listView: ListView = this.findViewById(R.id.contacts_view)
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,contactsList)
        contacts_view.adapter = adapter

        // 判断是否已授权
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),1)
        }else{
            // 读取联系人的逻辑
            readContacts()
        }
    }

    /**
     * 读取联系人
     */
    private fun readContacts() {
        var cursor:Cursor? = null
        try {
            // 查询联系人数据
            cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                ,null,null,null,null)
            if (cursor != null){
                while (cursor.moveToNext()){
                    // 获取联系人姓名
                    val displayName = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    // 获取联系人手机号
                    val number = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER))
                    // 加入到集合中
                    contactsList.add(displayName+"\n"+number)
                }
                // 重新加载适配器
                adapter?.notifyDataSetChanged()
            }
        }catch (e: SecurityException){
            e.printStackTrace()
        }finally {
            // 关闭Cursor
            cursor?.close()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            1 ->
                if (grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readContacts()
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show()
                }
        }
    }
}
