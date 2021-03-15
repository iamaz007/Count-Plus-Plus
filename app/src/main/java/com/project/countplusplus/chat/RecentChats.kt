package com.project.countplusplus.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.countplusplus.R
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_payment.goBack
import kotlinx.android.synthetic.main.activity_recent_chats.*

class RecentChats : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_chats)

        goBackFunc()
    }

    private fun goBackFunc() {
        goBackRecentChat.setOnClickListener{
            this.finish()

        }
    }
}