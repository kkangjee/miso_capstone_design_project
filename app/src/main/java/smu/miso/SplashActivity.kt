package smu.miso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONObject
import smu.miso.Chat.ChatActivity
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class SplashActivity : AppCompatActivity() {
    var mBackWait: Long = 0
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.reference
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var getRoomId: String? = ""
    private var department: String? = ""
    var myToken: String = FirebaseInstanceId.getInstance().getToken().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //대기화면에서 대기상태 종료하는 조건은 2가지
        //1. 종료 버튼을 누른다
        //2. 뒤로가기 버튼을 두번 누른다
        supportActionBar!!.setDisplayShowTitleEnabled(false) // 타이틀 안보이게 하기

        //1. 종료 버튼을 누른다
        end_button.setOnClickListener {
            stopSearching()
            Toast.makeText(this, "대기상태 종료", Toast.LENGTH_LONG).show()
            finish()
        }

        //ChatActivity에서 room id를 받아온다
        if (intent.hasExtra("splash_room_id")) {//user1
            getRoomId = intent.getStringExtra("splash_room_id")

            userRef.child("rooms").child(getRoomId!!).child("users").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var cnt = 0
                    department = snapshot.child(uid).value.toString()

                    //두명이 채팅방에 들어왓을 때 실행 됨
                    for (singleSnapshot in snapshot.children) {
                        cnt++
                        if (cnt == 2) {
                            CloudFunctions.sendFCM(myToken)
                            finish()
                            val nextIntent = Intent(this@SplashActivity, ChatActivity::class.java)
                            startActivity(nextIntent)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        } else {//user2
            Toast.makeText(this, "사용자가 입장했습니다. 채팅을 엽니다.", Toast.LENGTH_LONG).show()
            finish()
        }
        lookUpRooms()

    }

    //탐색을 종료하는 코드
    fun stopSearching() {
        if (getRoomId != null) {
            exitRoom(getRoomId!!)
            finish()
        } else {
            Toast.makeText(
                this,
                "룸 아이디 가져오기 실패, 오류발생",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //2. 뒤로가기 버튼을 두번 누른다
    //뒤로가기 버튼 두번 클릭 시 탐색 종료
    override fun onBackPressed() {
        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(
                this,
                "뒤로가기 버튼을 한번 더 누르면 채팅 탐색이 종료 됩니다.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            stopSearching()
        }
    }

    //방을 나가는 메소드
    private fun exitRoom(roomID: String) {
        //TODO: 가져온 roomid로 rooms의 방 폭파
        userRef.child("rooms").child(roomID).setValue(null)
        //TODO: users의 자신 randomRoomId 값 초기화
        userRef.child("users").child(uid).child("randomRoomId").setValue("")
        userRef.child("deptMap").child(department.toString()).setValue("")
        finish()
    }

    //사용자의 randomRoomId을 null로 초기화한다.
    private fun lookUpRooms() {
        if (getRoomId != null) {
            userRef.child("rooms").child(getRoomId!!)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        userRef.child("users").child(uid).child("randomRoomId")
                            .setValue("")
                    }
                })
        }

    }
}