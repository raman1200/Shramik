package com.majdoor.ovr.shramik.app.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.majdoor.ovr.shramik.app.DataClasses.Constants
import com.majdoor.ovr.shramik.app.DataClasses.UserData
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.ActivityChooseBinding

class ChooseActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseBinding
    lateinit var email: String
    lateinit var number: String
    lateinit var image: String
    lateinit var uid:String
    lateinit var name:String
    lateinit var appliedFor:String
    lateinit var auth: FirebaseAuth
    lateinit var database:FirebaseDatabase
    lateinit var sharedPreferences: SharedPreferences
    val SHARED_PREF_NAME = "my-pref"
    lateinit var const: Constants

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialise()
        buttonListeners()
    }

    private fun buttonListeners() {
        binding.builder.setOnClickListener{
            binding.builder.setBackgroundResource(R.drawable.selected_border)
            appliedFor = "Builder"
            binding.worker.setBackgroundResource(R.drawable.border)
            markButtonEnable(binding.next)
        }
        binding.worker.setOnClickListener {
            binding.worker.setBackgroundResource(R.drawable.selected_border)
            binding.builder.setBackgroundResource(R.drawable.border)
            appliedFor = "Worker"
            markButtonEnable(binding.next)
        }
        binding.next.setOnClickListener {
            binding.progressBar.visibility= View.VISIBLE
            uid = auth.currentUser?.uid.toString()
            email = auth.currentUser?.email.toString()
            number = auth.currentUser?.phoneNumber.toString()
            name = auth.currentUser?.displayName.toString()
            image = auth.currentUser?.photoUrl.toString()

            val data = UserData()
            data.Uid = uid
            data.Email = email
            data.Number = number
            data.Name = name
            data.Image = image
            data.Applied_for = appliedFor
            database.reference.child("Users").child(appliedFor).child(uid).setValue(data)
                .addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Login SuccessFully", Toast.LENGTH_SHORT).show()
                    val editor = sharedPreferences.edit()
                    editor.putString(Constants.APPLIED_FOR, appliedFor)
                    editor.putString(Constants.IMAGE, image)
                    editor.putString(Constants.NAME, name)
                    editor.apply()
                    val intent = Intent(this@ChooseActivity, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }.addOnFailureListener{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }

        }
    }
    private fun markButtonDisable(button: Button) {
        button.isEnabled = false
        button.setBackgroundResource(R.drawable.button_bg)
    }
    private fun markButtonEnable(button: Button) {
        button.isEnabled = true
        button.setBackgroundResource(R.drawable.next_btn)
    }
    private fun initialise() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        const = Constants()
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        markButtonDisable(binding.next)
    }
}