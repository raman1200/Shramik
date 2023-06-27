package com.majdoor.ovr.shramik.app.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.majdoor.ovr.shramik.app.Activities.ChattingActivity
import com.majdoor.ovr.shramik.app.DataClasses.MessageData
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.MessageListViewBinding

class MessageAdapter(val context: Context, val list: ArrayList<MessageData>): Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View): ViewHolder(itemView){
        val binding = MessageListViewBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent , false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val data = list[position]
        holder.binding.name.text = data.title
        holder.binding.itemView.setOnClickListener {
            val intent = Intent(context, ChattingActivity::class.java)
            intent.putExtra("TITLE", data.title)
            intent.putExtra("SENDER_ID", data.senderId)
            intent.putExtra("RECEIVER_ID", data.receiverId)
            intent.putExtra("ROOM", data.room)
            context.startActivity(intent)
        }
    }
}