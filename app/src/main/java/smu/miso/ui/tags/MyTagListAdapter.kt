package smu.miso.ui.tags

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import smu.miso.R
import smu.miso.model.MyTagModel

class MyTagListAdapter(val context: TagsFragment, private val mytaglist: ArrayList<MyTagModel>) :
    BaseAdapter() {
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_mytaglist, null)

        //위에서 생성된 view를 item_my_taglist 파일의 각 view와 연결하는 작업
        val myTagName = view.findViewById<TextView>(R.id.tagNametv)
        val myTagType = view.findViewById<TextView>(R.id.tagType)
        val alarmOnOffBtn = view.findViewById<Button>(R.id.onoffalramBtn)
        val requestQuestionBtn = view.findViewById<Button>(R.id.questionBtn)
        val deleteMyTagBtn = view.findViewById<Button>(R.id.deleteMyTagBtn)

        val mytag = mytaglist[position]
        myTagName.text = mytag.myTagName
        myTagType.text = mytag.myTagType
        //alarmOnOffBtn.text = mytag.alarmOnOff.toString()
        requestQuestionBtn.text = "질문하기"
        deleteMyTagBtn.text = "삭제하기"

        return view
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): MyTagModel {
        return mytaglist.get(position)
    }

    override fun getCount(): Int {
        return mytaglist.size
    }
}