package com.live.fixedassets.core

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.live.fixedassets.R
import com.live.fixedassets.util.toast

abstract class BaseFragment<Binding : ViewBinding>  : Fragment() {
    private var _binding: Binding? = null
    val binding: Binding
        get() = _binding!!
    var navigate: Navigation? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    fun popCurrentFragment() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): Binding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            navigate = context as? Navigation
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement YourInterface")
        }
    }

    private val READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "image/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                imagePickerLauncher?.launch(intent)
            } else {
                val openSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                openSettingsIntent.data = uri
                startActivity(openSettingsIntent)
            }
        }
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let { uri -> toast(uri.toString()) }
            }
        }

    fun openImage(onImageSelected: (Intent?) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                READ_EXTERNAL_STORAGE_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            imagePickerLauncher?.launch(intent)
        } else {
            requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE_PERMISSION)
        }
        val cameraPermission = Manifest.permission.CAMERA
        val requestPermissionLauncher2 =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    createCameraIntent(requireActivity())
                } else {
                    if (!shouldShowRequestPermissionRationale(cameraPermission)) {
                        // User has permanently denied the permission, show a dialog or navigate to app settings
                        openAppSettings(requireActivity())
                    } else {
                        // User has denied the permission, show a rationale and request again if needed

                    }
                }
            }

        fun openCamera(onImageCaptured: (Intent?) -> Unit) {


            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    cameraPermission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onImageCaptured(createCameraIntent(requireActivity()))
            } else {
                requestPermissionLauncher2.launch(cameraPermission)
            }
        }
    }

    private fun Fragment.openAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

//    fun imageDialog() {
//        val dialog = Dialog(requireActivity())
//        dialog.setContentView(R.layout.camera_gallery_popup)
//        val window = dialog.window
//        window!!.setGravity(Gravity.BOTTOM)
//        window.setLayout(
//            RelativeLayout.LayoutParams.WRAP_CONTENT,
//            RelativeLayout.LayoutParams.WRAP_CONTENT
//        )
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        val camera = dialog.findViewById<TextView>(R.id.tvCamera)
//        val cancel = dialog.findViewById<TextView>(R.id.tv_cancel)
//        val gallery = dialog.findViewById<TextView>(R.id.tvGallery)
//        cancel.setOnClickListener { dialog.dismiss() }
//
//        camera.setOnClickListener {
//            dialog.dismiss()
//            if (mVideoDialog) {
//                captureVideo()
//            } else {
//                captureImage()
//            }
//
//        }
//
//        gallery.setOnClickListener {
//            dialog.dismiss()
//            if (mVideoDialog) {
//                openGalleryForVideo()
//            } else {
//                openGalleryForImage()
//            }
//        }
////        avatar.setOnClickListener {
////            selectedImage("", mCode)
////            dialog.dismiss()
////        }
//        dialog.show()
//    }
        private fun Fragment.createCameraIntent(activity: Activity): Intent? {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            return if (intent.resolveActivity(activity.packageManager) != null) {
                intent
            } else {
                // No camera app available, handle accordingly
                null
            }
        }

    }
