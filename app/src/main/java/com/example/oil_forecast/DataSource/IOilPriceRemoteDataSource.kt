package com.example.oil_forecast.DataSource

import com.example.oil_forecast.Entity.OilPrice
import com.example.oil_forecast.Utils.Result

interface IOilPriceRemoteDataSource {
    suspend fun getOilPrice(): Result<List<OilPrice>>
}
