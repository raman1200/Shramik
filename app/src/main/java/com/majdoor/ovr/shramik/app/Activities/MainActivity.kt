package com.majdoor.ovr.shramik.app.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.majdoor.ovr.shramik.app.DataClasses.Constants
import com.majdoor.ovr.shramik.app.Fragments.HomeFragment
import com.majdoor.ovr.shramik.app.Fragments.MessageFragment
import com.majdoor.ovr.shramik.app.Fragments.NotificationFragment
import com.majdoor.ovr.shramik.app.Fragments.SaveFragment
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.ActivityMainBinding


open class MainActivity : AppCompatActivity() {
    var id:Int= 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref:SharedPreferences
    private lateinit var const:Constants
    private lateinit var type:String
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       init()
       bottomNavigation()



    }
    private fun replaceFragment(fragment:Fragment) {
        val transaction:FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()

    }
    private fun bottomNavigation(){
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    count = 0
                    id = R.id.home
                    replaceFragment(HomeFragment())
                }
                R.id.save -> {
                    count = 1
                    id = R.id.save
                    replaceFragment(SaveFragment())
                }
                R.id.add -> {
                    count = 2
                    if(type.equals(null))
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    else if(type.equals("Builder")){
                        val intent = Intent(this@MainActivity, AddPostActivity::class.java)
                        startActivity(intent)

                    }
                    else if(type.equals("Worker")){
                        val intent = Intent(this@MainActivity, WorkerPostActivity::class.java)
                        startActivity(intent)
                    }


                }
                R.id.notification -> {
                    count = 3
                    id = R.id.notification
                    replaceFragment(NotificationFragment())
                }
                R.id.message -> {
                    count = 4
                    id = R.id.message
                    replaceFragment(MessageFragment())

                }
                else ->  replaceFragment(HomeFragment())
            }
            true
        }
    }
    private fun init(){
        const = Constants()
        sharedPref = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)!!
        type = sharedPref.getString(Constants.APPLIED_FOR, null).toString()
        id = R.id.home
        replaceFragment(HomeFragment())


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}