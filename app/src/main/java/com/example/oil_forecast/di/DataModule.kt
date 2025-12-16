package com.example.oil_forecast.di

import com.example.oil_forecast.Repository.AQIRepository
import com.example.oil_forecast.Repository.ForeCastRepository
import com.example.oil_forecast.Repository.IAQIRepository
import com.example.oil_forecast.Repository.IForeCastRepository
import org.koin.dsl.module

val dataModule =
    module {
        includes(ioDispatcherModule, moshiModule)
        factory<IForeCastRepository> { ForeCastRepository(get(), get(koinIO)) }
        factory<IAQIRepository> { AQIRepository(get(), get(koinIO)) }
    }
