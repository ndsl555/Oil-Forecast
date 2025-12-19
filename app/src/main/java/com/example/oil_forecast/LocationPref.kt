package com.example.oil_forecast

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class LocationPref(context: Context) {
    companion object {
        private const val PREF_NAME = "location_pref"
        private const val KEY_LOCATION = "key_location" // 將 key 的名稱改得更清楚
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
}
