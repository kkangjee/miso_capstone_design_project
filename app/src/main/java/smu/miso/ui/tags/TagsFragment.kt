package smu.miso.ui.tags

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.fragment_tags.*
import kotlinx.android.synthetic.main.item_mytaglist.*
import smu.miso.R
import smu.miso.model.MyTagModel
import smu.miso.model.TagModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_mytaglist.view.*
import kotlinx.android.synthetic.main.item_select_taglist.*
import kotlinx.android.synthetic.main.item_select_taglist.view.*

class TagsFragment : Fragment() {
    //전역 변수 설정 공간
    private lateinit var tagsViewModel: TagsViewModel
    private var tagRef =
        FirebaseDatabase.getInstance().reference
    private var taglist = arrayListOf<TagModel>()
    private var mytaglist = arrayListOf<MyTagModel>()
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
                //위 하드코딩된 taglist 항목을 TagModel() 객체로 TagFragment listView에 자식 뷰로 나타내기 위한 작업
                //SelectTagListAdapter로 각 listView와 item을 매핑
                val selectTagListAdapter = SelectTagListAdapter(TagsFragment(), taglist)
                taglistView.adapter = selectTagListAdapter

                //나의 태그리스트 view 등록
                val myTagListAdapter = MyTagListAdapter(TagsFragment(), mytaglist)
                mytaglistView.adapter = myTagListAdapter

                for (list in snapshot.children) {
                    tagName = list.child("tagName").value.toString()
                    tagType = list.child("tagType").value.toString()
                    taglist.add(TagModel(tagName, tagType))
                }
                //그렇게 나타난 taglistView의 각 항목을 누르면 발생되는 리스너 등록
                //태그를 누르면 나의 태그 리스트에 저장할지에 대한 여부를 다이얼로그를 띄워 묻는다.
                taglistView.setOnItemClickListener { parent: AdapterView<*>, view1: View, position: Int, l: Long ->
                    val inflater =
                        context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view = inflater.inflate(R.layout.alert_popup, null)
                    val alertTextView = view.findViewById<TextView>(R.id.alertTextView)

                    /*tagInfo : 사용자가 선택한 태그 이름, tagTypeInfo : 선택한 태그의 종류 */
                    tagNameInfo = selectTagListAdapter.getItem(position).tagName
                    tagTypeInfo = selectTagListAdapter.getItem(position).tagType

                    //선택된 아이템(태그)의 태그 이름을 가져와서 alertDialog 에 안내문구와 함께 display
                    alertTextView.text = tagNameInfo + " 태그를 등록하시겠습니까?"

                    val alertDialog = AlertDialog.Builder(context)
                        .create()

                    //해당 태그를 저장할 것이라면
                    val btnSave = view.findViewById<Button>(R.id.btnSave)
                    btnSave.setOnClickListener {
                        //내가 선택한 태그와 이미 존재하는 나의 태그와 비교
                        //TODO: 이미 존재하는 tag라면 추가하지 않기(반복문 사용하며 계속 나의 태그리스트를 조회하도록 개발해야함)

                        val selectedTagName =
                            selectTagListAdapter.getItem(position).tagName.split('#')

                        mytaglist.add(MyTagModel(tagNameInfo, tagTypeInfo, true))
                        tagRef.child("users").child(user?.uid.toString())
                            .child("mytaglist")
                            .child(selectedTagName[1])
                            .setValue(MyTagModel(tagNameInfo, tagTypeInfo, true))
                        myTagListAdapter.notifyDataSetChanged()

                        if (mytaglistView != null) {
                        tagRef.child("users").child(user?.uid.toString()).child("mytaglist")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (i in snapshot.children) {
                                        if (i.toString() == selectTagListAdapter.getItem(
                                                position
                                            ).tagName
                                        ) {
                                            Toast.makeText(
                                                context,
                                                "이미 존재하는 태그입니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            alertDialog.dismiss()
                                        } else {
                                            mytaglist.add(
                                                MyTagModel(
                                                    tagNameInfo,
                                                    tagTypeInfo,
                                                    true
                                                )
                                            )
                                            tagRef.child("users").child(user?.uid.toString())
                                                .child("mytaglist")
                                                .child(selectedTagName[1])
                                                .setValue(
                                                    MyTagModel(
                                                        tagNameInfo,
                                                        tagTypeInfo,
                                                        true
                                                    )
                                                )
                                            alertDialog.dismiss()
                                            myTagListAdapter.notifyDataSetChanged()
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                    }

                        //그냥 취소할 것이라면
                        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
                        btnCancel.setOnClickListener {
                            Toast.makeText(context, "태그 등록이 취소되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                            alertDialog.dismiss()
                        }
                        alertDialog.setView(view)
                        alertDialog.show()

                        //아래 다이얼로그의 너비폭 상수값은 건들지 말것 (800,450)
                        alertDialog.window?.setLayout(900, 450)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }

}