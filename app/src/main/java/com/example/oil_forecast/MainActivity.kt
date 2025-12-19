package com.example.oil_forecast

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.oil_forecast.Utils.NetworkUtils
import com.example.oil_forecast.Utils.NetworkUtils.getNetworkType
import com.example.oil_forecast.Utils.NetworkUtils.isNetworkConnected
import com.example.oil_forecast.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class MainActivity :
    AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkNetworkStatus()
        observeNetworkState()
        initView()
    }

    private fun observeNetworkState() {
        lifecycleScope.launch {
            NetworkUtils.networkState.collect { isConnected ->
                if (isConnected) {
                    // 網路恢复
                    showNetworkRestored()
                } else {
                    // 網路斷開
                    showNetworkLost()
                }
            }
        }
    }

    private fun showNetworkRestored() {
        // 显示網路恢复提示
        Toast.makeText(this, "網路已恢复", Toast.LENGTH_SHORT).show()
    }

    private fun showNetworkLost() {
        // 显示網路斷開提示
        showNoWifiDialog()
        Toast.makeText(this, "網路已斷開", Toast.LENGTH_SHORT).show()
    }

    private fun checkNetworkStatus() {
        if (isNetworkConnected(baseContext)) {
            val networkType = getNetworkType(baseContext)
            Toast.makeText(this, "網路已連接 - $networkType", Toast.LENGTH_SHORT).show()
        } else {
            showNoWifiDialog()
            Toast.makeText(this, "網路未連接", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNoWifiDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.network_type_no_network))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun initView() {
        val bottomNavView: BottomNavigationView = findViewById(R.id.mainBottomNavigationView)
        val navHostFragment = supportFragmentManager.findNavHostFragment(R.id.navHostView)
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_home_graph)

        appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.navigationWeatherFragment,
                    R.id.navigationAQIFragment,
                ),
            )

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavView.setOnItemSelectedListener {
            Log.d("FragmentCheck", "BottomNavigationView item clicked: ${it.itemId}")

            when (it.itemId) {
                R.id.navigation_aqi -> {
                    navController.navigate(NavHomeGraphDirections.actionGlobalToNavigationAQIFragment())
                }
                R.id.navigation_weather -> {
                    navController.navigate(NavHomeGraphDirections.actionGlobalToNavigationWeatherFragment())
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

private fun FragmentManager.findNavHostFragment(
    @IdRes id: Int,
) = findFragmentById(id) as NavHostFragment
