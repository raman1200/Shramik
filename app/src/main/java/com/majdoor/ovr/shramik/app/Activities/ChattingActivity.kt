package com.majdoor.ovr.shramik.app.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.majdoor.ovr.shramik.app.Adapters.ChatAdapter
import com.majdoor.ovr.shramik.app.Adapters.MessageAdapter
import com.majdoor.ovr.shramik.app.DataClasses.ChatData
import com.majdoor.ovr.shramik.app.DataClasses.Constants.Companion.CHATS
import com.majdoor.ovr.shramik.app.databinding.ActivityChattingBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChattingActivity : AppCompatActivity() {
    lateinit var binding:ActivityChattingBinding
    lateinit var auth:FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var senderId:String
    lateinit var receiverId:String
    lateinit var room:String
    lateinit var list:ArrayList<ChatData>
    lateinit var uid : String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)



        getDataFromIntent()
        initialize()
        clickListeners()
        getDataFromFirebase()
    }

    private fun getDataFromIntent() {
        val title = intent.getStringExtra("TITLE")
        senderId = intent.getStringExtra("SENDER_ID").toString()
        receiverId = intent.getStringExtra("RECEIVER_ID").toString()
        room = intent.getStringExtra("ROOM").toString()
        binding.title.text = title
    }

    private fun clickListeners(){
        binding.send.setOnClickListener {
            getMessage()
        }
    }
    private fun getMessage(){
        val message = binding.message.text.toString()
        if(message.isEmpty()){
            binding.message.error = "Please Type a message"
            return
        }
        val d = Date()
        val sdf1 = SimpleDateFormat("dd/mm/yyyy")
        val sdf2 = SimpleDateFormat("hh:mm a")
        val date = sdf1.format(d)!!
        val time = sdf2.format(d)!!
        val chat = ChatData(message = message, uid = uid, date = date, time = time)
        database.reference.child(CHATS).child(senderId).child(room).push().setValue(chat)
        database.reference.child(CHATS).child(receiverId).child(room).push().setValue(chat)
        binding.message.text.clear()

    }

    private fun getDataFromFirebase() {
        list = ArrayList()
        database.reference.child(CHATS).child(uid).child(room).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for(snap in snapshot.children){
                    val chat = snap.getValue(ChatData::class.java)!!
                    list.add(chat)
                }
                if(list.isNotEmpty()){
                    setUpAdapter()
                }
                else{
                    binding.noDrafts.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChattingActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun setUpAdapter() {

        binding.noDrafts.visibility = View.GONE
        val adapter = ChatAdapter(this, list)
        adapter.notifyDataSetChanged()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun initialize() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        uid = auth.currentUser!!.uid
    }


}