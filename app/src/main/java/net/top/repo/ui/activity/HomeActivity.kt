package net.top.repo.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.commit
import androidx.work.*
import net.top.repo.R
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import net.top.repo.databinding.ActivityHomeBinding
import net.top.repo.ui.fragment.HomeFragment
import net.top.repo.utilities.*
import net.top.repo.utilities.KeyboardListenerUtils.SoftKeyboardToggleListener
import net.top.repo.viewmodels.HomeViewModel

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SoftKeyboardToggleListener {

    lateinit var homeActivityBinding: ActivityHomeBinding
    lateinit var sessionHelper: SessionHelper
    private lateinit var workManager: WorkManager
    var savedInstanceState: Bundle?=null
    var  intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
    private lateinit var  connectivityManager: ConnectivityManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivityBinding = ActivityHomeBinding.inflate(layoutInflater)
        val view = homeActivityBinding.root
        setContentView(view)
        this.savedInstanceState=savedInstanceState
        initialSetup()
        setupMainBottomNavigation()
        loadHomeFragment(savedInstanceState)
        initializeConnectivityManager()
        workManager = WorkManager.getInstance(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        KeyboardListenerUtils.removeAllKeyboardToggleListeners()
        KeyboardListenerUtils.addKeyboardToggleListener(this, this)
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onStop() {
        super.onStop()
        KeyboardListenerUtils.removeAllKeyboardToggleListeners()
    }
    override fun onDestroy() {
        unregisterReceiver(networkReceiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (homeActivityBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeActivityBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
          R.id.nav_menu -> {
              getString(R.string.under_development).toast(
                  this,
                  Toast.LENGTH_SHORT
              )
            }
            R.id.nav_logout -> {
                getString(R.string.under_development).toast(
                    this,
                    Toast.LENGTH_SHORT
                )
            }
        }

        homeActivityBinding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initialSetup(){
        sessionHelper = SessionHelper(applicationContext)
        supportActionBar.run {
            homeActivityBinding.mainToolbar
        }
        setupSideBarDrawer()


    }

    private fun setupSideBarDrawer(){
        homeActivityBinding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, homeActivityBinding.drawerLayout, homeActivityBinding.mainToolbar.mainToolbar,
            R.string.status_bar_notification_info_overflow, R.string.status_bar_notification_info_overflow
        )

        toggle.isDrawerIndicatorEnabled = false
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.hamburger, theme)
        toggle.setHomeAsUpIndicator(drawable)

        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (homeActivityBinding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                homeActivityBinding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                homeActivityBinding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        homeActivityBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }




    private fun setupMainBottomNavigation(){
        homeActivityBinding.mainBottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_home -> {
                    loadHomeFragment(savedInstanceState)
                    // Respond to navigation item 1 click
                    true
                }
                R.id.menu_schedule -> {
                        getString(R.string.under_development).toast(
                            this,
                            Toast.LENGTH_SHORT
                        )
                    true
                }
                R.id.menu_settings -> {
                    getString(R.string.under_development).toast(
                        this,
                        Toast.LENGTH_SHORT
                    )
                    true
                }
                R.id.menu_analytics->{
                    getString(R.string.under_development).toast(
                        this,
                        Toast.LENGTH_SHORT
                    )
                    true

                }
                R.id.menu_task -> {
                    getString(R.string.under_development).toast(
                        this,
                        Toast.LENGTH_SHORT
                    )
                    true
                }
                else -> false
            }
        }
    }

    private fun loadHomeFragment(savedInstanceState: Bundle?){
        if(savedInstanceState == null) {
            supportFragmentManager.commit {
                val homeFragment = HomeFragment.newInstance()
                replace(R.id.mainFragmentContainer, homeFragment, getCanonicalName(homeFragment))
                setReorderingAllowed(true)
                addToBackStack(getCanonicalName(homeFragment))
            }
        }
    }

    // region sync
    private fun deviceConnectivityChange(isConnected: Boolean) {

    }

    private fun syncData(){
        //data will sync
    }
    //endregion

    // region ConnectivityManager
    private fun initializeConnectivityManager() {
        connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        registerReceiver(networkReceiver, intentFilter)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }

    fun updateConnection() {
        deviceConnectivityChange(AppUtils.isNetworkAvailable(applicationContext))
    }
    //endregion


    companion object {
        private const val TAG = "HomeActivity"
    }

    override fun onToggleSoftKeyboard(isVisible: Boolean) {
        if(isVisible) {
            homeActivityBinding.mainBottomNavigation.visibility = View.GONE
        }else {
            homeActivityBinding.mainBottomNavigation.visibility = View.VISIBLE
        }
    }
}





