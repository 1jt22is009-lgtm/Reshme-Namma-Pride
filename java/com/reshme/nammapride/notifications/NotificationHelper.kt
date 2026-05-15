package com.reshme.nammapride.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.reshme.nammapride.R
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object NotificationHelper {
    const val CHANNEL_ID = "harvest_alerts"
    private const val WORK_TAG = "harvest_timer"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Harvest alerts", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Alerts for spinning tray transfer time"
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    fun scheduleHarvestAlert(context: Context, batchId: String, breedName: String, harvestEpochDay: Long) {
        val targetMillis = LocalDate.ofEpochDay(harvestEpochDay).atTime(7, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val delay = (targetMillis - System.currentTimeMillis()).coerceAtLeast(TimeUnit.MINUTES.toMillis(1))
        val request = OneTimeWorkRequestBuilder<HarvestAlertWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("batchId" to batchId, "breed" to breedName))
            .addTag(WORK_TAG)
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }

    fun cancelAll(context: Context) { WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG) }
}

class HarvestAlertWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val breed = inputData.getString("breed") ?: "silkworm"
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return Result.success()
        }
        val notification = NotificationCompat.Builder(applicationContext, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Spinning tray transfer due")
            .setContentText("$breed batch is ready. Move mature worms to clean chandrika trays.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(applicationContext).notify(101, notification)
        return Result.success()
    }
}
