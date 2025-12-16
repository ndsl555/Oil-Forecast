package com.example.oil_forecast.DataSource

import com.example.oil_forecast.Entity.ForecastEntity
import com.example.oil_forecast.Entity.LocationEntity
import com.example.oil_forecast.Utils.Result

interface IForeCastRemoteDataSource {
    suspend fun fetchLocations(): Result<List<LocationEntity>>

    suspend fun fetchForecastByGeocode(geocode: String): Result<List<ForecastEntity>>
}
