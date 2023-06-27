package com.majdoor.ovr.shramik.app.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.majdoor.ovr.shramik.app.Activities.PostDetailActivity
import com.majdoor.ovr.shramik.app.DataClasses.Constants
import com.majdoor.ovr.shramik.app.DataClasses.WorkersData
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.WorkerPostViewBinding

open class WorkerPostAdapter(var context: Context, var list: ArrayList<WorkersData>, var savedList:ArrayList<String>) : RecyclerView.Adapter<WorkerPostAdapter.workerPostViewHolder>() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var arrayList: ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): workerPostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.worker_post_view, parent, false)
        return workerPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: workerPostViewHolder, position: Int) {
        val data = list[position]
        var saved = false
        holder.binding.name.text = data.name
        holder.binding.experince.text = "Experience: ${data.experience}/months"
        holder.binding.type.text = data.category
        holder.binding.payment.text = "Rs.${data.salary}"
        holder.binding.date.text = data.date
        holder.binding.location.text = data.location
        val id = data.id.toString()
        if(savedList.isNotEmpty() && savedList.contains(id)){
            saved = true
            setSavedIcon(holder, saved)
        }
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        arrayList = ArrayList()
        arrayList.clear()

        if(data.image != null){
            Glide.with(context).load(data.image).placeholder(R.drawable.user).into(holder.binding.image)
        }
        holder.binding.savePost.setOnClickListener {
            saved = !saved
            setSavedIcon(holder, saved)

            val state = mapOf(id to id)
            if(saved){
                database.reference.child(Constants.SAVED_POSTS).child(Constants.BUILDER).child(uid).updateChildren(state)
            }
            else{
                database.reference.child(Constants.SAVED_POSTS).child(Constants.BUILDER).child(uid).child(id).removeValue()
            }


        }
        holder.binding.moreDetails.setOnClickListener {
            val intent = Intent(context, PostDetailActivity::class.java)
            intent.putExtra(Constants.SAVED, true)
            intent.putExtra(Constants.TITLE, data.name)
            intent.putExtra(Constants.DESCRIPTION, "Experience: ${data.experience}/months")
            intent.putExtra(Constants.CATEGORY, data.category)
            intent.putExtra(Constants.SALARY, "Rs.${data.salary}")
            intent.putExtra(Constants.DATE, data.date)
            intent.putExtra(Constants.LOCATION, data.location)
            intent.putExtra(Constants.UPLOADEDBY, data.uploader)
            intent.putExtra(Constants.IMAGE, data.image)
            intent.putExtra(Constants.POST_ID, data.id)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    private fun setSavedIcon(holder: workerPostViewHolder, saved:Boolean){
        if(saved){
            holder.binding.savePost.setImageResource(R.drawable.save)
        }
        else{
            holder.binding.savePost.setImageResource(R.drawable.unsave)
        }
    }

    class workerPostViewHolder(itemView: View) : ViewHolder(itemView){
       var binding: WorkerPostViewBinding
       init {
           binding =  WorkerPostViewBinding.bind(itemView)
       }
    }

}
