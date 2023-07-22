package com.live.fixedassets.activity
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.ResultPoint
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.live.fixedassets.R
import com.live.fixedassets.databinding.ActivityScannerBinding

class ScannerActivity : AppCompatActivity() {
    private lateinit var binding:ActivityScannerBinding
    private var barcodeView: DecoratedBarcodeView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        barcodeView = findViewById(R.id.barcodeScannerView)
        fetchScanner()
    }
    private fun fetchScanner(){
        binding.rvScanner.setOnClickListener {
            setupBarcodeScanner()

        }
    }
    private fun setupBarcodeScanner() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            barcodeView?.decodeContinuous(callback)
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView?.resume()
    }
    override fun onPause() {
        super.onPause()
        barcodeView?.pause()
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            val scannedText = result.text
            Toast.makeText(this@ScannerActivity, "Scanned Text: $scannedText", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@ScannerActivity, DetailedActivity::class.java)
            intent.putExtra("scannedText", scannedText)
            startActivity(intent)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeView?.decodeContinuous(callback)
            } else {
                // Camera permission not granted. Handle this case.
                Toast.makeText(this, "Camera permission not granted!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onClickListener(){
        val blueColor = ContextCompat.getColor(this, R.color.blue)
        val whiteColor = ContextCompat.getColor(this, R.color.white)
        val cornerRadius = resources.getDimension(R.dimen.button_cornerRadius)
//        binding.btnVerify.setOnClickListener {
//            binding.apply {
//                btnVerify.background = createRoundedDrawable(blueColor, cornerRadius)
//               btnVerify.setTextColor(Color.WHITE)
//               btnNext.background = createRoundedDrawable(whiteColor, cornerRadius)
//                btnNext.setTextColor(blueColor)
//            }
//        }
//        binding.btnNext.setOnClickListener {
//            binding.apply {
//                btnVerify.background = createRoundedDrawable(whiteColor, cornerRadius)
//                btnNext.background = createRoundedDrawable(blueColor, cornerRadius)
//                btnNext.setTextColor(Color.WHITE)
//                btnVerify.setTextColor(blueColor)
//                startActivity(Intent(this@ScannerActivity,DetailedActivity::class.java))
//            }
//        }
    }
    private fun createRoundedDrawable(backgroundColor: Int, cornerRadius: Float): GradientDrawable {
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.RECTANGLE
        shapeDrawable.cornerRadius = cornerRadius
        shapeDrawable.setColor(backgroundColor)
        return shapeDrawable
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }
}