package com.example.broadcasttest3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadcastReceiver2 : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"Receive2",Toast.LENGTH_SHORT)
            .show()

        abortBroadcast()
    }

}