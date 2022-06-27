package net.top.repo.utilities

import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun Any.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this.toString(), duration).apply { show() }
}

fun Any.getCanonicalName(fragment: Fragment): String {
    return fragment.javaClass.canonicalName
}


fun Window.disableUserInteraction() = this.setFlags(
    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

fun Window.enableUserInteraction() = this.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)


fun Long.epochToDateString(formatString: String?): String? {
    val sdf = SimpleDateFormat(formatString, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(this)
}


fun String.convertStringToDateObject(dateFormat: String = "yyyy-MM-dd HH:mm"): Long {
    try{
        val replaceTZ = this.replace("T", " ").replace("Z", "")
        return replaceTZ.toDate(dateFormat).time
    }catch (ex: Exception) {

    }
    return 0L
}

fun String.toDate(
    dateFormat: String = "yyyy-MM-dd HH:mm",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date {
    try{
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }catch (ex: Exception) {

    }
    return Calendar.getInstance().time
}

