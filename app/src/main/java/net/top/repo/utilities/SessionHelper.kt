package net.top.repo.utilities

import android.content.Context
import android.content.SharedPreferences
import androidx.collection.ArrayMap
import com.google.gson.Gson


class SessionHelper(val context: Context) {

    companion object {
        private const val PREF_NAME = "TopRepo"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_HOST = "host"
        private const val KEY_DATE_FILTER_VALUE = "date_filter_value"
        private const val KEY_STAR_FILTER_VALUE = "star_filter_value"

    }

    private var preferences: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    init {
        preferences = context.getSharedPreferences (PREF_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit ()
    }


    fun setDateFilterValue(value:String){
        editor!!.putString(KEY_DATE_FILTER_VALUE, value)
        editor!!.commit()
    }
    fun getDateFilterValue():String?{
        val s = preferences.getString(KEY_DATE_FILTER_VALUE, "")
        return s
    }


    fun setStarFilterValue(value:String){
        editor!!.putString(KEY_STAR_FILTER_VALUE, value)
        editor!!.commit()
    }
    fun getStarFilterValue():String?{
        val s = preferences.getString(KEY_STAR_FILTER_VALUE, "")
        return s
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        editor!!.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor!!.commit()
    }



    fun setHost(url: String?) {
        editor!!.putString(KEY_HOST, url)
        editor!!.commit()
    }

    fun getHost(): String? {
        // default dev server
        var host= preferences.getString(KEY_HOST, Constants.devServerLink.replace("https://", "").replace("/", ""))
        return host
    }

}