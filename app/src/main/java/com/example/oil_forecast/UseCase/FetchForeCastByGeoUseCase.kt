package com.example.oil_forecast.UseCase

import com.example.oil_forecast.DataSource.IForeCastRemoteDataSource
import com.example.oil_forecast.Entity.ForecastEntity
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class FetchForeCastByGeoUseCase(
    private val foreCastRemoteDataSource: IForeCastRemoteDataSource,
    dispatcher: CoroutineDispatcher,
) : UseCase<FetchForeCastByGeoUseCase.Params, List<ForecastEntity>>(dispatcher) {
    override suspend fun execute(parameters: Params): Result<List<ForecastEntity>> {
        return foreCastRemoteDataSource.fetchForecastByGeocode(parameters.geocode)
    }

    data class Params(
        val geocode: String,
    )
}
