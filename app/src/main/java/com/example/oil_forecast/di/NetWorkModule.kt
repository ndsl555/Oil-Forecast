package com.example.oil_forecast.di

import com.example.oil_forecast.DataSource.ForeCastRemoteDataSource
import com.example.oil_forecast.DataSource.IForeCastRemoteDataSource
import com.example.oil_forecast.NetWork.ForeCastService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule =
    module {
        single {
            Retrofit.Builder()
                .baseUrl("https://opendata.cwa.gov.tw/api/v1/rest/datastore/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // 我如果還要讀其他隻api 可以加在這?
        }

        factory {
            get<Retrofit>().create(ForeCastService::class.java)
            // 我如果還要讀其他隻api 可以加在這?
        }

        factory<IForeCastRemoteDataSource> { ForeCastRemoteDataSource(get(), get(koinIO)) }
    }
