package smu.miso.ui.randomchat
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import smu.miso.R
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
    fun setFragmentSetting() {
        requireActivity().run {
            startActivity(Intent(this, ChatActivity::class.java))
            finish() // If activity no more needed in back stack
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
                Log.e("학과 가져오기 실패", "두둥탁")
            }
        })
        userRef.child("deptMap").child(department).addListenerForSingleValueEvent(object :
                ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Log.d("학과 가져오기", department)
                for (singleSnapshot in snapshot.children) {
                    Log.d("스냅샷", singleSnapshot.key.toString())
                    //내 학과와 같은 상황일 때 반복문 종료
                    if (singleSnapshot.key.toString() == department) {
                        Log.d("기존 uid 가져오기", deptKey)
                        deptKey = snapshot.child(department).value.toString()
                        break
                    }
                }
                if (deptKey == "") {
                    //TODO: 아무도 대기 상태가 아닐 경우 대기상태로 변경하고 value값을 넣어준다.
                    userRef.child("deptMap").child(department)
                            .setValue(uid)
                } else {
                    //TODO: 이미 uid 값이 있는 것을 확인하여 손 잡고 채팅방을 나간다. 그리고 value값을 비운다.
                    Log.d("deptKey이미 존재", "확인")
                    var uid1 = deptKey
                    var uid2 = uid
                    selectedUsers.put(uid1, department)
                    selectedUsers.put(uid2, department)
                    // roomid생성
                    // roomid생성
                    roomID = FirebaseDatabase.getInstance().reference.child("rooms").push().key
                    FirebaseDatabase.getInstance().reference.child("rooms/$roomID").child("users")
                            .setValue(selectedUsers).addOnSuccessListener {

                                setFragmentSetting()

                            }
                    userRef.child("deptMap").child(department)
                            .setValue("")
                }
                //setFragmentSetting()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    //전체과 설정
    private fun setAllDepartmentUsers() {
        var deptKey = ""
        val allDept = "AllDept"
        userRef.child("deptMap").addListenerForSingleValueEvent(object :
                ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("학과 가져오기", allDept)
                for (singleSnapshot in snapshot.children) {
                    Log.d("스냅샷", singleSnapshot.key.toString())
                    //전체과를 찾은 상황일 때 반복문 종료
                    if (singleSnapshot.key.toString() == allDept) {
                        Log.d("기존 uid 가져오기", deptKey)
                        deptKey = snapshot.child(allDept).value.toString()
                        break
                    }
                }
                if (deptKey == "") {
                    //TODO: 아무도 대기 상태가 아닐 경우 대기상태로 변경하고 value값을 넣어준다.
                    userRef.child("deptMap").child(allDept)
                            .setValue(uid)
                } else {
                    //TODO: 이미 uid 값이 있는 것을 확인하여 손 잡고 채팅방을 나간다. 그리고 value값을 비운다.
                    Log.d("deptKey이미 존재", "확인")
                    var uid1 = deptKey
                    var uid2 = uid
                    selectedUsers.put(uid1, allDept)
                    selectedUsers.put(uid2, allDept)

                    roomID = FirebaseDatabase.getInstance().reference.child("rooms").push().key
                    FirebaseDatabase.getInstance().reference.child("rooms/$roomID").child("users")
                            .setValue(selectedUsers).addOnSuccessListener {
                                setFragmentSetting()
                            }

                    userRef.child("deptMap").child(allDept)
                            .setValue("")

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    //사용자 설정 함수나 override함수들(onBackPressed같은거)은 여기에 작성
}