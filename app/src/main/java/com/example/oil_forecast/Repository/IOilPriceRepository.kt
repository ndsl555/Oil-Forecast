package com.example.oil_forecast.Repository

import com.example.oil_forecast.Entity.OilPrice
import com.example.oil_forecast.Utils.Result

interface IOilPriceRepository {
    suspend fun getOilPrice(): Result<List<OilPrice>>
}
