package com.example.oil_forecast.Entity

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val records: RecordsDto,
)

data class RecordsDto(
    @SerializedName("Locations")
    val locations: List<LocationsDto>,
)

data class LocationsDto(
    @SerializedName("Location")
    val location: List<LocationDto>,
)

data class LocationDto(
    @SerializedName("LocationName")
    val locationName: String,
    @SerializedName("Geocode")
    val geocode: String,
    @SerializedName("WeatherElement")
    val weatherElement: List<WeatherElementDto>,
)

data class WeatherElementDto(
    @SerializedName("ElementName")
    val elementName: String,
    @SerializedName("Time")
    val time: List<TimeDto>,
)

data class TimeDto(
    @SerializedName("StartTime")
    val startTime: String,
    @SerializedName("EndTime")
    val endTime: String,
    @SerializedName("ElementValue")
    val elementValue: List<Map<String, String>>,
)
