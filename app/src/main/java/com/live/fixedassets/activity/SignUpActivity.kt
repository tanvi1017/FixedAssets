package com.live.fixedassets.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.live.fixedassets.R
import com.live.fixedassets.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickListener()
    }
    private fun onClickListener(){
        binding.signIn.setOnClickListener {
            val intent = Intent(this@SignUpActivity,HomeActivity::class.java)
            startActivity(intent)

        }
    }
}