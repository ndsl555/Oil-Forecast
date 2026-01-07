package com.example.oil_forecast.di

import com.example.oil_forecast.Repository.*
import org.koin.dsl.module

val dataModule =
    module {
        includes(ioDispatcherModule, moshiModule)
        factory<IForeCastRepository> { ForeCastRepository(get(), get(koinIO)) }
        factory<IAQIRepository> { AQIRepository(get(), get(koinIO)) }
        factory<IOilPriceRepository> { OilPriceRepository(get()) }
    }
