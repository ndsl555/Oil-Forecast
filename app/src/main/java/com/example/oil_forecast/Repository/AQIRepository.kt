package com.example.oil_forecast.Repository

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
}
