package com.live.fixedassets.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.live.fixedassets.BuildConfig
import com.live.fixedassets.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

abstract class ImagePickerUtility_old:AppCompatActivity() {

    private var mActivity: Activity? = null
    var mVideoDialog: Boolean = false
    private var mCode = 0
    private lateinit var mImageFile: File


    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA)

    private val permissions1 = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA)


    private val externalStorageLaunch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                getImage()
            }
        }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            if (permissions.isNotEmpty()) {
                permissions.entries.forEach {
                    Log.d("permissions", "${it.key} = ${it.value}")
                }


                val readStorage = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE]
                val writeStorage = permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]
                val camera = permissions[android.Manifest.permission.CAMERA]

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
                    if (camera == true) {
                        Log.e("permissions", "Permission Granted Successfully")
                        imageDialog()

                    } else {
                        Log.e("permissions", "Permission not granted")
                        checkPermissionDenied(permissions.keys.first())
                    }

                }else{
                    if (readStorage == true && writeStorage == true && camera == true) {
                        imageDialog()
                    } else {
                        Log.e("permissions", "Permission not granted")
                        checkPermissionDenied(permissions.keys.first()) }
                }
            }
        }

    private val videoCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // There are no request codes
                Log.e("VideoSelected", "RESULT_OK")
                val contentURI = result.data?.data



                val selectedVideoPath = getPath(contentURI!!)

                val mediaFile: File = File(selectedVideoPath)
                val fileSizeInBytes = mediaFile.length()
                val fileSizeInKB = fileSizeInBytes / 1024
                val fileSizeInMB = fileSizeInKB / 1024

                if (fileSizeInMB > 25) {
                    Toast.makeText(this, "Video files lesser than 25 MB are allowed", Toast.LENGTH_LONG)
                        .show()

                }else{
                    selectedImage(selectedVideoPath, mCode)

                }

            }
        }

    private val imageCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = Uri.fromFile(mImageFile)
                val picturePath = getAbsolutePath(uri)
                selectedImage(picturePath, mCode)
            }
        }


    private val videoGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // There are no request codes
                Log.e("VideoSelected", "RESULT_OK")
                val contentURI = result.data?.data

                val selectedVideoPath = getPath(contentURI!!)

                val mediaFile: File = File(selectedVideoPath)
                val fileSizeInBytes = mediaFile.length()
                val fileSizeInKB = fileSizeInBytes / 1024
                val fileSizeInMB = fileSizeInKB / 1024

                if (fileSizeInMB > 25) {
                    Toast.makeText(this, "Video files lesser than 25 MB are allowed", Toast.LENGTH_LONG)
                        .show()

                }else{
                    selectedImage(selectedVideoPath, mCode)

                }

                // selectedImage(selectedVideoPath, mCode)
            }
        }

    private val imageGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                val picturePath = getAbsolutePath(uri!!)
                selectedImage(picturePath, mCode)
            }
        }


    private fun getImage() {

        //*****videoDialog -> put false for pick the Image.*****
        //*****videoDialog -> put true for pick the Video.*****

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            if (hasPermissions(permissions)) {
                Log.e("Permissionsgregregrgrg", "Permissions Granted")
                imageDialog()
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.e("Permissionsgregregrgrg", "read for Permissions")

                checkPermissionDenied(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.e("Permissionsgregregrgrg", "write for Permissions")

                checkPermissionDenied(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                Log.e("Permissionsgregregrgrg", "write sdvd for Permissions")

                checkPermissionDenied(android.Manifest.permission.CAMERA)
            } else {
                Log.e("Permissionsgregregrgrg", "Request for Permissions")
                requestPermission()
            }

        }else{
            if (hasPermissions(permissions1)) {
                Log.e("Permissionsgregregrgrg", "Permissions yhhhhhhhhhGranted")
                imageDialog()
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.e("Permissionsgregregrgrg", "read forrthhhhhhhhhhhh Permissions")

                checkPermissionDenied(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.e("Permissionsgregregrgrg", "write for rfffffffff Permissions")

                checkPermissionDenied(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                Log.e("Permissionsgregregrgrg", "write sdvd forrtttttttttttt Permissions")

                checkPermissionDenied(android.Manifest.permission.CAMERA)
            } else {
                Log.e("Permissionsgregregrgrg", "Request forrttttttttttt Permissions")
                requestPermission()
            }
        }

    }

    fun imageDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.camera_gallery_popup)
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        window.setLayout(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val camera = dialog.findViewById<TextView>(R.id.tvCamera)
        val cancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        val gallery = dialog.findViewById<TextView>(R.id.tvGallery)
        cancel.setOnClickListener { dialog.dismiss() }

        camera.setOnClickListener {
            dialog.dismiss()
            if (mVideoDialog) {
                captureVideo()
            } else {
                captureImage()
            }

        }

        gallery.setOnClickListener {
            dialog.dismiss()
            if (mVideoDialog) {
                openGalleryForVideo()
            } else {
                openGalleryForImage()
            }
        }
//        avatar.setOnClickListener {
//            selectedImage("", mCode)
//            dialog.dismiss()
//        }
        dialog.show()
    }


    private fun captureVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        videoCameraLauncher.launch(intent)

    }

    private fun captureImage() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        try {
            createImageFile(this, imageFileName, ".jpg")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileUri = FileProvider.getUriForFile(
            Objects.requireNonNull(this), BuildConfig.APPLICATION_ID,
            mImageFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        imageCameraLauncher.launch(intent)
    }

    private fun openGalleryForVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_PICK
        videoGalleryLauncher.launch(
            Intent.createChooser(intent, "Select Video")
        )

    }


    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageGalleryLauncher.launch(intent)
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context, name: String, extension: String) {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mImageFile = File.createTempFile(
            name,
            extension,
            storageDir
        )
    }

    // util method
    private fun hasPermissions(permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissionDenied(permissions: String) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            val mBuilder = AlertDialog.Builder(mActivity)
            val dialog: AlertDialog =
                mBuilder.setTitle(R.string.alert).setMessage(R.string.permissionRequired)
                    .setPositiveButton(
                        R.string.ok
                    ) { dialog, which -> requestPermission() }
                    .setNegativeButton(
                        R.string.cancel
                    ) { dialog, which ->

                    }.create()
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(
                        this, R.color.blue
                    )
                )
            }
            dialog.show()
        } else {
            val builder = AlertDialog.Builder(mActivity)
            val dialog: AlertDialog =
                builder.setTitle(R.string.alert).setMessage(R.string.permissionRequired)
                    .setCancelable(
                        false
                    )
                    .setPositiveButton(R.string.openSettings) { dialog, which ->
                        //finish()
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts(
                                "package",com.live.fixedassets.
                                BuildConfig.APPLICATION_ID + ".provider",
                                null
                            )
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }.create()
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(
                        this, R.color.blue
                    )
                )
            }
            dialog.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            Log.d("jdmmhbd","hhhhhhhhhhhhh")

            requestMultiplePermissions.launch(permissions)
        }else{
            Log.d("jdmmhbd","kkkkkkkkkkkkkkkkkk")
            requestMultiplePermissions.launch(permissions1)
        }
    }

    @SuppressLint("Recycle")
    private fun getAbsolutePath(uri: Uri): String {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            val cursor: Cursor?
            try {
                cursor = this.contentResolver.query(uri, projection, null, null, null)
                val columnIndex = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
            } catch (e: Exception) {
                // Eat it
                e.printStackTrace()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path!!
        }
        return ""
    }

    private fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor? =
            this.contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }



    @RequiresApi(Build.VERSION_CODES.M)
    open fun askStorageManagerPermission(activity: Activity, code: Int, videoDialog: Boolean) {

        mActivity = activity
        mCode = code
        mVideoDialog = videoDialog

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data =
                        Uri.parse(String.format("package:%s", activity.packageName))
                    externalStorageLaunch.launch(intent)
                } catch (e: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    externalStorageLaunch.launch(intent)
                }
            } else {
                getImage()
            }
        } else {
            getImage()
        }
    }

    abstract fun selectedImage(imagePath: String?, code: Int)

}


