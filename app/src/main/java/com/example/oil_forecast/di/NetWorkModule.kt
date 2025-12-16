package com.example.oil_forecast.di

import com.example.oil_forecast.DataSource.AQIRemoteDataSource
import com.example.oil_forecast.DataSource.ForeCastRemoteDataSource
import com.example.oil_forecast.DataSource.IAQIRemoteDataSource
import com.example.oil_forecast.DataSource.IForeCastRemoteDataSource
import com.example.oil_forecast.NetWork.AirQualityService
import com.example.oil_forecast.NetWork.ForeCastService
import com.google.gson.GsonBuilder
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule =
    module {
        single {
            GsonBuilder()
                .setLenient()
                .create()
        }

        single(named("forecastRetrofit")) {
            Retrofit.Builder()
                .baseUrl("https://opendata.cwa.gov.tw/api/v1/rest/datastore/")
                .addConverterFactory(GsonConverterFactory.create(get()))
                .build()
        }

        single(named("aqiRetrofit")) {
            Retrofit.Builder()
                .baseUrl("https://data.moenv.gov.tw/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(get()))
                .build()
        }

        factory {
            get<Retrofit>(named("forecastRetrofit")).create(ForeCastService::class.java)
        }
        factory {
            get<Retrofit>(named("aqiRetrofit")).create(AirQualityService::class.java)
        }

        factory<IForeCastRemoteDataSource> { ForeCastRemoteDataSource(get(), get(koinIO)) }
        factory<IAQIRemoteDataSource> { AQIRemoteDataSource(get(), get(koinIO)) }
    }
