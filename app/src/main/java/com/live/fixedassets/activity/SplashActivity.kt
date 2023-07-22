package com.live.fixedassets.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.live.fixedassets.R
import com.live.fixedassets.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()

    }
    private fun onClick() {
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPref = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
            val loginCheck = sharedPref.getBoolean("FirstLogin",false)

            if (loginCheck) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 2000)
    }
}