package com.example.oil_forecast.Entity

data class ForecastEntity(
    val geocode: String,
    val startTime: String,
    val endTime: String,
    val avgTemp: Int?,
    val maxTemp: Int?,
    val minTemp: Int?,
    val pop: Int?,
    val relativeHumidity: String?,
    val windSpeed: String?,
    val windDirection: String?,
    val uVExposureLevel: String?,
)
