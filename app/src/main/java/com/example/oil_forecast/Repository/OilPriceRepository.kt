package com.example.oil_forecast.Repository

import com.example.oil_forecast.DataSource.IOilPriceRemoteDataSource
import com.example.oil_forecast.Entity.OilPrice
import com.example.oil_forecast.Utils.Result

class OilPriceRepository(
    private val remoteDataSource: IOilPriceRemoteDataSource,
) : IOilPriceRepository {
    override suspend fun getOilPrice(): Result<List<OilPrice>> {
        return remoteDataSource.getOilPrice()
    }
}
