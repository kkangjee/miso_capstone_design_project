package smu.miso.Chat

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_chat.*
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