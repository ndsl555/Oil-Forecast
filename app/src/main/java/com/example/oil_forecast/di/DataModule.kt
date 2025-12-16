package com.example.oil_forecast.di

import com.example.oil_forecast.Repository.ForeCastRepository
import com.example.oil_forecast.Repository.IForeCastRepository
import org.koin.dsl.module

val dataModule =
    module {
        includes(ioDispatcherModule, moshiModule)
        factory<IForeCastRepository> { ForeCastRepository(get(), get(koinIO)) }
    }
