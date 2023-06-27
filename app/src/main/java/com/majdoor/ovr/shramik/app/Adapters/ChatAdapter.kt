package com.majdoor.ovr.shramik.app.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.majdoor.ovr.shramik.app.DataClasses.ChatData
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.ReceiverChatBinding
import com.majdoor.ovr.shramik.app.databinding.SenderChatBinding
import java.text.SimpleDateFormat

class ChatAdapter(val context: Context, val messageList:ArrayList<ChatData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SENDER_VIEW_TYPE = 1
    val RECEIVER_VIEW_TYPE = 2


    override fun getItemViewType(position: Int): Int {
        return if(messageList[position].uid == FirebaseAuth.getInstance().currentUser!!.uid){
            SENDER_VIEW_TYPE
        }
        else{
            RECEIVER_VIEW_TYPE
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = if(viewType==SENDER_VIEW_TYPE){
            SenderViewHolder(LayoutInflater.from(context).inflate(R.layout.sender_chat, parent, false))
        }
        else{
            ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_chat, parent, false))
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = messageList[position]

        if(holder::class == SenderViewHolder::class){
            (holder as SenderViewHolder).senderBinding.message.text = chat.message
            (holder as SenderViewHolder).senderBinding.time.text = chat.time

        }
        else{
            (holder as ReceiverViewHolder).receiverBinding.message.text = chat.message
            (holder as ReceiverViewHolder).receiverBinding.time.text = chat.time

        }

    }
    class SenderViewHolder(itemView: View):ViewHolder(itemView) {
        val senderBinding:SenderChatBinding = SenderChatBinding.bind(itemView)
    }
    class ReceiverViewHolder(itemView: View):ViewHolder(itemView) {
        val receiverBinding: ReceiverChatBinding = ReceiverChatBinding.bind(itemView)
    }
}