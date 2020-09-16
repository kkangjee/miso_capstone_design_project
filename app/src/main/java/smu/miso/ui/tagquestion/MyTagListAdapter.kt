package smu.miso.ui.tagquestion


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smu.miso.R
import smu.miso.model.MyTagModel
import smu.miso.ui.tagquestion.TagQuestionFragment

class MyTagListAdapter(val context: TagQuestionFragment, private val mytaglist: ArrayList<MyTagModel>) :
    RecyclerView.Adapter<MyTagListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_mytaglist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mytaglist.size
    }

    override fun onBindViewHolder(holder: MyTagListAdapter.ViewHolder, position: Int) {
        holder.bind(mytaglist[position], context)
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        //위에서 생성된 view를 item_my_taglist 파일의 각 view와 연결하는 작업
        val myTagName = itemView?.findViewById<TextView>(R.id.tagNametv)
        val myTagType = itemView?.findViewById<TextView>(R.id.tagType)
        val alarmOnOffBtn = itemView?.findViewById<Button>(R.id.onoffalramBtn)
        val requestQuestionBtn = itemView?.findViewById<Button>(R.id.questionBtn)
        val deleteMyTagBtn = itemView?.findViewById<Button>(R.id.deleteMyTagBtn)

        fun bind(mytag: MyTagModel, context: TagQuestionFragment) {
            myTagName?.text = mytag.myTagName
            myTagType?.text = mytag.myTagType
            //alarmOnOffBtn.text = mytag.alarmOnOff.toString()
            requestQuestionBtn?.text = "질문하기"
            deleteMyTagBtn?.text = "삭제하기"
        }
    }
}