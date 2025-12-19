package com.example.oil_forecast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.oil_forecast.Entity.LocationEntity
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.WeatherViewModel
import com.example.oil_forecast.databinding.FragmentWeatherBinding
import com.example.oil_forecast.ui.Adapter.WeatherPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModel()

    private var locations: List<LocationEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        Log.d("FragmentCheck", "WeatherFragment loaded")

        initParam()

        viewModel.fetchLocations()
    }

    private fun initParam() {
        launchAndRepeatWithViewLifecycle {
            viewModel.locations.collect { locations ->
                this@WeatherFragment.locations = locations
                activity?.invalidateOptionsMenu()
                if (locations.isNotEmpty()) {
                    val pagerAdapter = WeatherPagerAdapter(this@WeatherFragment, locations)
                    binding.viewPager.adapter = pagerAdapter
                }
            }
        }
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater,
    ) {
        super.onCreateOptionsMenu(menu, inflater)
        // Clear previous menu items
        menu.clear()
        // Add new menu items from the locations list
        locations.forEachIndexed { index, location ->
            menu.add(Menu.NONE, index, index, location.locationName)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Set the ViewPager's current item to the selected menu item's ID
        binding.viewPager.currentItem = item.itemId
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
