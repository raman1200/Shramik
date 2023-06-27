package com.majdoor.ovr.shramik.app.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.majdoor.ovr.shramik.app.Adapters.PostAdapter
import com.majdoor.ovr.shramik.app.Adapters.WorkerPostAdapter
import com.majdoor.ovr.shramik.app.DataClasses.Constants
import com.majdoor.ovr.shramik.app.DataClasses.PostData
import com.majdoor.ovr.shramik.app.DataClasses.WorkersData
import com.majdoor.ovr.shramik.app.databinding.FragmentSaveBinding


class SaveFragment : Fragment() {
    private lateinit var binding: FragmentSaveBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var postlist:ArrayList<PostData>
    private lateinit var workersList:ArrayList<WorkersData>
    private lateinit var savedList: ArrayList<String>
    private lateinit var category: String
    private lateinit var type:String
    private lateinit var sharedPref:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveBinding.inflate(inflater, container, false)


        setActionBar()
        init()

        clickListeners()
        getSavedList()


        return binding.root
    }

    private fun clickListeners() {
        binding.deleteAll.setOnClickListener {
            database.reference.child(Constants.SAVED_POSTS).child(type).child(auth.currentUser!!.uid).removeValue()
                .addOnSuccessListener {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setActionBar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        sharedPref = context?.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)!!
        type = sharedPref.getString(Constants.APPLIED_FOR, null).toString()
        category = Constants.category(type)
    }

    private fun getSavedList() {
        binding.progressBar.visibility = View.VISIBLE
        database.reference.child(Constants.SAVED_POSTS).child(type).child(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                savedList = ArrayList()
                for(snapshot1 in snapshot.children){
                    val data = snapshot1.getValue(String::class.java)
                    if(data!=null) {
                        savedList.add(data)
                    }
                }
                if(savedList.isEmpty()){
                    binding.noDrafts.visibility = View.VISIBLE
                    binding.itemRecylerView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    return
                }
                if(category.equals(Constants.AVAILABLE_JOBS))
                    getPostData()
                else
                    getWorkerData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
    private fun getWorkerData(){
        database.reference.child(Constants.POSTS).child(category).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                workersList = ArrayList()
                for(snapshot1 in snapshot.children){
                    val data: WorkersData? = snapshot1.getValue(WorkersData::class.java)
                    if (data != null) {
                        val id = data.id.toString()
                        for(saved in savedList){
                            if(id.equals(saved)){
                                workersList.add(data)
                                break
                            }
                        }
                    }
                }
                binding.progressBar.visibility = View.GONE
                if(workersList.isEmpty())
                    binding.noDrafts.visibility = View.GONE
                binding.itemRecylerView.visibility = View.VISIBLE

                val adapter = context?.let { WorkerPostAdapter(it, workersList, savedList) }
                adapter?.notifyDataSetChanged()
                binding.itemRecylerView.adapter = adapter
                val llm = LinearLayoutManager(context)
                llm.reverseLayout = true
                llm.stackFromEnd = true
                binding.itemRecylerView.layoutManager = llm
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getPostData() {
        database.reference.child(Constants.POSTS).child(category).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postlist = ArrayList()
                for(snapshot1 in snapshot.children){
                    val data: PostData? = snapshot1.getValue(PostData::class.java)
                    if (data != null) {
                        val id = data.id.toString()
                        for(saved in savedList){
                            if(id.equals(saved)){
                                postlist.add(data)
                                break
                            }
                        }
                    }
                }
                binding.progressBar.visibility = View.GONE
                if(postlist.isEmpty())
                binding.noDrafts.visibility = View.GONE
                binding.itemRecylerView.visibility = View.VISIBLE

                val adapter = context?.let { PostAdapter(it, postlist, savedList) }
                adapter?.notifyDataSetChanged()
                binding.itemRecylerView.adapter = adapter
                val llm = LinearLayoutManager(context)
                llm.reverseLayout = true
                llm.stackFromEnd = true
                binding.itemRecylerView.layoutManager = llm
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

}