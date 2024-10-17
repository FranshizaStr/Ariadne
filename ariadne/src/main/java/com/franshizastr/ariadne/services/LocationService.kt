package com.franshizastr.ariadne.services

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.franshizastr.ariadne.MainActivity
import com.franshizastr.ariadne.R
import com.franshizastr.ariadne.application.appComponent
import com.franshizastr.ariadne.di.DaggerLocationServiceComponent
import com.franshizastr.core.contextInterfaces.AndroidNotificationChannelSetupInterface.Companion.CHANNEL_ID
import com.franshizastr.records.usecases.StartGpsRecordingUseCase
import com.franshizastr.records.usecases.StopGpsRecordingUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationService : Service() {

    private var isStarted = false
    private var coroutineJob: Job? = null
    private var coroutineScope: CoroutineScope? = null
    @Inject
    lateinit var startGpsRecordingUseCase: StartGpsRecordingUseCase
    @Inject
    lateinit var stopGpsRecordingUseCase: StopGpsRecordingUseCase

    override fun onCreate() {
        DaggerLocationServiceComponent.builder()
            .recordDeps(this.appComponent)
            .build()
            .inject(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val forcedClosure = intent?.getBooleanExtra(FORCED_CLOSURE, false) ?: false
        if (!isStarted) {
            startLocationService()
        } else if (forcedClosure) {
            coroutineScope?.launch {
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {

        fun resetStartedFlag() {
            isStarted = false
            super.onDestroy()
        }

        coroutineScope?.launch {
            async { stopGpsRecordingUseCase.execute() }.await()
            clearCoroutineScope()
            isStarted = false
        } ?: resetStartedFlag()
    }

    private fun startLocationService() {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        val closeServiceIntent = Intent(
            this,
            LocationService::class.java
        ).apply {
            putExtra(FORCED_CLOSURE, true)
        }
        val openAppIntent = Intent(
            this.applicationContext,
            MainActivity::class.java
        )
        val notification = notificationBuilder
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Ведется запись местоположения")
            .setContentText("Пожалуйста, не смахивайте уведомление")
            .addAction(
                R.drawable.baseline_close_24,
                "Остановить запись",
                PendingIntent.getService(
                    this.applicationContext,
                    FORCE_CLOSE_SERVICE_CODE,
                    closeServiceIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setContentIntent(
                PendingIntent.getActivity(
                    this.applicationContext,
                    OPEN_APP_CODE,
                    openAppIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setOngoing(true)
            .build()
        startForeground(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                FOREGROUND_SERVICE_TYPE_LOCATION
            } else {
                0
            },
            notification
        )
        startCoroutineScope()
        coroutineScope?.launch {
            startGpsRecordingUseCase.execute { message ->
                val locationNotification = notificationBuilder
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Новая точка")
                    .setContentText(message)
                    .setOngoing(true)
                    .build()
                with(NotificationManagerCompat.from(this@LocationService)) {
                    if (
                        ActivityCompat.checkSelfPermission(
                            this@LocationService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                         notify(LOCATION_MESSAGE_ID, locationNotification)
                    }
                }
            }
        }
        isStarted = true
    }

    private fun startCoroutineScope() {
        val job = Job()
        coroutineJob = job
        coroutineScope = CoroutineScope(job + Dispatchers.IO)
    }

    private fun clearCoroutineScope() {
        coroutineJob?.cancel()
        coroutineJob = null
        coroutineScope?.cancel()
        coroutineScope = null
    }

    companion object {
        private const val LOCATION_MESSAGE_ID = 42
        private const val FORCE_CLOSE_SERVICE_CODE = 21
        private const val OPEN_APP_CODE = 10
        private const val FORCED_CLOSURE = "forcedClosure"
    }
}