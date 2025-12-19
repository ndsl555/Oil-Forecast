package com.example.oil_forecast.ui.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.oil_forecast.Entity.LocationEntity
import com.example.oil_forecast.WeatherPageFragment

class WeatherPagerAdapter(
    fragment: Fragment,
    private val locations: List<LocationEntity>,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = locations.size

    override fun createFragment(position: Int): Fragment {
        val location = locations[position]
        return WeatherPageFragment.newInstance(location.geocode, location.locationName)
    }
}
