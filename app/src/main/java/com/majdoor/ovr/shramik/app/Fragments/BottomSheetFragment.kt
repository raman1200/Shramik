package com.majdoor.ovr.shramik.app.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.majdoor.ovr.shramik.app.Activities.ChooseActivity
import com.majdoor.ovr.shramik.app.Activities.PersonalDetailActivity
import com.majdoor.ovr.shramik.app.R
import com.majdoor.ovr.shramik.app.databinding.FragmentBottomSheetBinding
import java.util.concurrent.TimeUnit

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    var storedVerificationId:String =""
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var auth:FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        markButtonDisable(binding.submit)

        return binding.root

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this.requireActivity()) { task ->
            if (task.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                // Sign in success, update UI with the signed-in user's information
                val intent = Intent(activity?.applicationContext, ChooseActivity::class.java)
                intent.putExtra("reference","phone")
                startActivity(intent)
                activity?.finish()
                val user = task.result?.user
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this.requireActivity(), "Failed ..",Toast.LENGTH_SHORT).show()
                // Sign in failed, display a message and update the UI
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this.requireActivity(), "Invalid Code",Toast.LENGTH_SHORT).show()
                }
                // Update UI
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this.requireActivity()) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)

                }

                override fun onVerificationFailed(e: FirebaseException) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity?.applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    binding.firstPinView.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    storedVerificationId = verificationId
                    resendToken = token
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun markButtonDisable(button: Button) {
        button.isEnabled = false
        button.setTextColor(ContextCompat.getColor(this.requireActivity(), R.color.white))
        button.setBackgroundColor(ContextCompat.getColor(this.requireActivity(), R.color.grey))
    }
    private fun markButtonEnable(button: Button, text:String="Continue") {
        button.isEnabled = true
        button.text = text
        button.setTextColor(ContextCompat.getColor(this.requireActivity(), R.color.black))
        button.setBackgroundColor(ContextCompat.getColor(this.requireActivity(), R.color.green))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.submit.setOnClickListener{
            val number = binding.phone.text.toString()
            if(number.length==10){
                val phoneNumber = "+"+binding.countryCodePicker.selectedCountryCodeAsInt.toString() + number.trim()
                if(binding.submit.text.toString().equals("Continue",ignoreCase = true)){
                    binding.progressBar.visibility = View.VISIBLE
                    markButtonDisable(binding.submit)
                    Toast.makeText(context, "Otp Sending...", Toast.LENGTH_SHORT).show()
                    sendVerificationCode(phoneNumber)

                }
                else{
                    binding.progressBar.visibility = View.VISIBLE
                    val credential = PhoneAuthProvider.getCredential(storedVerificationId, binding.firstPinView.text.toString())
                    signInWithPhoneAuthCredential(credential)
                }
                binding.firstPinView.visibility = View.VISIBLE
                binding.firstPinView.isEnabled = false
            }
            else{
                binding.phone.error = "Enter your 10 digit Number"
            }
        }
        binding.firstPinView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(start==5){
                    markButtonEnable(binding.submit)
                    view.hideKeyboard()
                }
                else{
                    markButtonDisable(binding.submit)
                }

            }
        })

        binding.phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(start==9){
                    markButtonEnable(binding.submit)
                    view.hideKeyboard()
                }
                else{
                    binding.firstPinView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    markButtonDisable(binding.submit)
                }

            }
        })
        binding.firstPinView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(start==5){
                    view.hideKeyboard()
//
//                    context?.let { ContextCompat.getColor(it, R.color.green) }
//                        ?.let { binding.submit.setBackgroundColor(it) };
                    markButtonEnable(binding.submit, "Verify")
                }

            }
        })

        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }