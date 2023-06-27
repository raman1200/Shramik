package com.majdoor.ovr.shramik.app.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.majdoor.ovr.shramik.app.DataClasses.Constants
import com.majdoor.ovr.shramik.app.DataClasses.WorkersData
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.ActivityWorkerPostBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class WorkerPostActivity : AppCompatActivity() {
    lateinit var binding:ActivityWorkerPostBinding
    lateinit var database: FirebaseDatabase
    lateinit var auth:FirebaseAuth
    lateinit var name:String
    lateinit var experience:String
    lateinit var location:String
    lateinit var salary:String
    lateinit var category:String
    lateinit var storage: FirebaseStorage
    lateinit var uid:String
    lateinit var uniqueId: String
    lateinit var data:WorkersData
    var selectedImageUri: Uri? = null
    var selectedImageBitmap:Bitmap? = null
    lateinit var launchSomeActivity:ActivityResultLauncher<Intent>
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
        binding = ActivityWorkerPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        init()
        clickListener()
    }



    private fun init(){
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        data = WorkersData()
        storage = FirebaseStorage.getInstance()
        setImage()
        binding.spinner.adapter =  ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

    }
    private fun clickListener(){
        binding.profileImage.setOnClickListener { imageChooser() }
        binding.post.setOnClickListener{
            checkPostData()
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category = binding.spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }
    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(i)
    }
    private fun setActionBar(){
        setSupportActionBar(binding.toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() }
    }
    private fun checkPostData(){
            name = binding.name.text.toString()
            experience = binding.experince.text.toString()
            location = binding.location.text.toString()
            salary = binding.salary.text.toString()
            category = binding.spinner.selectedItem.toString()

            if(name.isEmpty()){
                binding.nameLayout.helperText = "Please Enter your Full Name"
            }
            else if(location.isEmpty()){
                binding.locationLayout.helperText = "Please Enter the Job Location"
            }
            else if(category.equals("Select Category")){
                binding.spinnerHt.text = "Please Select the Job Type"
            }
            else if(experience.isEmpty()){
                binding.experienceLayout.helperText = "Please Enter your Experience"
            }
            else if(salary.isEmpty()){
                binding.salaryLayout.helperText = "Please Enter the Expected Salary"
            }
            else if(selectedImageBitmap==null){
                setData()
                uploadPost()
            }
            else{
                setData()
                uploadImage()
            }
        }

    private fun setData() {
        data.name = name
        data.location = location
        data.category = category
        data.experience = experience
        data.salary = salary
        data.date = Constants.setCurrentDate()
        uid = auth.currentUser?.uid.toString()
        uniqueId = UUID.randomUUID().toString()
        data.id = uniqueId
        data.uploader = uid
    }

    private fun uploadPost() {


        database.reference.child(Constants.POSTS).child(Constants.AVAILABLE_WORKERS).child(uniqueId).setValue(data).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this@WorkerPostActivity, "Post Uploaded Successfully", Toast.LENGTH_SHORT).show()
                clearData();
            }
            else{
                Toast.makeText(this@WorkerPostActivity, it.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun uploadImage() {
        val baos = ByteArrayOutputStream()
        selectedImageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val finalimg = baos.toByteArray()
        val reference: StorageReference = storage.reference.child(Constants.POSTS).child(Constants.AVAILABLE_WORKERS)
            .child(finalimg.toString() + "jpg")
        val uploadTask: UploadTask = reference.putBytes(finalimg)

        uploadTask.addOnFailureListener(OnFailureListener { exception ->
            Toast.makeText(this@WorkerPostActivity,"Error: ${exception.message}", Toast.LENGTH_SHORT).show()

        }).addOnSuccessListener(OnSuccessListener<Any?> {
            reference.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                data.image = uri.toString()
                uploadPost()
            })
        })
    }

    private fun clearData() {
        binding.name.text?.clear()
        binding.experince.text?.clear()
        binding.location.text?.clear()
        binding.salary.text!!.clear()
        binding.spinner.setSelection(0)
        binding.name.requestFocus()
        binding.profileImage.setImageResource(R.drawable.user)
    }
    private fun setImage() {
        launchSomeActivity = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val imageData = result.data
                // do your operation from here....
                if (imageData != null && imageData.data != null) {

                    selectedImageUri = imageData.data!!
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)

                        binding.profileImage.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}