package smu.miso.ui.randomchat

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_randomchat.*
import smu.miso.Chat.ChatActivity
import smu.miso.Chat.ChatFragment
import smu.miso.HomeActivity
import smu.miso.R
import smu.miso.SplashActivity

class RandomChatFragment : Fragment() {
    //전역 변수 설정 공간
    private lateinit var randomchatViewModel: RandomChatViewModel
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.reference
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    var selectedUsers: HashMap<String, String> = HashMap<String, String>() //사용자 2명이 들어갈 공간
    var roomID: String? = null

    //기본 구성 함수(Don't touch)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {//여기까지
        randomchatViewModel =
            ViewModelProviders.of(this).get(RandomChatViewModel::class.java)
        //어떤 화면을 container(프레그먼트가 보여질 화면)에 보여질 것인지 설정
        val root = inflater.inflate(R.layout.fragment_randomchat, container, false)
        val textView: TextView = root.findViewById(R.id.text_randomchat)
        randomchatViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    //무작위 사용자 설정
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //과끼리 랜덤채팅
        button.setOnClickListener {
            setDepartmentUsers()
        }
        //전체과 랜덤채팅
        button2.setOnClickListener {
            setAllDepartmentUsers()
        }


    }

    //이하 메소드 부분
    //프래그먼트 설정
    //매칭 버튼을 눌렀을 때 채팅방 입장 (대기 중인 사용자도 채팅방 입장)
    //TODO : 채팅방 입장 전에 Splash(대기화면) 출력 후 채팅 방 입장 되어야 함 (update 200829)
    fun goChattingActivity() {
        requireActivity().run {
            val nextIntent = Intent(this, ChatActivity::class.java)
            startActivity(nextIntent)
            //finish() // If activity no more needed in back stack
        }
    }

    fun goSplashActivity() {
        requireActivity().run {
            val nextIntent = Intent(this, SplashActivity::class.java)
            nextIntent.putExtra("splash_room_id", roomID)
            startActivity(nextIntent)
            //finish() // If activity no more needed in back stack
        }
    }

    //학과별 설정
    private fun setDepartmentUsers() {
        var department: String = ""
        var deptKey: String = ""
        userRef.child("users").child(uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                department = snapshot.child("department").value.toString()
                Log.d("과 끼리 학과 가져오기", department)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("학과 가져오기", "실패")
            }
        })


        userRef.child("deptMap").child(department).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Log.d("학과 가져오기", department)
                for (singleSnapshot in snapshot.children) {
                    Log.d("학과 스냅샷", singleSnapshot.key.toString())
                    //내 학과와 같은 상황일 때 반복문 종료
                    if (singleSnapshot.key.toString() == department) {
                        Log.d("대기 중인 사용자", deptKey)
                        deptKey = snapshot.child(department).value.toString()
                        break
                    }
                }
                if (deptKey == "") {
                    //아무도 대기 상태가 아닐 경우 대기상태로 변경,방을 만들고 value값에 roodID를 넣어준다.
                    Log.d("RandomChatFragment", "deptKey 진입")
                    //selectedUsers[uid] = department
                    roomID = userRef.child("rooms").push().key
                    userRef.child("users").child(uid).child("randomRoomId").setValue(roomID)
                    userRef.child("rooms/$roomID").child("users")
                        .child(uid).setValue(department).addOnSuccessListener {
                            goSplashActivity()
                        }
                    userRef.child("deptMap").child(department).setValue(roomID)

                } else {
                    //이미 존재하는 roodID 값을 타고 방으로 들어간다. 그리고 value값을 비운다.
                    Log.d("매칭 대기 중인 사용자 여부", "확인")
                    userRef.child("users").child(uid).child("randomRoomId").setValue(deptKey)
                    userRef.child("rooms/$deptKey").child("users")
                        .child(uid).setValue(department).addOnSuccessListener {
                            goChattingActivity()
                        }
                    userRef.child("deptMap").child(department).setValue("")


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    //전체과 설정
    private fun setAllDepartmentUsers() {
        var deptKey = ""
        val allDept = "전체학과"
        userRef.child("deptMap").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("학과 가져오기", allDept)
                for (singleSnapshot in snapshot.children) {
                    Log.d("학과 스냅샷", singleSnapshot.key.toString())
                    //전체과를 찾은 상황일 때 반복문 종료
                    if (singleSnapshot.key.toString() == allDept) {
                        Log.d("대기 중인 사용자", deptKey)
                        deptKey = snapshot.child(allDept).value.toString()
                        break
                    }
                }

                if (deptKey == "") {
                    //아무도 대기 상태가 아닐 경우 대기상태로 변경,방을 만들고 value값에 roodID를 넣어준다.
                    Log.d("RandomChatFragment", "deptKey 진입")
                    //selectedUsers[uid] = department
                    roomID = userRef.child("rooms").push().key
                    Log.d("RandomChatFragment", roomID.toString())
                    userRef.child("users").child(uid).child("randomRoomId").setValue(roomID)
                    userRef.child("rooms/$roomID").child("users")
                        .child(uid).setValue(allDept).addOnSuccessListener {
                            goSplashActivity()
                        }
                    userRef.child("deptMap").child(allDept).setValue(roomID)

                } else {
                    //이미 존재하는 roodID 값을 타고 방으로 들어간다. 그리고 value값을 비운다.
                    Log.d("매칭 대기 중인 사용자 여부", "확인")
                    userRef.child("users").child(uid).child("randomRoomId").setValue(deptKey)
                    userRef.child("rooms/$deptKey").child("users")
                        .child(uid).setValue(allDept).addOnSuccessListener {
                            goChattingActivity()
                        }
                    userRef.child("deptMap").child(allDept).setValue("")


                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    //사용자 설정 함수나 override함수들(onBackPressed같은거)은 여기에 작성
}