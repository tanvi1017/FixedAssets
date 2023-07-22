package com.live.fixedassets.fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.live.fixedassets.R
import com.live.fixedassets.activity.HomeActivity
import com.live.fixedassets.activity.ScannerActivity
import com.live.fixedassets.adapter.HomeCategoryAdapter
import com.live.fixedassets.core.BaseFragment
import com.live.fixedassets.databinding.FragmentHomeBinding
import com.live.fixedassets.`interface`.OrderListener
import com.live.fixedassets.model.CategDataItem
import com.live.fixedassets.util.ImagePickerUtility_old
import com.live.fixedassets.util.show
import com.live.fixedassets.util.toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment<FragmentHomeBinding>(), OrderListener {
    var items = emptyList<CategDataItem>()
    private var outputFileUri: Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateRecyclerView()
        binding.ivProfile.setOnClickListener {
            requestPermissions()
        }
    }
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)

    }
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private fun requestPermissions() {
        val permissionResults = permissions.map {
            ContextCompat.checkSelfPermission(requireActivity(), it)
        }

        val permissionsToRequest = permissions.filterIndexed { index, _ ->
            permissionResults[index] != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest,
                STORAGE_PERMISSION_CODE
            )
        } else {
            chooseImage()
        }
    }

    override fun onItemClicked(position: Int) {
        startActivity(Intent(requireActivity(),ScannerActivity::class.java))
    }

    override fun onEditClick(position: Int) {
    }
    override fun onDeleteClick(position: Int) {
    }
    private fun populateRecyclerView(){
        items = listOf(
            CategDataItem(R.drawable.qr_mode, ""),
            CategDataItem(R.drawable.qr_mode, "Fruits"),
            CategDataItem(R.drawable.qr_mode, "Fruits"),
            CategDataItem(R.drawable.qr_mode, "Fruits"),
            CategDataItem(R.drawable.qr_mode, "Fruits"),
            CategDataItem(R.drawable.qr_mode, "Fruits"),


        ).also { items = it }
        val homeCategoryAdapter = HomeCategoryAdapter(items as MutableList<CategDataItem>,this)
        binding.rvHomeRecyclerview.apply {
            val layoutManager = GridLayoutManager(requireActivity(),2)
            binding.rvHomeRecyclerview.layoutManager = layoutManager
            // layoutManager = LinearLayoutManager(requireActivity())
            adapter = homeCategoryAdapter
        }
    }
    private fun chooseImage() {

        // Determine Uri of camera image to save.
        val root = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + "MyDir" + File.separator
        )
        root.mkdirs()
        val fname: String = generateFileName()
        val sdImageMainDirectory = File(root, fname)
        outputFileUri = Uri.fromFile(sdImageMainDirectory)
        val myIntents: MutableList<Intent> = ArrayList()

        // Camera.
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager: PackageManager = requireActivity().packageManager
        val listCam = requireActivity().packageManager.queryIntentActivities(captureIntent, 0)

        for (res in listCam) {
            val packageName = res.activityInfo.packageName
            val intent = Intent(captureIntent)
            intent.component = ComponentName(packageName, res.activityInfo.name)
            intent.setPackage(packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            myIntents.add(intent)
        }


        // Filesystem.
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT


        val listGall = packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGall) {
            val finalIntent = Intent(galleryIntent)
            finalIntent.component =
                ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            myIntents.add(finalIntent)
        }


        // Chooser of filesystem options.
        val chooserIntent = Intent.createChooser(galleryIntent, "Select Source")

        // Add the camera options.
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            myIntents.toTypedArray<Parcelable>()
        )
        startActivityForResult(chooserIntent, FILE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                val storagePermissionIndex =
                    permissions.indexOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                val cameraPermissionIndex = permissions.indexOf(Manifest.permission.CAMERA)

                if (grantResults[storagePermissionIndex] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[cameraPermissionIndex] == PackageManager.PERMISSION_GRANTED
                ) {
                    // Storage and camera permissions granted
                    // You can perform operations requiring both permissions here
                    chooseImage()
                    // imageDialog
                } else {
                    // Storage or camera permission denied
                    // Handle the denial or disable functionality that depends on these permissions
                }
            }
        }
    }
    private fun imageDialog() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.camera_gallery_popup)
        val window = dialog.window
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val camera = dialog.findViewById<TextView>(R.id.tvCamera)
        val cancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        val gallery = dialog.findViewById<TextView>(R.id.tvGallery)
        cancel.setOnClickListener { dialog.dismiss() }

        camera.setOnClickListener {
//
//            this@ProfileFragment.openCamera(this@ProfileFragment.requireActivity()){cameraIntent ->
//                cameraIntent?.let {
//                    captureImageLauncher.launch(it)
//                }
//            }
        }

        gallery.setOnClickListener {
            openImage{ urii->
                toast(urii.toString())
            }
        }

        dialog.show()
    }

    private val captureImageLauncher: ActivityResultLauncher<Intent?> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Image captured successfully, handle the result
            val data: Intent? = result.data
            data?.data?.let {
                binding.ivProfile.setImageURI(it)
            }
            // ...
        } else {
            // Image capture canceled or failed, handle accordingly
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                FILE_PICK_CODE -> {
                    val isCamera: Boolean = data?.action == MediaStore.ACTION_IMAGE_CAPTURE
                    val selectedImageUri: Uri? = if (isCamera) {
                        outputFileUri
                    } else {
                        data?.data
                    }

                    selectedImageUri?.let { uri ->
                        binding.ivProfile.setImageURI(uri)
                        imageCameraLauncher
                    }

                }
            }
        }
    }

private fun generateFileName(): String {
    val vowels = listOf("a", "e", "i", "o", "u")
    val consonants = listOf(
        "b", "c", "d", "f", "g", "h", "j", "k", "l", "m",
        "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z"
    )
    val random = java.util.Random()

    val nameLength = random.nextInt(4) + 4 // Random length between 4 and 7 characters
    var name = ""

    for (i in 0 until nameLength) {
        if (i % 2 == 0) {
            name += consonants[random.nextInt(consonants.size)]
        } else {
            name += vowels[random.nextInt(vowels.size)]
        }
    }

    return name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

    private val imageCameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Image captured successfully, handle the result
            val uri: Uri? = outputFileUri
            uri?.let {
                // Set the captured image URI to the ImageView
                binding.ivProfile.setImageURI(it)
            }
        } else {
            // Image capture canceled or failed, handle accordingly
        }
    }


    companion object {
        const val FILE_PICK_CODE = 101
        const val STORAGE_PERMISSION_CODE = 1
        const val CAMERA_PERMISSION_CODE = 2
        fun newInstance(first: String, second: String) : ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString("firstString", first)
                    putString("secondString", second)
                }
            }
        }
    }

}

data class ProfileItemData(val itemLabel: String, val itemValue: String)
