package smu.miso.signup

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_select_tag.*
import kotlinx.android.synthetic.main.alert_popup.*
import kotlinx.android.synthetic.main.fragment_tags.*
import smu.miso.HomeActivity
import smu.miso.R
import smu.miso.model.MyTagModel
import smu.miso.model.TagModel
import smu.miso.model.UserModel

class SelectTagActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val userRef = FirebaseDatabase.getInstance().reference
    private var tagRef =
        FirebaseDatabase.getInstance().reference
    private var taglist: ArrayList<TagModel?> = ArrayList()
    private var mytaglist: ArrayList<MyTagModel?> = ArrayList()
    var tagName = ""
    var tagType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_tag)
        supportActionBar?.hide()

        val studentID = intent.getStringExtra("studentID")
        val department: String? = intent.getStringExtra("department")
        val uid: String = user?.uid.toString()

        tagRef.child("tagList").child("addedTagList").addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val mSelectTagActivityAdapter =
                    SelectTagActivityAdapter(this@SelectTagActivity, taglist)
                selectTagRecyclerView?.adapter = mSelectTagActivityAdapter
                val selectTaglm = LinearLayoutManager(this@SelectTagActivity)
                selectTagRecyclerView?.layoutManager = selectTaglm
                selectTagRecyclerView?.setHasFixedSize(true)

                for (list in snapshot.children) {
                    tagName = list.child("tagName").value.toString()
                    tagType = list.child("tagType").value.toString()
                    taglist.add(TagModel(tagName, tagType))
                }

                mSelectTagActivityAdapter.setItemClickListener(object :
                    SelectTagActivityAdapter.OnItemClickListener {
                    override fun OnClick(v: View, position: Int) {
                        val item = taglist[position]
                        if (item != null) {
                            mytaglist.add(MyTagModel(item.tagName,item.tagType,true))
                            Log.d("태그 저장", mytaglist.toString())
                        }
                    }
                })

                //TODO: 아래로 당겨 스와이프 시 서버에서 새로 정보를 업데이트
                swipe_layout2.setOnRefreshListener {
                    taglist.clear()
                    tagRef.child("tagList").child("addedTagList")
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val selectRefreshTagListAdapter =
                                    SelectTagActivityAdapter(this@SelectTagActivity, taglist)
                                tagRecyclerView?.adapter = selectRefreshTagListAdapter
                                val selectTagRefeshLm = LinearLayoutManager(this@SelectTagActivity)
                                tagRecyclerView?.layoutManager = selectTagRefeshLm
                                tagRecyclerView?.setHasFixedSize(true)

                                for (list in snapshot.children) {
                                    tagName = list.child("tagName").value.toString()
                                    tagType = list.child("tagType").value.toString()
                                    taglist.add(TagModel(tagName, tagType))
                                }
                                swipe_layout2.isRefreshing = false
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        //태그 선택단계가 끝났으면 home으로 이동
        bt_gotoHome.setOnClickListener {
            user?.reload()
                ?.addOnSuccessListener {
                    Log.d("학교 이메일 인증", "완료")
                }
            when (verifiedEmail()) {
                true ->
                    //이메일 인증 까지 완료 되었으면 DB에 USER 객체 생성(emailVerfied 값을 true 로 DB에 전달)
                    //기본 태그는 학과명으로 태그 설정
                    userRef.child("users").child(uid)
                        .setValue(UserModel(uid, studentID.toString(), true, department, mytaglist))
                        .addOnSuccessListener {
                            Log.d("USER_INSERTED!!", "데이터베이스에 User 객체 생성 완료")
                            val intent = Intent(this@SelectTagActivity, HomeActivity::class.java)
                            Toast.makeText(
                                this@SelectTagActivity,
                                "로그인이 완료되었습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(intent)
                            finish()

                        }.addOnFailureListener {
                            Log.d("USER_INSERTED_FAIL!!", "데이터베이스에 User 객체 생성 실패")
                        }
                false -> {
                    Toast.makeText(
                        this@SelectTagActivity,
                        "사용자 정보를 업데이트 중 입니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }

    }
}