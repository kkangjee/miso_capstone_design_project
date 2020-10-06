package smu.miso.ui.freetalk

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*
import smu.miso.R
import smu.miso.model.ChatModel
import java.util.ArrayList

class FreeTalkFragment : Fragment() {
    //전역 변수 설정 공간
    private val freeTalk = "freeTalk"

    //var randomRoomId = ""
    var userRef = FirebaseDatabase.getInstance().reference
    var user = FirebaseAuth.getInstance().currentUser
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    var messageList: ArrayList<ChatModel.Message> = arrayListOf()
    var chatAdapter: FreeTalkFragmentAdapter? = null

    val resetCount = 10

    //기본 구성 함수(Don't touch)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {//여기까지

        //어떤 화면을 container(프레그먼트가 보여질 화면)에 보여질 것인지 설정

        //return inflater.inflate(R.layout.fragment_freetalk, container, false)
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    //버튼 등의 리스너를 달 때에는 onViewCreated안에 작성
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //전송 버튼 클릭 시 메시지 전송
        sendBtn.setOnClickListener {
            val msg: String = msg_input.text.toString()
            if (msg != "") {
                sendMessage(msg!!, "0")
                msg_input.setText("")
            }

        }

        //엔터 입력 시 메시지 전송
        msg_input.setOnKeyListener(View.OnKeyListener { _, i, _ ->
            when (i) {
                KeyEvent.KEYCODE_ENTER -> {
                    val msg: String = msg_input.text.toString()
                    if (msg != "") {
                        val msg: String = msg_input.text.trim().toString()
                        sendMessage(msg, "0")
                        msg_input.setText("")

                    }
                    return@OnKeyListener true
                }
            }
            false
        })


        chatAdapter = FreeTalkFragmentAdapter(messageList, requireActivity())
        chatRecyclerView.layoutManager = LinearLayoutManager(this.requireActivity())
        chatRecyclerView.setHasFixedSize(true)
        chatRecyclerView.adapter = chatAdapter


        //채팅 uid 가져오기
        userRef.child("rooms").child(freeTalk).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for (i in snapshot.children) {
                        //메시지 정보 가져오기
                        userRef.child("rooms").child(freeTalk).child("message")
                            .child(i.key.toString()).addValueEventListener(object :
                                ValueEventListener {
                                override fun onDataChange(msgsnapshot: DataSnapshot) {
                                    //messageList.add(msgsnapshot.child("msg").value.toString())
                                    var message: ChatModel.Message = ChatModel.Message()
                                    //Log.d("ChatMessage", msgsnapshot.child("msg").value.toString())
                                    try {
                                        message.uid = msgsnapshot.child("uid").getValue(
                                            String::class.java
                                        )!!
                                        message.msg = msgsnapshot.child("msg").getValue(
                                            String::class.java
                                        )!!
                                        message.msgtype =
                                            msgsnapshot.child("msgtype").getValue(
                                                String::class.java
                                            )!!
                                        message.timestamp =
                                            msgsnapshot.child("timestamp").getValue(
                                                Any::class.java
                                            )!!
                                        message.readUsers[msgsnapshot.child("uid")
                                            .getValue(
                                                String::class.java
                                            )!!] = true
                                        messageList.add(message)
                                        chatAdapter!!.notifyDataSetChanged()
                                        if (messageList.size > 0) {
                                            chatRecyclerView.scrollToPosition(messageList.size - 1)
                                        }
                                        //채팅이 많이 쌓였다면 초기화
                                        if (messageList.size > resetCount) {
                                            Log.d("messageList","호출")
                                            userRef.child("rooms").child(freeTalk).child("message").setValue(null)
                                        }
                                    } catch (e: KotlinNullPointerException) {
                                        Log.e("KotlinNull", e.toString())
                                    } catch (e: NullPointerException) {
                                        Log.e("NullPointerException", e.toString())
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                    }
                    chatAdapter!!.notifyDataSetChanged()
                    if (messageList.size > 0) {
                        chatRecyclerView.scrollToPosition(messageList.size - 1)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }

    //사용자 설정 함수나 override함수들(onBackPressed같은거)은 여기에 작성
    private fun sendMessage(msg: String, msgtype: String) {
        sendBtn.isEnabled = false
        //var roomID = ""
        var message: ChatModel.Message = ChatModel.Message()
        message.uid = uid
        message.msg = msg
        message.msgtype = msgtype
        message.timestamp = ServerValue.TIMESTAMP
        message.readUsers[uid] = true

        userRef.child("rooms").child(freeTalk).child("message").push().setValue(message)
            .addOnCompleteListener {
                sendBtn.isEnabled = true

            }


    }
}