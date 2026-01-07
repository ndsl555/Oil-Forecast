package com.example.oil_forecast.UseCase

import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.Repository.IAQIRepository
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class FetchAQIByLocationUseCase(
    private val aqiRepository: IAQIRepository,
    dispatcher: CoroutineDispatcher,
) : UseCase<FetchAQIByLocationUseCase.Params, AQIEntity>(dispatcher) {
    override suspend fun execute(parameters: Params): Result<AQIEntity> {
        return aqiRepository.getAqiByLocation(parameters.latitude, parameters.longitude)
    }

    data class Params(
        val latitude: Double,
        val longitude: Double,
    )
}
