package com.example.oil_forecast.UseCase

import com.example.oil_forecast.Entity.AQIEntity
import com.example.oil_forecast.Repository.IAQIRepository
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class FetchAQIUseCase(
    private val aqiRepository: IAQIRepository,
    dispatcher: CoroutineDispatcher,
) : UseCase<Unit, List<AQIEntity>>(dispatcher) {
    override suspend fun execute(parameters: Unit): Result<List<AQIEntity>> {
        return aqiRepository.getAllAQIData()
    }
}
