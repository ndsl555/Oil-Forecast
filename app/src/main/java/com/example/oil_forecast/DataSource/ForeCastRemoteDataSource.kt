package com.example.oil_forecast.DataSource

import android.util.Log
import com.example.oil_forecast.Entity.ForecastEntity
import com.example.oil_forecast.Entity.LocationEntity
import com.example.oil_forecast.Entity.WeatherElementDto
import com.example.oil_forecast.Extension.toResult
import com.example.oil_forecast.NetWork.ForeCastService
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.successOrNot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ForeCastRemoteDataSource(
    private val foreCastService: ForeCastService,
    private val ioDispatcher: CoroutineDispatcher,
) : IForeCastRemoteDataSource {
    override suspend fun fetchLocations(): Result<List<LocationEntity>> =
        withContext(ioDispatcher) {
            try {
                Log.d("ForeCastRemoteDataSource", "Fetching all forecast data...")
                val res =
                    foreCastService.getAllForeCast(
                        id = "F-D0047-091",
                        authKey = "rdec-key-123-45678-011121314",
                    )
                val body = res.body()?.records
                Log.d("ForeCastRemoteDataSource body", "Result: $body")
                res.toResult().successOrNot { body ->
                    Log.d("ForeCastRemoteDataSource", "locations: ${body.records.locations}")

                    // 完全安全處理 null
                    body.records.locations
                        .flatMap { it.location }
                        .map { loc ->
                            LocationEntity(
                                geocode = loc.geocode,
                                locationName = loc.locationName,
                            )
                        }
                }
            } catch (e: Exception) {
                Log.e("ForeCastRemoteDataSource", "Error fetching forecast data", e)
                Result.Error(e)
            }
        }

    override suspend fun fetchForecastByGeocode(geocode: String): Result<List<ForecastEntity>> =
        withContext(ioDispatcher) {
            try {
                val res =
                    foreCastService.getAllForeCast(
                        id = "F-D0047-091",
                        authKey = "rdec-key-123-45678-011121314",
                    )
                res.toResult().successOrNot { body ->
                    val location =
                        body.records.locations
                            .flatMap { it.location }
                            .firstOrNull { it.geocode == geocode }
                            ?: throw Exception("Location not found: $geocode")

                    mergeWeatherElements(geocode, location.weatherElement)
                }
            } catch (e: Exception) {
                Log.e("ForeCastRemoteDataSource", "Error fetching forecast data by geocode", e)
                Result.Error(e)
            }
        }

    private fun mergeWeatherElements(
        geocode: String,
        elements: List<WeatherElementDto>,
    ): List<ForecastEntity> {
        val map = mutableMapOf<String, ForecastEntity>()

        elements.forEach { element ->

            Log.d("WeatherDebug", "elementName = ${element.elementName}")

            element.time.forEach { time ->
                val key = "${time.startTime}_${time.endTime}"

                val base =
                    map.getOrPut(key) {
                        ForecastEntity(
                            geocode = geocode,
                            startTime = time.startTime.substring(5),
                            endTime = time.endTime.substring(5),
                            avgTemp = null,
                            maxTemp = null,
                            minTemp = null,
                            pop = null,
                            relativeHumidity = null,
                            windSpeed = null,
                            windDirection = null,
                            uVExposureLevel = null,
                            uVIndex = null,
                            dewPoint = null,
                        )
                    }

                val value = time.elementValue.firstOrNull() ?: return@forEach

                map[key] =
                    when (element.elementName) {
                        "平均溫度" ->
                            base.copy(
                                avgTemp = value["Temperature"]?.toInt(),
                            )

                        "最高溫度" ->
                            base.copy(
                                maxTemp = value["MaxTemperature"]?.toInt(),
                            )

                        "最低溫度" ->
                            base.copy(
                                minTemp = value["MinTemperature"]?.toInt(),
                            )

                        "12小時降雨機率" ->
                            base.copy(
                                pop = value["ProbabilityOfPrecipitation"]?.takeIf { it != "-" }?.toInt(),
                            )

                        "平均相對濕度" ->
                            base.copy(
                                relativeHumidity = value["RelativeHumidity"],
                            )

                        "風速" ->
                            base.copy(
                                windSpeed = value["WindSpeed"],
                            )

                        "風向" ->
                            base.copy(
                                windDirection = value["WindDirection"],
                            )
                        "紫外線指數" ->
                            base.copy(
                                uVExposureLevel = value["UVExposureLevel"],
                                uVIndex = value["UVIndex"]?.toInt(),
                            )
                        "平均露點溫度" ->
                            base.copy(
                                dewPoint = value["DewPoint"]?.toInt(),
                            )

                        else -> base
                    }
            }
        }

        return map.values.sortedBy { it.startTime }
    }
}
