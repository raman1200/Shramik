package com.majdoor.ovr.shramik.app.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.majdoor.ovr.shramik.app.DataClasses.Constants
import com.majdoor.ovr.shramik.app.DataClasses.PostData
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.ActivityAddPostBinding
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddPostBinding
    lateinit var database: FirebaseDatabase
    lateinit var auth:FirebaseAuth
    lateinit var title:String
    lateinit var desc:String
    lateinit var location:String
    lateinit var salary:String
    lateinit var category:String
    lateinit var date:String
    var items = arrayOf(
        "Select Category",
        "Carpenter",
        "Painter",
        "Gate/Grill maker",
        "POP",
        "Mistri",
        "Helper",
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        init()
        checkPostData()



    }
    private fun init(){
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        binding.spinner.adapter =  ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category = binding.spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }
    private fun setActionBar(){
        setSupportActionBar(binding.toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun checkPostData(){

        binding.post.setOnClickListener{
            title = binding.title.text.toString()
            desc = binding.description.text.toString()
            location = binding.location.text.toString()
            salary = binding.salary.text.toString()

            category = binding.spinner.selectedItem.toString()

            if(title.isEmpty()){
                binding.titleLayout.helperText = "Please Enter the post title"
            }
            else if(desc.isEmpty()){
                binding.descriptionLayout.helperText = "Please Enter the post description"
            }
            else if(location.isEmpty()){
                binding.locationLayout.helperText = "Please Enter the Job Location"
            }
            else if(salary.isEmpty()){
                binding.salaryLayout.helperText = "Please Enter the Expected Salary"
            }
            else if(category.equals("Select Category")){
                binding.spinnerHt.text = "Please Select the Job Type"
            }
            else{
                uploadPost()
            }
        }
    }

    private fun uploadPost() {
        val uid:String = auth.currentUser?.uid.toString()
        val uniqueId: String = UUID.randomUUID().toString()
        date = Constants.setCurrentDate()
        val data = PostData(uniqueId, uid, title, desc, location, category, salary, date)
        database.reference.child(Constants.POSTS).child(Constants.AVAILABLE_JOBS).child(uniqueId).setValue(data).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this@AddPostActivity, "Post Uploaded Successfully", Toast.LENGTH_SHORT).show()
                clearAll();
            }
            else{
                Toast.makeText(this@AddPostActivity, it.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun clearAll() {
        binding.title.getText()?.clear()
        binding.description.getText()?.clear()
        binding.location.getText()?.clear()
        binding.salary.getText()?.clear()
        binding.spinner.setSelection(0)
        binding.title.requestFocus()

    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}