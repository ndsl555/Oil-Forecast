package com.example.oil_forecast.Repository

import android.location.Location
import com.example.oil_forecast.DataSource.IAQIRemoteDataSource
import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.Utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AQIRepository(
    private val dataSource: IAQIRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : IAQIRepository {
    override suspend fun getAllAQIData(): Result<List<AQIEntity>> =
        withContext(ioDispatcher) {
            dataSource.fetchAQIData()
        }

    override suspend fun getAqiByLocation(
        latitude: Double,
        longitude: Double,
    ): Result<AQIEntity> =
        withContext(ioDispatcher) {
            when (val result = dataSource.fetchAQIData()) {
                is Result.Success -> {
                    val nearestAqi =
                        result.data.minByOrNull { aqiEntity ->
                            val stationLocation =
                                Location("").apply {
                                    this.latitude = aqiEntity.latitude.toDouble()
                                    this.longitude = aqiEntity.longitude.toDouble()
                                }
                            val userLocation =
                                Location("").apply {
                                    this.latitude = latitude
                                    this.longitude = longitude
                                }
                            userLocation.distanceTo(stationLocation)
                        }
                    if (nearestAqi != null) {
                        Result.Success(nearestAqi)
                    } else {
                        Result.Error(Exception("No AQI data found"))
                    }
                }
                is Result.Error -> result
            }
        }
}
