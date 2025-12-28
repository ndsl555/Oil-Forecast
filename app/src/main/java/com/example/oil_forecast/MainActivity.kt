package com.example.oil_forecast

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.oil_forecast.Utils.LocationUtils
import com.example.oil_forecast.Utils.NetworkUtils
import com.example.oil_forecast.Utils.NetworkUtils.getNetworkType
import com.example.oil_forecast.Utils.NetworkUtils.isNetworkConnected
import com.example.oil_forecast.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity :
    AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkNetworkStatus()
        observeNetworkState()
        initView()
    }

    override fun onResume() {
        super.onResume()
        fetchCityName()
    }

    private fun fetchCityName() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 檢查位置權限
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 取得最後已知位置
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude

                        // 使用你的工具類別來取得城市名稱
                        val cityName = LocationUtils.getCityFromLocation(this, latitude, longitude)

                        if (cityName != null) {
                            Log.d("Location", "Current City: $cityName")
                            // 儲存城市名稱
                            val locationPref = LocationPref(this)
                            locationPref.saveLocation(cityName)
                        } else {
                            Log.d("Location", "City not found")
                        }
                    } else {
                        Log.d("Location", "Last location is null")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Location", "Error getting location", e)
                }
        } else {
            // 如果沒有權限，你需要向使用者請求權限
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch the city name
                fetchCityName()
            } else {
                // Permission denied. Show a message to the user.
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                Log.d("Location", "Location permission denied by user")
            }
        }
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
        NoNetworkDialogFragment().show(supportFragmentManager, "NoNetworkDialogFragment")
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
