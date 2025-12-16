package com.example.oil_forecast.DataSource

import android.util.Log
import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.NetWork.AirQualityService
import com.example.oil_forecast.Utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AQIRemoteDataSource(
    private val airQualityService: AirQualityService,
    private val ioDispatcher: CoroutineDispatcher,
) : IAQIRemoteDataSource {
    override suspend fun fetchAQIData(): Result<List<AQIEntity>> =
        withContext(ioDispatcher) {
            try {
                val response =
                    airQualityService.getAirQualityData(
                        apiKey = "af57253c-e838-46da-a1f5-12b43afd75f3",
                    )

                if (response.isSuccessful) {
                    val body = response.body()
                    val records =
                        body?.records?.map { record ->
                            AQIEntity(
                                siteName = record.sitename,
                                county = record.county,
                                aqi = record.aqi.toIntOrNull(),
                                pollutant = record.pollutant,
                                status = record.status,
                                so2 = record.so2.toDoubleOrNull(),
                                co = record.co.toDoubleOrNull(),
                                o3 = record.o3.toDoubleOrNull(),
                                o3_8hr = record.o3_8hr.toDoubleOrNull(),
                                pm10 = record.pm10.toDoubleOrNull(),
                                pm2_5 = record.pm2_5.toDoubleOrNull(),
                                no2 = record.no2.toDoubleOrNull(),
                                nox = record.nox.toDoubleOrNull(),
                                no = record.no.toDoubleOrNull(),
                                windSpeed = record.windSpeed.toDoubleOrNull(),
                                windDirec = record.windDirec.toIntOrNull(),
                                publishTime = record.publishTime,
                                co_8hr = record.co_8hr.toDoubleOrNull(),
                                pm2_5_avg = record.pm2_5_avg.toDoubleOrNull(),
                                pm10_avg = record.pm10_avg.toDoubleOrNull(),
                                so2_avg = record.so2_avg.toDoubleOrNull(),
                                longitude = record.longitude.toDoubleOrNull(),
                                latitude = record.latitude.toDoubleOrNull(),
                                siteId = record.siteId,
                            )
                        } ?: emptyList()
                    Result.Success(records)
                } else {
                    Result.Error(Exception("API error code: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("AQIRemoteDataSource", "Error fetching AQI data", e)
                Result.Error(e)
            }
        }
}
