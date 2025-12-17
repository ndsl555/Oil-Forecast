package com.example.oil_forecast.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AQIEntity(
    val siteName: String,
    val county: String,
    val aqi: Int?,
    val pollutant: String,
    val status: String,
    val so2: Double?,
    val co: Double?,
    val o3: Double?,
    val o3_8hr: Double?,
    val pm10: Double?,
    val pm2_5: Double?,
    val no2: Double?,
    val nox: Double?,
    val no: Double?,
    val windSpeed: Double?,
    val windDirec: Int?,
    val publishTime: String,
    val co_8hr: Double?,
    val pm2_5_avg: Double?,
    val pm10_avg: Double?,
    val so2_avg: Double?,
    val longitude: Double,
    val latitude: Double,
    val siteId: String,
) : Parcelable
