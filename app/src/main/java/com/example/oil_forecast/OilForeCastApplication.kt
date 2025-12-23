package com.example.oil_forecast

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.oil_forecast.Utils.NetworkUtils
import com.example.oil_forecast.di.dataModule
import com.example.oil_forecast.di.ioDispatcherModule
import com.example.oil_forecast.di.moshiModule
import com.example.oil_forecast.di.networkModule
import com.example.oil_forecast.di.viewModule
import com.example.oil_forecast.work.AqiCheckWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class OilForeCastApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkUtils.initialize(this)
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

        createNotificationChannel()
        setupRecurringWork()
    }

    private fun createNotificationChannel() {
        val name = "AQI Alert"
        val descriptionText = "Notifications for unhealthy AQI levels"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel("AQI_NOTIFICATION_CHANNEL", name, importance).apply {
                description = descriptionText
            }
        val notificationManager:
            NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun setupRecurringWork() {
        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        // --- 為了測試，暫時將週期性任務換成一次性任務 ---
//        val testRequest = OneTimeWorkRequestBuilder<AqiCheckWorker>()
//            .setConstraints(constraints)
//            .build()
//
//        WorkManager.getInstance(applicationContext).enqueue(testRequest)

        val repeatingRequest =
            PeriodicWorkRequestBuilder<AqiCheckWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "AqiCheckWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest,
        )
    }
}
