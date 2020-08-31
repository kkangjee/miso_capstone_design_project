package smu.miso.Chat

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_chat.*
import smu.miso.R
import smu.miso.SplashActivity


class ChatActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()

        val splashIntent = Intent(this, SplashActivity::class.java)
        startActivity(splashIntent)

        // chatting area
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragment, ChatFragment())
            .commit()



    }
}