package smu.miso.Chat


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_chatmsg_left.view.*
import smu.miso.R

class ChatFragmentAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<ChatFragmentAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chatmsg_left, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items[position]
        val listener = View.OnClickListener {
            Toast.makeText(it.context,"꾹 눌러서 삭제하세요.(미구현)",Toast.LENGTH_SHORT).show()
        }

        holder.apply {
            bindItems(listener,item,context)
            itemView.tag = item

        }
    }
    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(listener: View.OnClickListener, chatMsg : String, context:Context){
            itemView.msg_item.text = chatMsg

            itemView.setOnClickListener(listener)
        }

    }



}