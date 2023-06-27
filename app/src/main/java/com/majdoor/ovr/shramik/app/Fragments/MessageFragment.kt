package com.majdoor.ovr.shramik.app.Fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.majdoor.ovr.shramik.app.Adapters.MessageAdapter
import com.majdoor.ovr.shramik.app.DataClasses.Constants
import com.majdoor.ovr.shramik.app.DataClasses.MessageData
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.FragmentMessageBinding


class MessageFragment : Fragment() {
    lateinit var binding :FragmentMessageBinding
    lateinit var database: FirebaseDatabase
    lateinit var auth:FirebaseAuth
    lateinit var list:ArrayList<MessageData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.message_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayShowTitleEnabled(false)

        initialize()



        return binding.root
    }
    private fun initialize() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        getData()
    }

    private fun setUpAdapter() {
        binding.noDrafts.visibility = View.GONE
        val adapter = MessageAdapter(requireContext(), list)
        adapter.notifyDataSetChanged()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun getData() {
        binding.progressBar.visibility = View.VISIBLE
        val uid = auth.currentUser?.uid.toString()
        list = ArrayList()
        database.reference.child(Constants.CHATS).child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                binding.progressBar.visibility = View.GONE
                for(snap in snapshot.children){
                    val s = snap.key.toString()
                    val m = s.indexOf("--1--")
                    val n = s.indexOf("--2--")
                    val senderId =  s.substring(0, m)
                    val receiverId = s.substring(n+5)
                    val title = s.substring(m+5 , n)
//                    Toast.makeText(context, senderId+" "+ receiverId, Toast.LENGTH_SHORT).show()
                    list.add(MessageData(title, senderId, receiverId, s))
                }
                if(list.isNotEmpty()){
                    setUpAdapter()
                }
                else{
                    binding.noDrafts.visibility = View.VISIBLE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
}
