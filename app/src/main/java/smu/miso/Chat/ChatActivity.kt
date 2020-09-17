package smu.miso.Chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import smu.miso.HomeActivity
import smu.miso.R
import smu.miso.SplashActivity

class ChatActivity : AppCompatActivity() {
    var mBackWait: Long = 0
    var userRef = FirebaseDatabase.getInstance().reference
    var user = FirebaseAuth.getInstance().currentUser
    var roomID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val splashIntent = Intent(this, SplashActivity::class.java)
        startActivity(splashIntent)
        supportActionBar!!.setDisplayShowTitleEnabled(false) // 타이틀 안보이게 하기
        //roomid 가져오기
        getRoomId()
        //채팅방 상태를 조회하여 채팅 방을 한명이라도 나가면 방을 폭파
        lookUpRooms()
        val fragobj = ChatFragment()
        val bundle = Bundle()

        bundle.putString("roomID", roomID)
        fragobj.arguments = bundle
        //chatting area
        supportFragmentManager.beginTransaction()
            //.replace(R.id.mainFragment, ChatFragment())
            .replace(R.id.mainFragment, ChatFragment())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_setting_chatroom, menu)
        return true
    }

    //채팅방 설정
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        // 채팅방 메뉴 항목 선택
        when (item.itemId) {
            //TODO : 채팅 방 나가기
            R.id.action_exitroom -> {
                exitRoom(roomID)
            }
            //TODO: 상대방 신고하기 기능 (개발 예정)
            R.id.action_opponent_alert -> {
                Toast.makeText(this, "상대방을 신고", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getRoomId() {
        //TODO :자신이 매칭된 채팅방 roomId 가져오기.
        //users - uid - ramdomRoomId
        userRef.child("users").child(user?.uid.toString()).child("randomRoomId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(roomIDsnapShot: DataSnapshot) {
                    roomID = roomIDsnapShot.value.toString()
                    Log.d("roomid 가져오기", roomID)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("roomid 가져오기", "실패")
                }
            })
    }

    private fun lookUpRooms() {
        userRef.child("rooms").child(roomID).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.key.toString()==roomID){
                    userRef.child("users").child(user?.uid.toString()).child("randomRoomId")
                        .setValue("")
                    Toast.makeText(
                        this@ChatActivity,
                        "매칭이 종료되었습니다. 새로운 채팅을 시작하세요",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }

            }
        })
    }

    private fun exitRoom(roomID: String) {
        //TODO: 가져온 roomid로 rooms의 방 폭파
        userRef.child("rooms").child(roomID).setValue(null)
        //TODO: users의 자신 randomRoomId 값 초기화
        userRef.child("users").child(user?.uid.toString()).child("randomRoomId").setValue("")
        finish()
    }

    fun getMyData(): String { //roomID반환함수
        return roomID
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(
                this,
                "뒤로가기 버튼을 한번 더 누르면 채팅이 종료 됩니다.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            stopSearching()

        }
    }
    fun stopSearching() {
        if (roomID != null) {
            exitRoom(roomID!!)
            finish()
        } else {
            Toast.makeText(
                this,
                "룸 아이디 가져오기 실패, 오류발생",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}