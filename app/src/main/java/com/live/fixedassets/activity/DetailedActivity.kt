package com.live.fixedassets.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.live.fixedassets.R
import com.live.fixedassets.databinding.ActivityDetailedBinding

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickListener()
    }
    private fun onClickListener(){
        val blueColor = ContextCompat.getColor(this, R.color.blue)
        val whiteColor = ContextCompat.getColor(this, R.color.white)
        val cornerRadius = resources.getDimension(R.dimen.button_cornerRadius)
        binding.btnVerify.setOnClickListener {
            binding.apply {
                btnVerify.background = createRoundedDrawable(blueColor, cornerRadius)
                btnVerify.setTextColor(Color.WHITE)
                btnNext.background = createRoundedDrawable(whiteColor, cornerRadius)
                btnNext.setTextColor(blueColor)
            }
        }
        binding.btnNext.setOnClickListener {
            binding.apply {
                btnVerify.background = createRoundedDrawable(whiteColor, cornerRadius)
                btnNext.background = createRoundedDrawable(blueColor, cornerRadius)
                btnNext.setTextColor(Color.WHITE)
                btnVerify.setTextColor(blueColor)
                //startActivity(Intent(this@ScannerActivity,DetailedActivity::class.java))
            }
        }
    }
    private fun createRoundedDrawable(backgroundColor: Int, cornerRadius: Float): GradientDrawable {
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.RECTANGLE
        shapeDrawable.cornerRadius = cornerRadius
        shapeDrawable.setColor(backgroundColor)
        return shapeDrawable
    }
}