package com.example.oil_forecast.di

import com.example.oil_forecast.ViewModels.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModule: Module =
    module {
        includes(ioDispatcherModule, domainModule, moshiModule)
        viewModel { WeatherViewModel(get(), get()) }
    }
