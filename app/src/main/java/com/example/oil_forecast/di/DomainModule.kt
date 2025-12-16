package com.example.oil_forecast.di

import com.example.oil_forecast.UseCase.FetchForeCastByGeoUseCase
import com.example.oil_forecast.UseCase.FetchLocationUseCase
import org.koin.dsl.module

val domainModule =
    module {
        includes(ioDispatcherModule, dataModule, moshiModule)
        factory { FetchLocationUseCase(get(), get(koinIO)) }
        factory { FetchForeCastByGeoUseCase(get(), get(koinIO)) }
    }
