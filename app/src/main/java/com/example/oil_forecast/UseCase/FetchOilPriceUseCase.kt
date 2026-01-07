package com.example.oil_forecast.UseCase

import com.example.oil_forecast.Entity.OilPrice
import com.example.oil_forecast.Repository.IOilPriceRepository
import com.example.oil_forecast.Utils.Result
import com.example.oil_forecast.Utils.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class FetchOilPriceUseCase(
    private val oilPriceRepository: IOilPriceRepository,
    dispatcher: CoroutineDispatcher,
) : UseCase<Unit, List<OilPrice>>(dispatcher) {
    override suspend fun execute(parameters: Unit): Result<List<OilPrice>> {
        return oilPriceRepository.getOilPrice()
    }
}
