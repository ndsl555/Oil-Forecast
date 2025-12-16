// ForeCastRepository.kt
package com.example.oil_forecast.Repository

import com.example.oil_forecast.DataSource.IForeCastRemoteDataSource
import com.example.oil_forecast.Entity.ForecastEntity
import com.example.oil_forecast.Entity.LocationEntity
import com.example.oil_forecast.Utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ForeCastRepository(
    private val dataSource: IForeCastRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : IForeCastRepository {
    override suspend fun getAllLocations(): Result<List<LocationEntity>> =
        withContext(ioDispatcher) {
            dataSource.fetchLocations()
        }

    override suspend fun getForecastByGeocode(geocode: String): Result<List<ForecastEntity>> =
        withContext(ioDispatcher) {
            dataSource.fetchForecastByGeocode(geocode)
        }
}
