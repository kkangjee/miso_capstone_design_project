package smu.miso.ui.freetalk


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_chatmsg_left.view.*
import smu.miso.R
import smu.miso.model.ChatModel
import java.text.SimpleDateFormat
import java.util.*

class FreeTalkFragmentAdapter(val items: ArrayList<ChatModel.Message>, val context: Context) :
    RecyclerView.Adapter<FreeTalkFragmentAdapter.ViewHolder>() {
    private val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var dateFormatDay = SimpleDateFormat("yyyy-MM-dd")
    private var dateFormatHour = SimpleDateFormat("aa hh:mm")
    var beforeDay: String? = null
    var beforeViewHolder: FreeTalkFragmentAdapter.ViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items[position]
        val listener = View.OnLongClickListener {
            //Toast.makeText(it.context, "꾹 눌러서 삭제하세요.(미구현)", Toast.LENGTH_SHORT).show()
            return@OnLongClickListener true
        }

        var day: String = dateFormatDay.format(Date(item.timestamp as Long))
        var timestamp: String = dateFormatHour.format(Date(item.timestamp as Long))

        dateFormatDay.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        dateFormatHour.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        //오늘날짜 검사사
        holder.itemView.timestamp.text = timestamp

        holder.itemView.divider.visibility = View.INVISIBLE
        holder.itemView.divider.layoutParams.height = 0

        if (position == 0) {
            holder.itemView.divider_date.text = day
            holder.itemView.divider.visibility = View.VISIBLE
            holder.itemView.divider.layoutParams.height = 60
        }

        if (day != beforeDay && beforeDay != null) {
            beforeViewHolder?.itemView?.divider_date?.text = beforeDay
            beforeViewHolder?.itemView?.divider?.visibility = View.VISIBLE
            beforeViewHolder?.itemView?.divider?.layoutParams?.height = 60
        }
        beforeViewHolder = holder
        beforeDay = day


        holder.apply {
            bindItems(listener, item, context)
            itemView.tag = item
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message: ChatModel.Message = items.get(position)
        return if (myUid == message.uid) {
            when (message.msgtype) {
                "1" -> R.layout.item_chatimage_right
                "2" -> R.layout.item_chatfile_right
                else -> R.layout.item_chatmsg_right
            }
        } else {
            when (message.msgtype) {
                "1" -> R.layout.item_chatimage_left
                "2" -> R.layout.item_chatfile_left
                else -> R.layout.item_chatmsg_left
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
            listener: View.OnLongClickListener,
            message: ChatModel.Message,
            context: Context
        ) {
            itemView.msg_item.text = message.msg
            itemView.divider_date.text = dateFormatDay.format(Date(message.timestamp as Long))
            itemView.timestamp.text = dateFormatHour.format(Date(message.timestamp as Long))

            itemView.setOnLongClickListener(listener)
        }

    }


}