package com.example.oil_forecast

import android.app.Application
import com.example.oil_forecast.di.dataModule
import com.example.oil_forecast.di.ioDispatcherModule
import com.example.oil_forecast.di.moshiModule
import com.example.oil_forecast.di.networkModule
import com.example.oil_forecast.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OilForeCastApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@OilForeCastApplication)
            modules(
                listOf(
                    dataModule,
                    viewModule,
                    ioDispatcherModule,
                    moshiModule,
                    networkModule,
                ),
            )
        }
    }
}
