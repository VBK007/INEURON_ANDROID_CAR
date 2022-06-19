package nr.king.carserv.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import nr.king.carserv.R
import nr.king.carserv.di.PreferenceManager
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

inline fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

inline fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.isOverLayRequired(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)
}



fun Context.showToast(text: CharSequence, isError: Boolean = false) {
    if (text.isNotEmpty()) {
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.inflate_toast, null)
        val toastText = view.findViewById<TextView>(R.id.toastText)
        toastText.text = text
        if (isError) {
            toastText.setTextColor(Color.WHITE)
            view.findViewById<View>(R.id.toastLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
        }
        val toast = Toast(this)
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }
}

@Throws(IOException::class)
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "RI_IMG_" + timeStamp + "_"
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    val image: File = File.createTempFile(imageFileName, ".jpg", storageDir)
    return image
}






//fragment
fun Context.hasPermission(permission: String): Boolean {
    if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        return true
    }
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.showGPSDisabledAlertToUser(): AlertDialog {
    val alertDialogBuilder = AlertDialog.Builder(this)
    alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
        .setCancelable(false)
        .setPositiveButton("Settings") { _, _ ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(callGPSSettingIntent)
        }
    alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
    return alertDialogBuilder.create()
}

/**********************Fragment Extensions ***********************************/
inline fun Fragment.toast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}


fun Fragment.showToast(text: String, isError: Boolean = false) {
    if (text.isNotEmpty()) {
        val inflater = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.inflate_toast, null)
        val toastText = view.findViewById<TextView>(R.id.toastText)
        toastText.text = text
        if (isError) {
            toastText.setTextColor(Color.WHITE)
            view.findViewById<View>(R.id.toastLayout).setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.purple_200))
        }
        val toast = Toast(requireActivity())
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }
}

fun View.invisibleIf(beInvisible: Boolean) = if (beInvisible) invisible() else visible()

fun View.visibleIf(beVisible: Boolean) = if (beVisible) visible() else gone()

fun View.goneIf(beGone: Boolean) = visibleIf(!beGone)

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.getBitmapFromDrawable(drawable: Int): Bitmap {
    return BitmapFactory.decodeResource(this.resources, drawable)
}



fun Int.getErrorMessage(): String {
    return when (this) {
        403 -> "Access denied for this user"
        404 -> "Please update to latest version of POS"
        401 -> ""
        504,
        424,
        502 -> "Please check whether Web Reporter / Gateway Service is running"
        else -> "Something went wrong.Error Code :$this"
    }
}

fun Throwable.getErrorMessage(): String {
    return when (this) {
        is HttpException -> {
            val response = this.response()
            response!!.code().getErrorMessage()
        }
        is SocketTimeoutException -> "Network Timed Out..Please try again"
        is ConnectException -> "Unable to connect to server...Please try again"
        is UnknownHostException -> "Host not available...Please contact support"
        is SocketException -> "Unable to connect to server...Please try again"
        else -> "Error in processing your request...Please try again"
    }
}

fun ResponseBody.processErrorMsg(): String {
    val errorResponse = JSONObject(this.string())
    return errorResponse.getString("message")
}

fun View.SnackShortMessage(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.SnackLongMessage(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun Activity.isGooglePlayServicesAvailable(): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
    if (status != ConnectionResult.SUCCESS) {
        if (googleApiAvailability.isUserResolvableError(status)) {
            googleApiAvailability.getErrorDialog(this, status, 2404)?.show()
        }
        return false
    }
    return true
}


fun currentDatetime(): String {
    val date = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    return dateFormat.format(date)
}

fun Any.formatDecimal(): String {
    val nf: NumberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH)
    val df = nf as DecimalFormat
    df.applyPattern("########0.00")
    return df.format(this)
}


fun View.hide()
{
    this.visibility= View.GONE
}

fun View.showOrhide(isTrue: Boolean)
{
    if (isTrue)
    {
        this.visibility= View.VISIBLE
    }
    else{
        this.visibility= View.GONE
    }
}
fun getSimpleClassName(activity: Activity): String {
    val className = activity.localClassName
    val pos = className.lastIndexOf('.') + 1
    return className.substring(pos)
}




fun getDateFormat(value: Long): String {
    return if (value != 0L) {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        format.format(Date(value))
    } else {
        value.toString()
    }
}


