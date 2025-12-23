package com.example.oil_forecast.work

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.oil_forecast.MainActivity
import com.example.oil_forecast.R
import com.example.oil_forecast.Repository.IAQIRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.example.oil_forecast.Utils.Result as DomainResult

class AqiCheckWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams), KoinComponent {
    private val aqiRepository: IAQIRepository by inject()

    override suspend fun doWork(): ListenableWorker.Result {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)

        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return ListenableWorker.Result.failure()
        }

        return try {
            val location = fusedLocationClient.lastLocation.await()
            if (location != null) {
                when (val result = aqiRepository.getAqiByLocation(location.latitude, location.longitude)) {
                    is DomainResult.Success -> {
                        val aqiData = result.data
                        val isUnhealthy =
                            when (aqiData.status) {
                                "對敏感族群不健康", "對所有族群不健康", "非常不健康", "危害" -> true
                                else -> false
                            }

                        if (isUnhealthy) {
                            sendAqiNotification(
                                context = appContext,
                                siteName = aqiData.siteName,
                                aqi = aqiData.aqi.toString(),
                                status = aqiData.status,
                            )
                        }
                    }
                    is DomainResult.Error -> {
                        // Handle error, maybe log it
                    }
                }
            }
            ListenableWorker.Result.success()
        } catch (e: Exception) {
            ListenableWorker.Result.failure()
        }
    }

    private fun sendAqiNotification(
        context: Context,
        siteName: String,
        aqi: String,
        status: String,
    ) {
        val channelId = "AQI_NOTIFICATION_CHANNEL"
        val notificationId = 101

        val intent =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.baseline_masks_24) // Using an existing icon for now
                .setContentTitle("空污警報: $siteName")
                .setContentText("AQI: $aqi ($status)")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}
