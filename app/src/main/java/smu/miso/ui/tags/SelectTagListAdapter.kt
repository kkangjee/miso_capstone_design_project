package smu.miso.ui.tags

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import smu.miso.R
import smu.miso.model.TagModel


class SelectTagListAdapter(val context: TagsFragment, private val taglist: ArrayList<TagModel>) : BaseAdapter() {
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.item_select_taglist,null)

        // 위에서 생성된 view를 item_select_taglist 파일의 각 view와 연결하는 작업
        val tagName = view.findViewById<TextView>(R.id.selectTagNametv)
        val tagType = view.findViewById<TextView>(R.id.selectTagType)
        val requestCount = view.findViewById<TextView>(R.id.request_count)

        //ArrayList<TagModel>의 데이터를 TextView에 담는다.
        val tag = taglist[position]
        tagName.text = tag.tagName
        tagType.text = tag.tagType
        //requestCount.text = tag.requestCount.toString()

        return view
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    //getItem 기능을 수행할 때 반환해야 하는 것은 해당 모델에서의 정보 위치
    override fun getItem(position: Int): TagModel {
        return taglist.get(position)
    }

    override fun getCount(): Int {
        return taglist.size
    }
}