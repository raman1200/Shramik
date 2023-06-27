package com.majdoor.ovr.shramik.app.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.majdoor.ovr.shramik.app.Activities.PostDetailActivity
import com.majdoor.ovr.shramik.app.DataClasses.Constants
import com.majdoor.ovr.shramik.app.DataClasses.PostData
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.PostListViewBinding
open class PostAdapter(var context: Context, var list: ArrayList<PostData>, var savedList:ArrayList<String>) : RecyclerView.Adapter<PostAdapter.postViewHolder>() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val const:Constants = Constants()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_list_view, parent, false)
        return postViewHolder(view)
    }

    override fun onBindViewHolder(holder: postViewHolder, position: Int) {
        var saved = false
        val data = list[position]
        setSavedIcon(holder, saved)
        holder.binding.title.text = data.title
        holder.binding.desc.text = data.description
        holder.binding.type.text = data.category
        holder.binding.payment.text = "Rs.${data.salary}"
        holder.binding.date.text = data.date
        holder.binding.location.text = data.location
        val id = data.id.toString()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        if(savedList.isNotEmpty() && savedList.contains(id)){
            saved = true
            setSavedIcon(holder, saved)
        }
        holder.binding.savePost.setOnClickListener {
            saved = !saved
            val state = mapOf(id to id)
            setSavedIcon(holder, saved)
            if(saved){
                database.reference.child(Constants.SAVED_POSTS).child(Constants.WORKER).child(uid).updateChildren(state)
            }
            else{
                database.reference.child(Constants.SAVED_POSTS).child(Constants.WORKER).child(uid).child(id).removeValue()
            }
        }
        holder.binding.moreDetails.setOnClickListener {
            val intent = Intent(context, PostDetailActivity::class.java)
            intent.putExtra(Constants.TITLE, data.title)
            intent.putExtra(Constants.DESCRIPTION, data.description)
            intent.putExtra(Constants.CATEGORY, data.category)
            intent.putExtra(Constants.SALARY, "Rs.${data.salary}")
            intent.putExtra(Constants.DATE, data.date)
            intent.putExtra(Constants.LOCATION, data.location)
            intent.putExtra(Constants.UPLOADEDBY, data.uploadedBy)
            intent.putExtra(Constants.POST_ID, data.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    private fun setSavedIcon(holder: postViewHolder, saved:Boolean){
        if(saved){
            holder.binding.savePost.setImageResource(R.drawable.save)
        }
        else{
            holder.binding.savePost.setImageResource(R.drawable.unsave)
        }
    }

    class postViewHolder(itemView: View) : ViewHolder(itemView){
        var binding:PostListViewBinding
        init {
            binding = PostListViewBinding.bind(itemView)
        }
    }

}
