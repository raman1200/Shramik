package com.majdoor.ovr.shramik.app.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.majdoor.ovr.shramik.app.databinding.ActivityYourPostsBinding

class YourPostsActivity : AppCompatActivity() {
    lateinit var binding:ActivityYourPostsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}