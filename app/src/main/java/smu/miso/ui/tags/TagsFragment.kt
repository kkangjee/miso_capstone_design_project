package smu.miso.ui.tags

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_tags.*
import smu.miso.R
import smu.miso.model.MyTagModel
import smu.miso.model.TagModel

class TagsFragment : Fragment() {
    //전역 변수 설정 공간
    private lateinit var tagsViewModel: TagsViewModel
    private var tagRef =
        FirebaseDatabase.getInstance().reference
    private var taglist = arrayListOf<TagModel>()

    private var tagNameInfo: String = ""
    private var tagTypeInfo: String = ""

    var tagName = ""
    var tagType = ""
    var requestCount = 0

    private var auth = FirebaseAuth.getInstance()
    private var uid = auth.uid
    private var user = auth.currentUser

    //기본 구성 함수(Don't touch)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {//여기까지
        tagsViewModel =
            ViewModelProviders.of(this).get(TagsViewModel::class.java)
        //어떤 화면을 container(프레그먼트가 보여질 화면)에 보여질 것인지 설정
        val root = inflater.inflate(R.layout.fragment_tags, container, false)
        return root
    }

    //버튼 등의 리스너를 달 때에는 onViewCreated안에 작성
    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tagRef.child("tagList").child("addedTagList").addListenerForSingleValueEvent(object :
            ValueEventListener {
            //Firebase에서 등록된 taglist 가져오기
            override fun onDataChange(snapshot: DataSnapshot) {
                //위 하드코딩된 taglist 항목을 TagModel() 객체로 TagFragment RecyclerView 자식 뷰로 나타내기 위한 작업
                //SelectTagListAdapter로 각 RecyclerView item을 매핑
                val selectTagListAdapter = SelectTagListAdapter(TagsFragment(), taglist)
                tagRecyclerView?.adapter = selectTagListAdapter
                val taglm = LinearLayoutManager(context)
                tagRecyclerView?.layoutManager = taglm
                tagRecyclerView?.setHasFixedSize(true)

                for (list in snapshot.children) {
                    tagName = list.child("tagName").value.toString()
                    tagType = list.child("tagType").value.toString()
                    taglist.add(TagModel(tagName, tagType))
                }

                //TODO: 아래로 당겨 스와이프 시 서버에서 새로 정보를 업데이트
                val mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout) as SwipeRefreshLayout
                mSwipeRefreshLayout.setOnRefreshListener {
                    taglist.clear()
                    tagRef.child("tagList").child("addedTagList")
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val selectRefreshTagListAdapter =
                                    SelectTagListAdapter(TagsFragment(), taglist)
                                tagRecyclerView?.adapter = selectRefreshTagListAdapter
                                val tagRefeshLm = LinearLayoutManager(context)
                                tagRecyclerView?.layoutManager = tagRefeshLm
                                tagRecyclerView?.setHasFixedSize(true)

                                for (list in snapshot.children) {
                                    tagName = list.child("tagName").value.toString()
                                    tagType = list.child("tagType").value.toString()
                                    taglist.add(TagModel(tagName, tagType))
                                }
                                mSwipeRefreshLayout.isRefreshing = false
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}

//                    //해당 태그를 저장할 것이라면
//                    val btnSave = view.findViewById<Button>(R.id.btnSave)
//                    btnSave.setOnClickListener {
//                        //내가 선택한 태그와 이미 존재하는 나의 태그와 비교
//                        //TODO: 이미 존재하는 tag라면 추가하지 않기(반복문 사용하며 계속 나의 태그리스트를 조회하도록 개발해야함)
//
//                        val selectedTagName =
//                            selectTagListAdapter.getItem(position).tagName.split('#')
//
//                        for (i in mytaglist.myTagName) {
//                            if (i.toString() != selectTagListAdapter.getItem(position).tagName) {
//                                mytaglist.add(MyTagModel(tagNameInfo, tagTypeInfo, true))
//                                tagRef.child("users").child(user?.uid.toString())
//                                    .child("mytaglist")
//                                    .child(selectedTagName[1])
//                                    .setValue(MyTagModel(tagNameInfo, tagTypeInfo, true))
//                                myTagListAdapter.notifyDataSetChanged()
//                                alertDialog.dismiss()
//                            } else {
//                                Toast.makeText(context, "이미 존재하는 태그입니다.", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//
//                    //그냥 취소할 것이라면
//                    val btnCancel = view.findViewById<Button>(R.id.btnCancel)
//                    btnCancel.setOnClickListener {
//                        Toast.makeText(context, "태그 등록이 취소되었습니다.", Toast.LENGTH_SHORT)
//                            .show()
//                        alertDialog.dismiss()
//                    }
//
//
//                    alertDialog.setView(view)
//                    alertDialog.show()
//
//                    //아래 다이얼로그의 너비폭 상수값은 건들지 말것
//                    alertDialog.window?.setLayout(1000, 550)
//
//                }
//            }


