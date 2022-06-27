package net.top.repo.utilities

import android.content.Context
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.TouchDelegate
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.work.WorkInfo
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*

class AppUtils {

    companion object {
        const val TAG = "AppUtils"
        fun isNetworkAvailable(context: Context): Boolean {
            var isConnected = false
            var isRouteExist = false
            when(getNetworkType(context)) {
                "-", "?" -> {
                    isConnected = false
                }
                "2G" -> {
                    isConnected = false
                }
                "3G", "4G", "5G" -> {
                    isConnected = true
                }
                "WIFI" -> {
                    isConnected = true
                }
            }
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    isRouteExist = isNetworkRouteExists()
                }.join()
            }
            return isConnected && isRouteExist
        }

        private fun getNetworkType(context: Context): String? {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            if (info == null || !info.isConnected) return "-" // not connected
            if (info.type == ConnectivityManager.TYPE_WIFI) return "WIFI"
            if (info.type == ConnectivityManager.TYPE_MOBILE) {
                val networkType = info.subtype
                return when (networkType) {
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN, TelephonyManager.NETWORK_TYPE_GSM -> "2G"
                    TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP, TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "3G"
                    TelephonyManager.NETWORK_TYPE_LTE, TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> "4G"
                    TelephonyManager.NETWORK_TYPE_NR -> "5G"
                    else -> "?"
                }
            }
            return "?"
        }

        private fun isNetworkRouteExists(): Boolean {
            //Check if we can access a remote server
            var routeExists: Boolean
            try {
                //Check Google Public DNS
                val host: InetAddress = InetAddress.getByName("8.8.8.8")
                val s = Socket()
                s.connect(InetSocketAddress(host, 53), 1500)
                //It exists if no exception is thrown
                routeExists = true
                s.close()
            } catch (e: IOException) {
                routeExists = false
            }
            return routeExists
        }


    }

}