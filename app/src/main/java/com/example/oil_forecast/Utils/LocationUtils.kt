package com.example.oil_forecast.Utils

import android.content.Context
import android.location.Geocoder
import java.util.Locale

object LocationUtils {
    /**
     * Get city name from latitude and longitude
     */
    fun getCityFromLocation(
        context: Context,
        latitude: Double,
        longitude: Double,
    ): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                return addresses[0].adminArea // In Taiwan, adminArea is the city
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
