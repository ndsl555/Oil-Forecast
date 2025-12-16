package com.example.oil_forecast.DataSource

import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.Utils.Result

interface IAQIRemoteDataSource {
    suspend fun fetchAQIData(): Result<List<AQIEntity>>
}
