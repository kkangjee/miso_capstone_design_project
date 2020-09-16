package smu.miso.signup

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_select_taglist.view.*
import smu.miso.R
import smu.miso.model.MyTagModel
import smu.miso.model.TagModel

class SelectTagActivityAdapter(
    val context: SelectTagActivity,
    private val taglist: ArrayList<TagModel?>
) :
    RecyclerView.Adapter<SelectTagActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectTagActivityAdapter.ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_select_taglist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taglist.size

    }

    override fun onBindViewHolder(holder: SelectTagActivityAdapter.ViewHolder, position: Int) {
        val item = taglist[position]
        holder.itemView.setOnClickListener {
            itemClickListener.OnClick(it, position)
        }
        holder.apply {
            bind(View.OnClickListener {
                val inflater =
                    itemView.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.alert_popup, null)
                val alertTextView = view.findViewById<TextView>(R.id.alertTextView)

                /*tagInfo : 사용자가 선택한 태그 이름, tagTypeInfo : 선택한 태그의 종류 */
                val tagNameInfo = item?.tagName
                //val tagTypeinfo = item.tagType
                //선택된 아이템(태그)의 태그 이름을 가져와서 alertDialog 에 안내문구와 함께 display
                alertTextView.text = "$tagNameInfo 태그를 등록하시겠습니까?"

                val alertDialog = AlertDialog.Builder(itemView.context)
                    .create()
                alertDialog.setView(view)
                alertDialog.show()

                //아래 다이얼로그의 너비폭 상수값은 건들지 말것
                alertDialog.window?.setLayout(1100, 550)

                val btnSave = view.findViewById<Button>(R.id.btnSave)
                btnSave.setOnClickListener {
                    if (item != null) {
                        taglist.add(TagModel(item.tagName, item.tagType))
                    }


                    Toast.makeText(itemView.context, "태그 등록되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                    alertDialog.dismiss()
                }

                val btnCancel = view.findViewById<Button>(R.id.btnCancel)
                btnCancel.setOnClickListener {
                    Toast.makeText(itemView.context, "태그 등록이 취소되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                    alertDialog.dismiss()
                }


            }, taglist[position], context)
            itemView.tag = item
        }

    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bind(listener: View.OnClickListener, tag: TagModel?, context: Context) {
            itemView.selectTagNametv.text = tag?.tagName
            itemView.selectTagType.text = tag?.tagType
            itemView.setOnClickListener(listener)
        }
    }

    interface OnItemClickListener {
        fun OnClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener
    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}