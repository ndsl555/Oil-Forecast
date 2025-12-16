package com.example.oil_forecast

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.example.oil_forecast.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity :
    AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val bottomNavView: BottomNavigationView = findViewById(R.id.mainBottomNavigationView)
        val navHostFragment = supportFragmentManager.findNavHostFragment(R.id.navHostView)
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_home_graph)

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
}

private fun FragmentManager.findNavHostFragment(
    @IdRes id: Int,
) = findFragmentById(id) as NavHostFragment
