package com.example.oil_forecast.Repository

import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.Utils.Result

interface IAQIRepository {
    suspend fun getAllAQIData(): Result<List<AQIEntity>>
}
