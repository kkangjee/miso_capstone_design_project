package smu.miso.Chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import smu.miso.R


class ChatActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()

        // chatting area
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragment, ChatFragment())
            .commit()
    }

}