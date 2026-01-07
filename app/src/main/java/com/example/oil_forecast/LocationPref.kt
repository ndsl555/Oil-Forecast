package com.example.oil_forecast

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class LocationPref(context: Context) {
    companion object {
        private const val PREF_NAME = "location_pref"
        private const val KEY_LOCATION = "key_location" // 將 key 的名稱改得更清楚
        private const val KEY_LATITUDE = "key_latitude"
        private const val KEY_LONGITUDE = "key_longitude"
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * 儲存地點字串.
     * @param location 要儲存的地點字串.
     */
    fun saveLocation(location: String) {
        pref.edit { putString(KEY_LOCATION, location) }
    }

    /**
     * 取得已儲存的地點字串.
     * @return 已儲存的地點字串，如果找不到則為 null.
     */
    fun getLocation(): String? {
        return pref.getString(KEY_LOCATION, null)
    }

    /**
     * 儲存經緯度.
     * @param lat 緯度.
     * @param lng 經度.
     */
    fun saveLatLng(
        lat: Double,
        lng: Double,
    ) {
        pref.edit {
            putString(KEY_LATITUDE, lat.toString())
            putString(KEY_LONGITUDE, lng.toString())
        }
    }

    /**
     * 取得已儲存的經緯度.
     * @return 包含經緯度的 Pair，如果找不到則為 null.
     */
    fun getLatLng(): Pair<Double, Double>? {
        val latString = pref.getString(KEY_LATITUDE, null)
        val lngString = pref.getString(KEY_LONGITUDE, null)

        return if (latString != null && lngString != null) {
            try {
                Pair(latString.toDouble(), lngString.toDouble())
            } catch (e: NumberFormatException) {
                null
            }
        } else {
            null
        }
    }
}
