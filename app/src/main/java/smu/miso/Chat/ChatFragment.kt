package smu.miso.Chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*
import smu.miso.R
import smu.miso.model.ChatModel
import smu.miso.ui.randomchat.RandomChatViewModel
import java.text.SimpleDateFormat

class ChatFragment : Fragment() {
    //전역 변수 설정 공간
    var roomID = ""
    var randomRoomId = ""
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.reference

    var messageList: ArrayList<String> = arrayListOf()
    var chatAdapter: ChatFragmentAdapter? = null

    @SuppressLint("SimpleDateFormat")
    private val dateFormatDay = SimpleDateFormat("yyyy-MM-dd")

    @SuppressLint("SimpleDateFormat")
    private val dateFormatHour = SimpleDateFormat("aa hh:mm")

    //var bundle:Bundle? = arguments

    //기본 구성 함수(Don't touch)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {//여기까지
        //어떤 화면을 container(프레그먼트가 보여질 화면)에 보여질 것인지 설정
        val root = inflater.inflate(R.layout.fragment_chat, container, false)

        return root
    }

    //버튼 등의 리스너를 달 때에는 onViewCreated안에 작성
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendBtn.setOnClickListener {
            var msg: String = msg_input.text.toString()
            sendMessage(msg, "0")
            msg_input.setText("")
        }

        chatAdapter = ChatFragmentAdapter(messageList, requireActivity())
        chatRecyclerView.layoutManager = LinearLayoutManager(this.requireActivity())
        chatRecyclerView.setHasFixedSize(true)
        chatRecyclerView.adapter = chatAdapter


        userRef.child("users").child(uid).child("randomRoomId").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(roomIDsnapShot: DataSnapshot) {
                    randomRoomId = roomIDsnapShot.value.toString()
                    Log.d("randomRoomId 가져오기", randomRoomId)


                    //채팅 uid 가져오기
                    userRef.child("rooms").child(randomRoomId).child("message")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                messageList.clear()
                                for (i in snapshot.children) {
                                    userRef.child("rooms").child(randomRoomId).child("message")
                                        .child(i.key.toString()).child("msg").addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(msgsnapshot: DataSnapshot) {
                                                messageList.add(msgsnapshot.value.toString())
                                                chatAdapter!!.notifyDataSetChanged()
                                            }
                                            override fun onCancelled(error: DatabaseError) {
                                            }

                                        })



                                }
                                chatAdapter!!.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })


                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("randomRoomId 가져오기", "실패")
                }
            })


        //messageList.add("test")
        //서버에서 채팅 가져와서 리스트 만들어 여기에 연결하기

    }

    //사용자 설정 함수나 override함수들(onBackPressed같은거)은 여기에 작성
    private fun sendMessage(msg: String, msgtype: String) {
        sendBtn.isEnabled = false

        var message: ChatModel.Message = ChatModel.Message()
        message.uid = uid
        message.msg = msg
        message.msgtype = msgtype
        message.timestamp = ServerValue.TIMESTAMP
        message.readUsers[uid] = true

        //save message
        val activity: ChatActivity = activity as ChatActivity
        roomID = activity.getMyData()

        //recyclerView?.layoutManager = LinearLayoutManager(context)
        //  recyclerView?.adapter = RecyclerViewAdapter()

        //Toast.makeText(getActivity(), roomID, Toast.LENGTH_SHORT).show()
        userRef.child("rooms").child(roomID).child("message").push().setValue(message)
            .addOnCompleteListener {
                sendBtn.isEnabled = true

            }
        //save last message
        userRef.child("rooms").child(roomID).child("last_message").push().setValue(message)


        // inc unread message count
//        userRef.child("rooms").child(roomID).child("users")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    for (item in dataSnapshot.children) {
//                        val uid = item.key
//                        if (uid != item.key) {
//                            if (uid != null) {
//                                userRef.child("rooms").child(roomID).child("unread").child(uid)
//                                    .addListenerForSingleValueEvent(object : ValueEventListener {
//                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                            var cnt = dataSnapshot.getValue(Int::class.java)
//                                            if (cnt == null) cnt = 0
//                                            if (uid != null) {
//                                                userRef.child("rooms").child(roomID).child("unread").child(uid)
//                                                    .setValue(cnt + 1)
//                                            }
//                                        }
//
//                                        override fun onCancelled(databaseError: DatabaseError) {}
//                                    })
//                            }
//                        }
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {}
//            })
    }
}