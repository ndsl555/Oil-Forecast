package com.example.oil_forecast.Repository

import com.example.oil_forecast.Entity.ForecastEntity
import com.example.oil_forecast.Entity.LocationEntity
import com.example.oil_forecast.Utils.Result

interface IForeCastRepository {
    suspend fun getAllLocations(): Result<List<LocationEntity>>

    suspend fun getForecastByGeocode(geocode: String): Result<List<ForecastEntity>>
}
