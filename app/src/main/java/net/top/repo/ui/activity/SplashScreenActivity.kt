package net.top.repo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import net.top.repo.utilities.SessionHelper
import net.top.repo.R


@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 1000

    private lateinit var sessionHelper: SessionHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sessionHelper = SessionHelper(applicationContext)
        Handler().postDelayed({
            val appLinkIntent = intent
            val data = appLinkIntent.data
            if (data != null) {
                Log.d(
                    "smn_", "onCreateCheck: path: ${data.path} pathSegments: ${data.pathSegments}" +
                            " queryParameterNames: ${data.queryParameterNames}"
                )
                Log.d(
                    "smn_",
                    "onCreateCheck: access_token: ${data.getQueryParameter("access_token")}" +
                            " \nmultipass: ${data.getQueryParameter("multipass")}"
                )
                checkLoginStatus(data?.getQueryParameter("access_token")!!)
            } else {
                lunchActivity()
            }
        }, SPLASH_TIME_OUT)

    }

    private fun checkLoginStatus(token: String) {
        sessionHelper.setLoggedIn(true)
        setupLoginViewModel()
        return

    }

    private fun lunchActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun setupLoginViewModel() {
        //observe login response
    }


    companion object {
        const val TAG = "SplashScreenActivity"
    }
}

