package com.live.fixedassets.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.live.fixedassets.R
import com.live.fixedassets.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickListener()
    }
    private fun onClickListener(){
        binding.btnLogin.setOnClickListener {
            checkLogIn()
//            val intent = Intent(this@LoginActivity,HomeActivity::class.java)
//            startActivity(intent)
        }
       binding.signUp.setOnClickListener {
           val intent = Intent(this@LoginActivity,SignUpActivity::class.java)
           startActivity(intent)

       }
    }
    private fun getStringFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
    private fun checkLogIn(){
        val loginEmail = binding.edtEmail.text.trim().toString()
        val loginPass = binding.edtPassword.text.trim().toString()
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val staticEmail = "admin@gmail.com"
        val staticPass = "12345678"
        if (loginEmail == staticEmail && loginPass == staticPass){
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "enter valid password", Toast.LENGTH_LONG).show()
        }
        editor.putBoolean("FirstLogin",true)
        editor.apply()
    }
}