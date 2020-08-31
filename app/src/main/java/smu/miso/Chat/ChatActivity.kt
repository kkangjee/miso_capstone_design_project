package smu.miso.Chat

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import smu.miso.R


class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //supportActionBar?.hide()

        supportActionBar!!.setDisplayShowTitleEnabled(false) // 타이틀 안보이게 하기

        // chatting area
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragment, ChatFragment())
            .commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_setting_chatroom, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        // 채팅방 메뉴 항목 선택
        //TODO : 채팅 방 나가기, 상대방 신고 기능 추가.
        when (item.itemId){
            R.id.action_exitroom -> {
                Toast.makeText(this, "채팅 방을 폭파", Toast.LENGTH_SHORT).show()
            }
            R.id.action_opponent_alert -> {
                Toast.makeText(this, "상대방을 신고",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


