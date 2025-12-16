package com.example.oil_forecast.UseCase

import com.example.oil_forecast.Entity.LocationEntity
import com.example.oil_forecast.Repository.IForeCastRepository
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class FetchLocationUseCase(
    private val foreCastRepository: IForeCastRepository,
    dispatcher: CoroutineDispatcher,
) : UseCase<Unit, List<LocationEntity>>(dispatcher) {
    override suspend fun execute(parameters: Unit): Result<List<LocationEntity>> {
        return foreCastRepository.getAllLocations()
    }
}
