package com.live.fixedassets.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.net.Uri
import android.os.*
import android.os.Build.VERSION.SDK_INT
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


fun View.show(){
    visibility = View.VISIBLE
}
fun View.hide(){
    visibility = View.GONE
}
fun View.shouldShow(shouldShow:Boolean = true){
    visibility = if(shouldShow) View.VISIBLE else View.GONE
}
fun delay(delayMillis: Long, callback: () -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed(callback, delayMillis)
}
inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

fun View.setColoredCard(
    width:Int = ViewGroup.LayoutParams.MATCH_PARENT,
    height: Int=50.toPixel(),
    cornerRadius: Float ,
    backgroundColor: Int
): View {
    val shapeDrawable = ShapeDrawable()
    shapeDrawable.shape = RoundRectShape(
        floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius,
            cornerRadius, cornerRadius, cornerRadius, cornerRadius),
        null,
        null
    )
    shapeDrawable.paint.color = backgroundColor

    val cardView = View(context)
    cardView.background = shapeDrawable

    val layoutParams = ViewGroup.LayoutParams(width, height)
    cardView.layoutParams = layoutParams

    val margin = 5//resources.getDimensionPixelSize(R.dimen.card_view_margin) // Adjust margin as desired
    val marginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
    marginLayoutParams.setMargins(margin, margin, margin, margin)
    cardView.layoutParams = marginLayoutParams

    return cardView
}

fun Int.toPixel(): Int {
    return dpToPixels(this.toFloat())
}
fun pixelToDp(pixel: Int): Float {
    val r = Resources.getSystem()
    return pixel.toFloat() / r.displayMetrics.density
}
fun dpToPixels(dp: Float): Int {
    val r = Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
}

fun Random.randomColor(): Int {
    val alpha = 255 // You can customize the alpha value if needed
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color.argb(alpha, red, green, blue)
}

fun formatTimestampToDate(timestamp: Long): String {
    val date = Date(timestamp)
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return dateFormat.format(date)
}


fun View.makeToast(message:String){
    Toast.makeText(this.context,message, Toast.LENGTH_LONG).show()
}
fun isEmailValid(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.matches(emailPattern.toRegex())
}

/**
 * Here in regex - "^[+]?[0-9]{10,13}$" 10, 13 are the minimun and max number of charcters allowed with
 */
fun isPhoneNumberValid(phoneNumber: String): Boolean {
    val phonePattern = "^[+]?[0-9]{10,13}$"
    return phoneNumber.matches(phonePattern.toRegex())
}

fun isPasswordValid(password: String): Boolean {
    val passwordPattern = "^(?=.*\\d)(?=.*[!@#\$%^&*])(?=.*[a-zA-Z]).{8,}$"
    return password.matches(passwordPattern.toRegex())
}
fun Boolean.toggle():Boolean{
    return !this
}
fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}
fun DialogFragment.setFullScreen() {
    dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}
fun Context.toast(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}
fun Fragment.toast(msg:String){
    Toast.makeText(this.requireContext(),msg,Toast.LENGTH_LONG).show()
}
fun textWatcherEnable(editText: EditText, charLimit:Int){
    editText.addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // This method is called before the text is changed
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // This method is called when the text is changing
        }

        override fun afterTextChanged(s: Editable) {
            if ((s?.length ?: 0) > charLimit) {
                editText.setText(s?.subSequence(0, charLimit)) // Truncate the text to 10 characters
                editText.setSelection(charLimit) // Set the cursor position to the end
            }
        }
    })

}


